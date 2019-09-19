package com.geekhome.http.jetty;

import com.geekhome.http.ContentNotFoundException;
import com.geekhome.http.IUContentProvider;

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
