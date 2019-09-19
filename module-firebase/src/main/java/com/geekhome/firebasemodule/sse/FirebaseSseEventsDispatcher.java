package com.geekhome.firebasemodule.sse;

import okhttp3.*;
import okio.BufferedSource;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class FirebaseSseEventsDispatcher {
    private final FirebaseSseListener listener;
    private final Request originalRequest;

    private OkHttpClient client;
    private Call call;
    private Reader sseReader;

    private long readTimeoutMillis = 0;

    public FirebaseSseEventsDispatcher(Request request, FirebaseSseListener listener) {
        if (!"GET".equals(request.method())) {
            throw new IllegalArgumentException("Request must be GET: " + request.method());
        }
        this.originalRequest = request;
        this.listener = listener;
    }

    public void connect(OkHttpClient client) {
        this.client = client;
        prepareCall(originalRequest);
        enqueue();
    }

    private void prepareCall(Request request) {
        if (client == null) {
            throw new AssertionError("Client is null");
        }
        Request.Builder requestBuilder = request.newBuilder()
                .header("Accept-Encoding", "")
                .header("Accept", "text/event-stream")
                .header("Cache-Control", "no-cache");

        call = client.newCall(requestBuilder.build());
    }

    private void enqueue() {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                notifyFailure(e, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        openSse(response);
                    } catch (Exception e) {
                        notifyFailure(e, null);
                    }
                } else {
                    notifyFailure(new IOException(response.message()), response);
                }
            }
        });
    }

    private void openSse(Response response) throws Exception {
        sseReader = new Reader(response.body().source());
        sseReader.setTimeout(readTimeoutMillis, TimeUnit.MILLISECONDS);
        listener.onOpen();

        //noinspection StatementWithEmptyBody
        while (call != null && !call.isCanceled() && sseReader.read()) {
        }
    }

    private void notifyFailure(Throwable throwable, Response response) {
        listener.onClosed();
        close();
    }

    public Request request() {
        return originalRequest;
    }

    public void setTimeout(long timeout, TimeUnit unit) {
        if (sseReader != null) {
            sseReader.setTimeout(timeout, unit);
        }
        readTimeoutMillis = unit.toMillis(timeout);
    }

    public void close() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }

    /**
     * Internal reader for the SSE channel. This will wait for data being send and will parse it according to the
     * SSE standard.
     *
     * @see Reader#read()
     */
    private class Reader {

        private static final char COLON_DIVIDER = ':';

        private static final String DATA = "data";
        private static final String EVENT = "event";
        private static final String DEFAULT_EVENT = "message";
        private static final String EMPTY_STRING = "";

        private final BufferedSource source;

        private StringBuilder data = new StringBuilder();
        private String eventName = DEFAULT_EVENT;

        Reader(BufferedSource source) {
            this.source = source;
        }

        /**
         * Blocking call that will try to read a line from the source
         *
         * @return true if the read was successfully, false if an error was thrown
         */
        boolean read() throws Exception {
            try {
                String line = source.readUtf8LineStrict();
                processLine(line);
            } catch (IOException e) {
                notifyFailure(e, null);
                return false;
            }
            return true;
        }


        void setTimeout(long timeout, TimeUnit unit) {
            if (source != null) {
                source.timeout().timeout(timeout, unit);
            }
        }

        private void processLine(String line) throws Exception {
            if (line == null || line.isEmpty()) {
                dispatchEvent();
                return;
            }

            int colonIndex = line.indexOf(COLON_DIVIDER);
            if (colonIndex == 0) { // If line starts with COLON dispatch a comment
                listener.onComment(line.substring(1).trim());
            } else if (colonIndex != -1) { // Collect the characters on the line after the first U+003A COLON character (:), and let value be that string.
                String field = line.substring(0, colonIndex);
                String value = EMPTY_STRING;
                int valueIndex = colonIndex + 1;
                if (valueIndex < line.length()) {
                    if (line.charAt(valueIndex) == ' ') { // If value starts with a single U+0020 SPACE character, remove it from value.
                        valueIndex++;
                    }
                    value = line.substring(valueIndex);
                }
                processField(field, value);
            } else {
                processField(line, EMPTY_STRING);
            }
        }

        private void dispatchEvent() throws Exception {
            if (data.length() == 0) {
                return;
            }
            String dataString = data.toString();
            if (dataString.endsWith("\n")) {
                dataString = dataString.substring(0, dataString.length() - 1);
            }
            listener.onMessage(eventName, dataString);
            data.setLength(0);
            eventName = DEFAULT_EVENT;
        }

        private void processField(String field, String value) {
            if (DATA.equals(field)) {
                data.append(value).append('\n');
            } else if (EVENT.equals(field)) {
                eventName = value;
            }
        }
    }
}
