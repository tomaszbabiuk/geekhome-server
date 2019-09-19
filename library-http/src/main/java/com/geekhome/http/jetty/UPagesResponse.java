package com.geekhome.http.jetty;

import com.geekhome.http.*;

import java.io.IOException;
import java.util.ArrayList;

public class UPagesResponse extends UPagesResponseBase {

    interface IMatchDelegate {
        boolean match(byte byteToCheck);
    }

    private String _originalString;
    private ILocalizationProvider _resourceProvider;
    private IUContentProvider _uContentDispatchersSearcher;

    private String _contentPath;
    private int _statusCode;

    public UPagesResponse(String originalString, String contentPath, ILocalizationProvider resourceProvider, IUContentProvider uContentDispatchersSearcher) {
        this(originalString, contentPath, resourceProvider, uContentDispatchersSearcher, 200);
    }

    public UPagesResponse(String originalString, String contentPath, ILocalizationProvider resourceProvider, IUContentProvider uContentDispatchersSearcher, int statusCode) {
        super(resourceProvider, uContentDispatchersSearcher);
        setCacheable(true);
        _contentPath = contentPath;
        _originalString = originalString;
        _resourceProvider = resourceProvider;
        _uContentDispatchersSearcher = uContentDispatchersSearcher;
        _statusCode = statusCode;
    }

    @Override
    public void process(IHttpListenerResponse response) throws IOException, ContentNotFoundException {
        response.setStatusCode(_statusCode);
        response.setContentType(matchContentType(_originalString));
        response.flush();

        ArrayList<Chunk> firstChunks = new ArrayList<>();

        byte[] bytes = ("<!-- @" + _contentPath + " -->").getBytes();
        firstChunks.add(new Chunk(bytes, ChunkType.FileInclusion));

        while (firstChunks.size() > 0) {
            flushChunksIfPossible(firstChunks, response);
            resolveChunks(firstChunks);
        }

        response.flush();
    }

    protected static void flushChunksIfPossible(ArrayList<Chunk> chunks, IHttpListenerResponse response) throws IOException {
        if ((chunks != null) && (chunks.size() > 0)) {
            Chunk chunk = chunks.get(0);
            while (chunk != null && chunk.getType() == ChunkType.HtmlResolved && chunks.size() > 0) {
                if (chunk.getContent().length > 0) {
                    response.writeToOutputStream(chunk.getContent());
                }
                chunks.remove(0);
                chunk = (chunks.size() == 0) ? null : chunks.get(0);
            }
        }
    }

