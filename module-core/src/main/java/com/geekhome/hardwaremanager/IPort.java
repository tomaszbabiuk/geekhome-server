package com.geekhome.hardwaremanager;

import com.geekhome.common.DescriptiveName;

import java.util.ArrayList;

public interface IPort {
    String getId();
    ArrayList<DescriptiveName> getMappedTo();
    void initialize(IPortMapper portMapper);
}
