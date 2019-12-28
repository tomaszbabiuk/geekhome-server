package com.geekhome.coremodule.jetty;

import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.coremodule.httpserver.*;
import org.eclipse.jetty.io.EofException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;

public class WebProcessor {
    private final String _defaultPage;

    public interface LoggingCallback {
        void info(String info);
        void error(String info, Throwable throwable);
    }

    private final ArrayList<IRequestsDispatcher> _dispatchers;
    private final IResponseCache _cache;
    private final ILocalizationProvider _localizationProvider;
    private final IUContentProvider _contentProvidersSearcher;
    private final LoggingCallback _loggingCallback;

    public WebProcessor(ArrayList<IRequestsDispatcher> dispatchers, IResponseCache cache, ILocalizationProvider localizationProvider,
                        IUContentProvider contentProvider, LoggingCallback loggingCallback, String defaultPage) {
        _dispatchers = dispatchers;
        _cache = cache;
        _localizationProvider = localizationProvider;
        _contentProvidersSearcher = contentProvider;
        _defaultPage = defaultPage;
        _loggingCallback = loggingCallback;
    }

    public void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        IHttpListenerRequest requestWrapped = new HttpServletRequestWrapper(request);
        IHttpListenerResponse responseWrapped = new HttpServletResponseWrapper(response);

        _loggingCallback.info("Incoming request: " + requestWrapped.getUrl().getOriginalString());
        try {
            IResponse type = findDispatcher(requestWrapped);
            if (_cache != null && type.isCacheable()) {
                String cacheKey = requestWrapped.getUrl().getOriginalString().toUpperCase();
                int randomParameterPos = cacheKey.indexOf("?_");
                if (randomParameterPos > 0) {
                    cacheKey = cacheKey.substring(0, randomParameterPos);
                }
                IResponse responseCached = _cache.get(cacheKey);
                if (responseCached != null) {
                    _loggingCallback.info("Processing request from cache");
                    responseCached.process(responseWrapped);
                } else {
                    ResponseGrabber responseGrabber = new ResponseGrabber();
                    type.process(responseGrabber);
                    IResponse responseGrabbed = responseGrabber.createCachedRequest();
                    responseGrabbed.process(responseWrapped);
                    if (responseWrapped.getStatusCode() == 200) {
                        _cache.put(cacheKey, responseGrabbed);
                    }
                }
            } else {
                type.process(responseWrapped);
            }
        } catch (EofException eof) {
            _loggingCallback.info("EofException from Jetty. Broken pipe.");
        } catch (UndeclaredThrowableException uex) {
            handleException(requestWrapped, responseWrapped, uex.getCause());
        } catch (Exception ex) {
            handleException(requestWrapped, responseWrapped, ex);
        }
    }

    private void handleException(IHttpListenerRequest requestWrapped, IHttpListenerResponse responseWrapped, Throwable ex) {
        _loggingCallback.error("Unhandled exception", ex);
        try {
            new ErrorRequestsDispatcher(500, _localizationProvider, _contentProvidersSearcher).dispatch(requestWrapped).process(responseWrapped);
        } catch (EofException eof) {
            //request cancelled in browser - ignoring
        } catch (Exception exx) {
            _loggingCallback.error("Error page not generated!", exx);
        }
    }

    private IResponse findDispatcher(IHttpListenerRequest request) throws Exception {
        IResponse redirectionResponse = doRedirection(request);
        if (redirectionResponse != null) {
            return redirectionResponse;
        }

        IResponse contentResponse = doDispatching(request);
        if (contentResponse != null) {
            return contentResponse;
        }

        throw new Exception("Request not supported! Cannot find a proper dispatcher to process this request: " + request.getUrl().getOriginalString());
    }

    private IResponse doDispatching(IHttpListenerRequest request) throws Exception {
        for (IRequestsDispatcher requestsDispatcher : _dispatchers) {
            if (requestsDispatcher.isRequestSupported(request)) {
                IResponse result = requestsDispatcher.dispatch(request);
                if (result != null) {
                    return result;
                }
            }
        }

        _loggingCallback.info("Cannot find dispatcher for this type of request!");
        return null;
    }

    private IResponse doRedirection(IHttpListenerRequest request) {
        String originalStringUppercased = request.getUrl().getOriginalString().toUpperCase();

        if (originalStringUppercased.equals("/")) {
            return new RedirectionResponse(_defaultPage);
        }

        return null;
    }
}