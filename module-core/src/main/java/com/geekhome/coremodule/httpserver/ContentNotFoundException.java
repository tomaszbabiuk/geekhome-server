package com.geekhome.coremodule.httpserver;

public class ContentNotFoundException extends Exception {
    public ContentNotFoundException(String contentPath) {
        super(createMessage(contentPath));
    }

    private static String createMessage(String contentPath) {
        return String.format("Content not found: %s", contentPath);
    }
}
