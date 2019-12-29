package com.geekhome.coremodule.httpserver;

public class Url {
    private String _urlNoQuery;
    private String _originalString;
    private QueryString _queryString;

    public QueryString getQueryString() {
        return _queryString;
    }

    public String getOriginalString() {
        return _originalString;
    }

    public String getUrlNoQuery() {
        return _urlNoQuery;
    }

    public Url(String originalString) {
        _urlNoQuery = extractOriginalString(originalString);
        if (originalString != null)
        {
            _originalString = originalString;
            _queryString = new QueryString(originalString);
        }
    }

    public String buildContentPath()
    {
        String pathPartsUntrimmed = getUrlNoQuery().toUpperCase();
        String[] pathParts = StringExt.trimStart(pathPartsUntrimmed, '/').split("/");
        String contentPath = "";
        if (pathParts.length > 0)
        {
            for (String pathPart : pathParts)
            {
                contentPath += (contentPath.equals("")) ? pathPart : "\\" + pathPart;
            }
        }

        return contentPath;
    }

    private static String extractOriginalString(String originalString)
    {
        if (originalString != null)
        {
            String result = originalString;
            int questionMarkIndex = originalString.indexOf('?');
            if (questionMarkIndex > 0)
            {
                result = originalString.substring(0, questionMarkIndex);
            }

            return result;
        }

        return "";
    }
}
