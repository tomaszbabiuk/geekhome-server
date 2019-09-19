package com.geekhome.http.jetty;

public enum ChunkType {
    HtmlUnresolved,
    ParametersExtracted,
    HtmlResolved,
    FileInclusion,
    ParameterInclusion
}
