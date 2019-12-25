package com.geekhome.automationmodule;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.DeviceCategory;
import com.geekhome.coremodule.OnOffDeviceBase;
import com.geekhome.coremodule.YesNo;
import com.geekhome.http.ILocalizationProvider;

public class OnOffDevice extends OnOffDeviceBase {
    public OnOffDevice(DescriptiveName name, String iconName, String portId, String roomId, YesNo triggeredManually, DeviceCategory deviceCategory) {
        super(name, portId, roomId, triggeredManually, iconName, deviceCategory);
    }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("AUTO:OnOffDevices");
    }
}