    protected void resolveChunks(ArrayList<Chunk> chunks) throws ContentNotFoundException {
        if (chunks.size() > 0) {
            for (int i = chunks.size() - 1; i >= 0; i--) {
                Chunk chunk = chunks.get(i);

                if (chunk.getType() == ChunkType.HtmlUnresolved || chunk.getType() == ChunkType.ParametersExtracted) {
                    ChunkType matchedChunkType = ChunkType.FileInclusion;
                    ChunkType unmatchedChunkType = ChunkType.HtmlUnresolved;
                    if (chunk.getType() == ChunkType.HtmlUnresolved) {
                        matchedChunkType = ChunkType.HtmlUnresolved;
                        unmatchedChunkType = ChunkType.HtmlResolved;
                    }

                    ArrayList<Chunk> processedChunks = splitToChunks(chunk.getContent(), "<!-- @", new IMatchDelegate() {
                        @Override
                        public boolean match(byte byteToCheck) {
                            return byteToCheck == 62;
                        }
                    }, false, matchedChunkType, unmatchedChunkType); // 62 = ">"
                    chunks.remove(i);
                    for (int j = processedChunks.size() - 1; j >= 0; j--) {
                        Chunk processedChunk = processedChunks.get(j);
                        chunks.add(i, processedChunk);
                    }
                } else if (chunk.getType() == ChunkType.FileInclusion) {
                    chunks.remove(i);
                    String[] parameters = splitChunkToParameters(chunk);
                    if ((parameters != null) && (parameters.length > 0)) {
                        if (parameters[0].indexOf("RES:") == 0) {
                            String resourceKey = parameters[0].substring(4);
                            byte[] resourceResolved = _resourceProvider.getValue(resourceKey).getBytes();

                            Chunk resourceFilledChunk = new Chunk(resourceResolved, ChunkType.HtmlResolved);
                            chunks.add(i, resourceFilledChunk);
                        } else {
                            String contentPath = parameters[0];
                            byte[] content = _uContentDispatchersSearcher.findInContentProviders(contentPath.toUpperCase());
                            if (parameters.length == 1) {
                                chunks.add(i, new Chunk(content, ChunkType.ParametersExtracted));
                            } else {
                                ArrayList<Chunk> processedChunks = splitToChunks(content, "$p", new IMatchDelegate() {
                                    @Override
                                    public boolean match(byte b) {
                                        return b >= ("0".getBytes())[0] && b <= ("9".getBytes())[0];
                                    }
                                }, true, ChunkType.ParameterInclusion, ChunkType.ParametersExtracted);
                                for (int j = processedChunks.size() - 1; j >= 0; j--) {
                                    Chunk processedChunk = processedChunks.get(j);
                                    if (processedChunk.getType() == ChunkType.ParameterInclusion) {
                                        int parameterNumber = extractParameterFromChunk(processedChunk);
                                        String parameterContent = parameters[parameterNumber + 1];
                                        if (parameterContent.indexOf("RES:") == 0) {
                                            String resourceKey = parameterContent.substring(4);
                                            parameterContent = _resourceProvider.getValue(resourceKey);
                                        } else if (parameterContent.indexOf("QUERYSTRING:") == 0) {
                                            String queryStringKey = parameterContent.substring(12);
                                            parameterContent = _resourceProvider.getValue(queryStringKey);
                                        }
                                        processedChunk.setContent(parameterContent.getBytes());
                                        processedChunk.setType(ChunkType.HtmlUnresolved);
                                    }
                                    chunks.add(i, processedChunk);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static int extractParameterFromChunk(Chunk chunk) {
        String parameterValueAsString = chunk.getContentAsString();
        return Integer.parseInt(parameterValueAsString.substring(2));
    }

    private static ArrayList<Chunk> splitToChunks(byte[] bytes, String pattern, IMatchDelegate matchDelegate, boolean follow, ChunkType matchingPatternChunksType, ChunkType unmatchingPatternChunksType) {
        byte[] patternAsBytes = pattern.getBytes();
        ArrayList<Integer> inclusions = ByteArrayRocks.locate(bytes, patternAsBytes);
        int start = 0;
        byte[] chunk;
        int length;
        ArrayList<Chunk> chunks = new ArrayList<>();
        for (int inclusion : inclusions) {
            int stop = inclusion;
            length = stop - start;
            chunk = new byte[length];

            System.arraycopy(bytes, start, chunk, 0, length);
            chunks.add(new Chunk(chunk, unmatchingPatternChunksType));
            start = stop;

            boolean endOfMarkFound = false;
            int endOfMarkPosition = stop + pattern.length();

            while (!endOfMarkFound) {
                if (follow) {
                    while (matchDelegate.match(bytes[endOfMarkPosition])) {
                        endOfMarkPosition++;
                    }
                    endOfMarkFound = true;
                } else {
                    if (matchDelegate.match(bytes[endOfMarkPosition])) // 62 = ">"
                    {
                        endOfMarkFound = true;
                    }
                    endOfMarkPosition++;
                }
            }

            length = endOfMarkPosition - stop;
            chunk = new byte[length];
            System.arraycopy(bytes, start, chunk, 0, length);
            chunks.add(new Chunk(chunk, matchingPatternChunksType));

            start = endOfMarkPosition;
        }

        length = bytes.length - start;
        chunk = new byte[length];
        System.arraycopy(bytes, start, chunk, 0, length);
        chunks.add(new Chunk(chunk, unmatchingPatternChunksType));

        return chunks;
    }

    private static String[] splitChunkToParameters(Chunk chunk) {
        String s = chunk.getContentAsString();
        return s.substring(6, s.length() - 4).split(" ");
    }
}