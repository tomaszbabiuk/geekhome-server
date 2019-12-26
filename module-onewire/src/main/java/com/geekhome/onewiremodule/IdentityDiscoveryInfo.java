package com.geekhome.onewiremodule;

import com.dalsemi.onewire.container.OneWireContainer01;
import com.geekhome.common.settings.AutomationSettings;

class IdentityDiscoveryInfo extends DiscoveryInfo<IdentityContainerWrapper> {
    IdentityDiscoveryInfo(OneWireContainer01 container, AutomationSettings automationSettings) {
        super(new IdentityContainerWrapper(container, automationSettings));
    }
}
