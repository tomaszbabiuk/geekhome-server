package com.geekhome.firebasemodule.sse;

public interface FirebaseSseListener {
    void onOpen();

    void onClosed();

    void onMessage(String eventName, String dataString) throws Exception;

    void onComment(String trim);
}
