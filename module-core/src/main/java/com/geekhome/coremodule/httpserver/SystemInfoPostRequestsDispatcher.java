package com.geekhome.coremodule.httpserver;

import com.geekhome.common.OperationMode;
import com.geekhome.common.automation.SystemInfo;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.QueryString;
import com.geekhome.http.jetty.RedirectionResponse;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SystemInfoPostRequestsDispatcher extends PostRequestsDispatcherBase {
    private SystemInfo _systemInfo;

    public SystemInfoPostRequestsDispatcher(SystemInfo systemInfo) {
        _systemInfo = systemInfo;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {
        String urlUpperCased = request.getUrl().getUrlNoQuery().substring(1, request.getUrl().getUrlNoQuery().length() - 5).toUpperCase();
        QueryString queryString = new QueryString("?" + new String(request.getPostData(), "UTF-8"));

        if (urlUpperCased.equals("CHANGEOPERATIONMODE")) {
            String mode = queryString.getValues().getValue("mode");
            OperationMode modeParsed = OperationMode.fromInt(Integer.parseInt(mode));
            _systemInfo.setOperationMode(modeParsed);
            return new RedirectionResponse("/Dashboard.htm");
        }

        if (urlUpperCased.equals("SHUTDOWN")) {
            _systemInfo.shutdown();
            return new JsonResponse(JsonResponse.createJSONResult(true), false);
        }

        if (urlUpperCased.equals("REBOOT")) {
            _systemInfo.reboot();
            return new JsonResponse(JsonResponse.createJSONResult(true), false);
        }

        if (urlUpperCased.equals("DATETIME")) {
            String date = queryString.getValues().getValue("date");
            String[] dateSplitted = date.split("-");
            int year = Integer.parseInt(dateSplitted[0]);
            int month = Integer.parseInt(dateSplitted[1]);
            int day = Integer.parseInt(dateSplitted[2]);
            String time = queryString.getValues().getValue("time");
            String[] timeSplitted = time.split(":");
            int hour = Integer.parseInt(timeSplitted[0]);
            int minute = Integer.parseInt(timeSplitted[1]);
            Calendar newDate = new GregorianCalendar();
            newDate.set(year, month -1, day, hour, minute, 0);
            throw new Exception("Changing system date not supported on this platform");
        }

        return null;
    }
}

