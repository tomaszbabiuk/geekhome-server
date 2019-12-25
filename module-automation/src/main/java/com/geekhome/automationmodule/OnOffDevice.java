package com.geekhome.automationmodule;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.DeviceCategory;
import com.geekhome.common.configuration.OnOffDeviceBase;
import com.geekhome.common.configuration.YesNo;
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
