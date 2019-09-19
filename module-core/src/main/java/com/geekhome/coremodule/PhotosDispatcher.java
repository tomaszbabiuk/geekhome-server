package com.geekhome.coremodule;

import com.geekhome.http.jetty.IOUtils;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.ResponseBase;
import com.geekhome.http.jetty.RequestsDispatcherBase;

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
