package com.geekhome.emailmodule.httpserver;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.NamedObject;
import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.emailmodule.EmailModule;
import com.geekhome.coremodule.httpserver.IHttpListenerRequest;
import com.geekhome.coremodule.httpserver.IResponse;
import com.geekhome.coremodule.jetty.JsonRequestsDispatcherBase;
import com.geekhome.coremodule.httpserver.JsonResponse;
import com.geekhome.common.configuration.CollectorCollection;
import org.json.simple.JSONObject;

import java.util.Collection;

public class EmailJsonRequestsDispatcher extends JsonRequestsDispatcherBase {
    private AutomationSettings _automationSettings;

    public EmailJsonRequestsDispatcher(AutomationSettings automationSettings) {
        _automationSettings = automationSettings;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {
        String originalStringUppercased = request.getUrl().getUrlNoQuery().toUpperCase();
        if (originalStringUppercased.equals("/CONFIG/TRUSTEDRECIPIENTS.JSON")) {
            CollectorCollection<NamedObject> result = new CollectorCollection<>();
            Collection<String> trustedRecipients = _automationSettings.getTableKeys(EmailModule.TABLE_TRUSTED_RECIPIENTS);
            for (String email : trustedRecipients) {
                String name = _automationSettings.readTable(EmailModule.TABLE_TRUSTED_RECIPIENTS, email);
                DescriptiveName recipientName = new DescriptiveName(name, email);
                result.add(new NamedObject(recipientName));
            }

            JSONObject json = JsonResponse.createJSONResult(result);
            return new JsonResponse(json, false);
        }

        return null;
    }
}
