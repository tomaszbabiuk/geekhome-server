package com.geekhome.coremodule;

import com.geekhome.common.DeviceCategory;
import com.geekhome.common.ControlType;
import com.geekhome.common.INamedObject;
import com.geekhome.http.ILocalizationProvider;
import org.json.simple.JSONAware;

public interface IDevice extends INamedObject, JSONAware {
    String getIconName();
    ControlType getControlType();
    DeviceCategory getCategory();
    String getGroupName(ILocalizationProvider localizationProvider);
}
