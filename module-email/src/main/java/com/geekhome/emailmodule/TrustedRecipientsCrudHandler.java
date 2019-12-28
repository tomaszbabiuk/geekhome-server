package com.geekhome.emailmodule;

import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.common.INameValueSet;
import com.geekhome.coremodule.httpserver.QueryString;
import com.geekhome.coremodule.httpserver.ResponseBase;
import com.geekhome.coremodule.httpserver.ICrudPostHandler;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.coremodule.jetty.RedirectionResponse;

class TrustedRecipientsCrudHandler implements ICrudPostHandler {
    private AutomationSettings _automationSettings;
    private ILocalizationProvider _localizationProvider;

    TrustedRecipientsCrudHandler(AutomationSettings automationSettings, ILocalizationProvider localizationProvider) {
        _automationSettings = automationSettings;
        _localizationProvider = localizationProvider;
    }

    @Override
    public String getKeyword() {
        return "TRUSTEDRECIPIENT";
    }

    @Override
    public void doAdd(QueryString queryString) throws Exception {
        doModify(queryString);
    }

    @Override
    public void doEdit(QueryString queryString) throws Exception {
        doModify(queryString);
    }

    @Override
    public void doRemove(QueryString queryString) throws Exception {
        String param = queryString.getValues().getValue("param");
        _automationSettings.clearSetting(EmailModule.TABLE_TRUSTED_RECIPIENTS, param);
    }

    @Override
    public void doModify(QueryString queryString) throws Exception {
        INameValueSet values = queryString.getValues();
        String name = values.getValue("name");
        String email = values.getValue("email");
        String uniqueId = values.getValue("uniqueid");
        if (!uniqueId.equals("")) {
            //modify
            _automationSettings.clearSetting(EmailModule.TABLE_TRUSTED_RECIPIENTS, uniqueId);
        }

        if (_automationSettings.getTableKeys(EmailModule.TABLE_TRUSTED_RECIPIENTS).contains(email)) {
            throw new Exception(_localizationProvider.getValue("E:EmailAlreadyExists"));
        }
        _automationSettings.changeSetting(EmailModule.TABLE_TRUSTED_RECIPIENTS, email, name);
    }

    @Override
    public ResponseBase returnRequest(QueryString queryString) throws Exception {
        return new RedirectionResponse("/config/email.htm");
    }
}