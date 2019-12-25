package com.geekhome.coremodule;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.hardwaremanager.IPortMapper;
import com.geekhome.common.configuration.CollectorCollection;

import java.util.ArrayList;

public class CorePortMapper implements IPortMapper {
    private MasterConfiguration _masterConfiguration;

    public CorePortMapper(MasterConfiguration masterConfiguration) {
        _masterConfiguration = masterConfiguration;
    }

    public ArrayList<DescriptiveName> buildPortMappings(String portId) {
        ArrayList<DescriptiveName> result = new ArrayList<>();
        for (CollectorCollection collection : _masterConfiguration.getDevicesCollectors()) {
            for (Object current : collection.values()) {

                if (current instanceof IPortDrivenDevice) {
                    IPortDrivenDevice portDevice = (IPortDrivenDevice)current;
                    if (portDevice.getPortId().equals(portId)) {
                        result.add(portDevice.getName());
                    }
                }

                if (current instanceof IPortsDrivenDevice) {
                    IPortsDrivenDevice portsDevice = (IPortsDrivenDevice)current;
                    if (portsDevice.getPortsIds() != null && !portsDevice.getPortsIds().equals("")) {
                        for(String portDevicesId : portsDevice.getPortsIds().split(","))
                        {
                            if (portDevicesId.equals(portId)) {
                                result.add(portsDevice.getName());
                                break;
                            }
                        }
                    }
                }
            }
        }

        return result;
    }
}
