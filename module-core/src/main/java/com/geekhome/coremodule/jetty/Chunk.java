package com.geekhome.coremodule.jetty;

import java.io.UnsupportedEncodingException;

public class Chunk {
    private byte[] _content;
    private ChunkType _type;
    private String _contentAsString;

    public byte[] getContent() {
        return _content;
    }

    public void setContent(byte[] value) {
        _content = value;
    }

    public ChunkType getType() {
        return _type;
    }

    public void setType(ChunkType value) {
        _type = value;
    }

    public String getContentAsString() {
        try {
            return new String(getContent(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "<encoding not supported!>";
        }
    }


    public Chunk(byte[] content, ChunkType type)
    {
        setContent(content);
        setType(type);
    }

}
