package com.geekhome.onewiremodule;

import com.dalsemi.onewire.OneWireException;
import com.dalsemi.onewire.container.SwitchContainer;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.hardwaremanager.IHardwareManager;
import com.geekhome.common.hardwaremanager.IInputPort;
import com.geekhome.common.hardwaremanager.IOutputPort;

public class SwitchDiscoveryInfo extends DiscoveryInfo<SwitchContainerWrapper> {
    enum Direction {
        Input("IN"),
        Output("OUT");

        private String _str;

        Direction(String str) {
            _str = str;
        }

        public String toString() {
            return _str;
        }
    }

    private int _channelsCount;

    @Persistable(name="ChannelsCount")
    public int getChannelsCount() {
        return _channelsCount;
    }


    public String extractPortId(int channel, Direction direction) {
        return String.format("%s-%s-%d", getAddress(), direction.toString(), channel);
    }

    public SwitchDiscoveryInfo(SwitchContainer container) throws OneWireException {
        super(new SwitchContainerWrapper(container));
        _channelsCount = getContainer().getChannelsCount();
    }

    public SwitchRole calculateSwitchRole(IHardwareManager hardwareManager) {
        boolean usedAsInput = false;
        boolean usedAsOutput = false;

        for (byte channel=0; channel<getChannelsCount(); channel++) {
            String inputPortId = extractPortId(channel, SwitchDiscoveryInfo.Direction.Input);
            String outputPortId = extractPortId(channel, SwitchDiscoveryInfo.Direction.Output);

            IInputPort<Boolean> inputPort = hardwareManager.tryFindDigitalInputPort(inputPortId);
            if (inputPort != null && inputPort.getMappedTo().size() > 0) {
                usedAsInput = true;
            }

            IOutputPort<Boolean> outputPort = hardwareManager.tryFindDigitalOutputPort(outputPortId);
            if (outputPort != null && outputPort.getMappedTo().size() > 0) {
                usedAsOutput = true;
            }

            if (usedAsOutput && usedAsInput) {
                break;
            }
        }

        if (usedAsInput && usedAsOutput) {
            return SwitchRole.BothConfliting;
        } else if (usedAsInput) {
            return SwitchRole.AlarmSensing;
        } else if (usedAsOutput) {
            return SwitchRole.RelayBoard;
        } else {
            return SwitchRole.Unknown;
        }
    }
}