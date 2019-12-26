package com.geekhome.coremodule.httpserver;

import com.geekhome.http.IHttpListenerResponse;
import com.geekhome.http.ResponseBase;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Arrays;

public class JsonResponse extends ResponseBase {
    private String _content;

    public JsonResponse(JSONAware obj, boolean cacheable) {
        this(obj.toJSONString(), cacheable);
    }

    private JsonResponse(String content, boolean cacheable) {
        _content = content;
        setCacheable(cacheable);
    }

    @Override
    public void process(IHttpListenerResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatusCode(200);
        response.flush();
        try {
            response.writeToOutputStream(_content);
            response.flush();
        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                response.writeToOutputStream(ex.getMessage());
            }
            if (ex.getStackTrace() != null) {
                response.writeToOutputStream(Arrays.toString(ex.getStackTrace()));
            }
            response.setStatusCode(501);
        }

        response.flush();
    }

    public static JSONObject createJSONResult(JSONAware value) {
        JSONObject json = new JSONObject();
        json.put("Result", value);
        return json;
    }

    private static JSONObject createJSONResult(Object value) {
        JSONObject json = new JSONObject();
        json.put("Result", value);
        return json;
    }

    public static JSONObject createJSONResult(boolean value) {
        return createJSONResult((Object)value);
    }

    public static JSONObject createJSONResult(byte value) {
        return createJSONResult((Object)value);
    }

    public static JSONObject createJSONResult(double value) {
        return createJSONResult((Object)value);
    }

    public static JSONObject createJSONResult(String value) {
        return createJSONResult((Object)value);
    }

}

