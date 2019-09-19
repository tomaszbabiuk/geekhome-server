package com.geekhome.extafreemodule;

import com.geekhome.common.PortBase;
import com.geekhome.hardwaremanager.ITogglePort;

import java.io.IOException;
import java.io.OutputStream;

public class Xrm01TogglePort extends PortBase implements ITogglePort {
    private OutputStream _serialPort;
    private byte[] _pushCrc;
    private byte[] _releaseCrc;
    private byte _channel;

    public Xrm01TogglePort(OutputStream serialPort, byte[] pushCrc, byte[] releaseCrc, byte channel) {
        super("ExtaFree-" + addZeros(channel));
        _serialPort = serialPort;
        _pushCrc = pushCrc;
        _releaseCrc = releaseCrc;
        _channel = channel;
    }

    private static String addZeros(byte channel) {
        if (channel < 10) {
            return "00" + channel;
        }

        if (channel < 100) {
            return "0" + channel;
        }

        return String.valueOf(channel);
    }

    public void write(Boolean value) throws IOException {
        if (value) {
            byte[] frame = new byte[]{0x01, 0x05, 0x00, _channel, (byte) 0xFF, 0x00, _pushCrc[0], _pushCrc[1]};
            _serialPort.write(frame, 0, frame.length);
        } else {
            byte[] frame = new byte[]{0x01, 0x05, 0x00, _channel, 0x00, 0x00, _releaseCrc[0], _releaseCrc[1]};
            _serialPort.write(frame, 0, frame.length);
        }

        _serialPort.flush();
    }

    @Override
    public void toggleOn() throws Exception {
        write(true);
        Thread.sleep(150);
    }

    @Override
    public void toggleOff() throws Exception {
        write(false);
        Thread.sleep(150);
    }
}
