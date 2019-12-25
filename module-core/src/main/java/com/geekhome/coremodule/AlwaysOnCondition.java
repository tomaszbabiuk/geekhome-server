package com.geekhome.coremodule;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.http.ILocalizationProvider;

public class AlwaysOnCondition extends ConditionBase {
    public AlwaysOnCondition(ILocalizationProvider localizationProvider) {
        super(new DescriptiveName(localizationProvider.getValue("C:AlwaysOn"), "alwayson"));
    }
}