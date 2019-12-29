package com.geekhome.coremodule;

import com.geekhome.coremodule.jetty.IOUtils;
import com.geekhome.coremodule.httpserver.IHttpListenerRequest;
import com.geekhome.coremodule.httpserver.IResponse;
import com.geekhome.coremodule.httpserver.ResponseBase;
import com.geekhome.coremodule.jetty.RequestsDispatcherBase;

import java.io.*;


public class PhotosDispatcher extends RequestsDispatcherBase {
    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {

        String requestedContent = request.getUrl().getOriginalString().replace("/", File.separator);
        if (requestedContent.toUpperCase().startsWith(File.separator + "PHOTOS" + File.separator)) {
            String imagePath = new File(".").getCanonicalPath() + requestedContent;
            File f = new File(imagePath);
            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                byte[] content = IOUtils.inputStreamToByteArray(fis);
                ResponseBase response = new ResponseBase(content);
                response.setCacheable(true);
                return response;
            }
        }
        return null;
    }

    @Override
    public boolean isRequestSupported(IHttpListenerRequest request) {
        return isSpecificTypeOfFileRequest(request, ".GIF") ||
               isSpecificTypeOfFileRequest(request, ".JPG") ||
               isSpecificTypeOfFileRequest(request, ".PNG") ||
               isSpecificTypeOfFileRequest(request, ".SVG");
    }
}
