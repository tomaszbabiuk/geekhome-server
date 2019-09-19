package com.geekhome.hardwaremanager;

import com.geekhome.common.DescriptiveName;

import java.util.ArrayList;

public interface IPortMapper {
    ArrayList<DescriptiveName> buildPortMappings(String portId);
}
