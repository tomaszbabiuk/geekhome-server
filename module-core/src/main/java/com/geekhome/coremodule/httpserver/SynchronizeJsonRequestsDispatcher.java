package com.geekhome.coremodule.httpserver;

import com.geekhome.common.commands.Synchronizer;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.INameValueSet;
import com.geekhome.http.IResponse;
import com.geekhome.http.ResponseBase;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.http.jetty.JsonRequestsDispatcherBase;
import com.geekhome.httpserver.modules.ObjectNotFoundException;
import com.geekhome.synchronizationmodule.business.*;
import com.google.gson.Gson;

import java.io.IOException;

public class SynchronizeJsonRequestsDispatcher extends JsonRequestsDispatcherBase {
    private Synchronizer _synchronizer;
    private ILocalizationProvider _localizationProvider;
    private ILogger _logger = LoggingService.getLogger();

    public SynchronizeJsonRequestsDispatcher(Synchronizer synchronizer, ILocalizationProvider localizationProvider) {
        _synchronizer = synchronizer;
        _localizationProvider = localizationProvider;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws ObjectNotFoundException, IOException {
        String originalStringUppercased = request.getUrl().getUrlNoQuery().toUpperCase();
        if (originalStringUppercased.equals("/SYNCHRONIZE.JSON")) {
            INameValueSet values = request.getUrl().getQueryString().getValues();

            String from = request.getUserName();
            SynchronizationCommandType type = SynchronizationCommandType.fromString(values.getValue("type"));
            String parameter = values.getValue("parameter");
            String value = values.getValue("value");
            String code = values.getValue("code");

            SynchronizationRequest syncRequest = new SynchronizationRequest(type, parameter, value, code);
            ReceivedSynchronizationRequest command = new ReceivedSynchronizationRequest(from, null, syncRequest);
            try {
                Object result = _synchronizer.processSynchronizationCommand(command);
                if (result != null) {
                    ResponseBase response = new ResponseBase(new Gson().toJson(result));
                    response.setContentType(command.getType().getContentType().toString());
                    return response;
                } else {
                    throw new Exception(_localizationProvider.getValue("C:CommandNotFound"));
                }
            }
            catch (Exception ex) {
                _logger.warning("Error processing HTTP command. Sending error to the client.", ex);
                SynchronizedError error = new SynchronizedError(ex);
                ResponseBase response = new ResponseBase(new Gson().toJson(error));
                response.setContentType(SynchronizationContentType.Error.toString());
                return response;
            }
        }

        return null;
    }
}