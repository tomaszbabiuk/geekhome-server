package com.geekhome.coremodule.jetty;

import com.geekhome.coremodule.httpserver.ContentNotFoundException;
import com.geekhome.coremodule.httpserver.IUContentProvider;

import java.io.*;

public class EmbeddedResourcesUContentProvider implements IUContentProvider {
    public byte[] findInContentProviders(String contentPath) throws ContentNotFoundException {
        try {
            String path = contentPath.toLowerCase().replace('\\','/');

            InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
            return stream != null ? IOUtils.inputStreamToByteArray(stream) : null;
        }
        catch (Exception ex) {
            throw new ContentNotFoundException(contentPath);
        }
    }
}
