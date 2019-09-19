package com.geekhome.http.jetty;

import com.geekhome.http.IHttpListenerResponse;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.http.IUContentProvider;

import java.util.ArrayList;

public class ConsolidationResponse extends UPagesResponseBase {
    private ArrayList<String> _toConsolidate;
    private String _contentType;
    private IUContentProvider _searcher;

    public ConsolidationResponse(ArrayList<String> toConsolidate, String contentType, IUContentProvider searcher, ILocalizationProvider localizationProvider) {
        super(localizationProvider, searcher);
        _toConsolidate = toConsolidate;
        _contentType = contentType;
        _searcher = searcher;
    }

    @Override
    public void process(IHttpListenerResponse response) throws Exception {
        setCacheable(true);
        response.setStatusCode(200);
        response.setContentType(_contentType);
        response.flush();

        for (String consolidatedContent : _toConsolidate) {
            if (consolidatedContent.endsWith(".RESOURCES.JS")) {
                ArrayList<Chunk> firstChunks = new ArrayList<>();

                byte[] bytes = ("<!-- @" + consolidatedContent + " -->").getBytes();
                firstChunks.add(new Chunk(bytes, ChunkType.FileInclusion));

                while (firstChunks.size() > 0) {
                    flushChunksIfPossible(firstChunks, response);
                    resolveChunks(firstChunks);
                }
                response.writeToOutputStream("\r\n\r\n");
            } else {
                byte[] content = _searcher.findInContentProviders(consolidatedContent);
                response.writeToOutputStream(content);
                response.writeToOutputStream("\r\n\r\n");
            }
        }


        response.flush();
    }
}