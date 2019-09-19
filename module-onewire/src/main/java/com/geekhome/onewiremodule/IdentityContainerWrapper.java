package com.geekhome.onewiremodule;

import com.dalsemi.onewire.container.OneWireContainer01;
import com.geekhome.coremodule.settings.AutomationSettings;

class IdentityContainerWrapper extends ContainerWrapperBase {


    IdentityContainerWrapper(OneWireContainer01 container, AutomationSettings automationSettings) {
        super(container);
    }


    public void tryExecute() {
        //        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        md.update(new byte[] { (byte)234, 34, 54});
//        md.digest();
//        return new String(md.digest());
    }
}
