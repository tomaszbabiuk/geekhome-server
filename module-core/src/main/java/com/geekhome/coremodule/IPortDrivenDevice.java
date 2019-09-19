package com.geekhome.coremodule;

import com.geekhome.common.INamedObject;

public interface IPortDrivenDevice extends INamedObject {
    String getPortId();
}
