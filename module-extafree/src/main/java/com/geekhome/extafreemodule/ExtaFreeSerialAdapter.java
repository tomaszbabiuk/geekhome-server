package com.geekhome.extafreemodule;

import com.geekhome.common.DescriptiveName;
import com.geekhome.common.DiscoveryException;
import com.geekhome.common.RefreshState;
import com.geekhome.common.SerialAdapterBase;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.hardwaremanager.InputPortsCollection;
import com.geekhome.hardwaremanager.OutputPortsCollection;
import com.geekhome.hardwaremanager.TogglePortsCollection;
import com.geekhome.http.ILocalizationProvider;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.OutputStream;

class ExtaFreeSerialAdapter extends SerialAdapterBase {

    private ILogger _logger = LoggingService.getLogger();
    private SerialPort _serialPort;
    private OutputStream _serialOutputStream;

    ExtaFreeSerialAdapter(String port, ILocalizationProvider localizationProvider) {
        super(new DescriptiveName("Exta free adapter " + port, "extafree" + port), port, localizationProvider);
    }

    @Override
    public void discover(InputPortsCollection<Boolean> digitalInputPorts, OutputPortsCollection<Boolean> digitalOutputPorts,
                         InputPortsCollection<Integer> powerInputPorts, OutputPortsCollection<Integer> powerOutputPorts,
                         InputPortsCollection<Double> temperaturePorts, TogglePortsCollection togglePorts,
                         InputPortsCollection<Double> humidityPorts, InputPortsCollection<Double> luminosityPorts) throws DiscoveryException {
        try {
            openPort(getSerialPortName());

            _logger.info(String.format("Successfully connected to USB->RS485 adapter on port %s. Adding  Exta free ports to Hardware Manager", getSerialPortName()));

            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x8C, (byte) 0x3A}, new byte[]{(byte) 0xCD, (byte) 0xCA}, (byte) 0);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xDD, (byte) 0xFA}, new byte[]{(byte) 0x9C, (byte) 0x0A}, (byte) 1);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x2D, (byte) 0xFA}, new byte[]{(byte) 0x6C, (byte) 0x0A}, (byte) 2);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x7C, (byte) 0x3A}, new byte[]{(byte) 0x3D, (byte) 0xCA}, (byte) 3);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xCD, (byte) 0xFB}, new byte[]{(byte) 0x8C, (byte) 0x0B}, (byte) 4);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x9C, (byte) 0x3B}, new byte[]{(byte) 0xDD, (byte) 0xCB}, (byte) 5);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x6C, (byte) 0x3B}, new byte[]{(byte) 0x2D, (byte) 0xCB}, (byte) 6);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x3D, (byte) 0xFB}, new byte[]{(byte) 0x7C, (byte) 0x0B}, (byte) 7);

            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x0D, (byte) 0xF8}, new byte[]{(byte) 0x4C, (byte) 0x08}, (byte) 8);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x5C, (byte) 0x38}, new byte[]{(byte) 0x1D, (byte) 0xC8}, (byte) 9);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xAC, (byte) 0x38}, new byte[]{(byte) 0xED, (byte) 0xC8}, (byte) 10);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xFD, (byte) 0xF8}, new byte[]{(byte) 0xBC, (byte) 0x08}, (byte) 11);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x4C, (byte) 0x39}, new byte[]{(byte) 0x0D, (byte) 0xC9}, (byte) 12);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x1D, (byte) 0xF9}, new byte[]{(byte) 0x5C, (byte) 0x09}, (byte) 13);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xED, (byte) 0xF9}, new byte[]{(byte) 0xAC, (byte) 0x09}, (byte) 14);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xBC, (byte) 0x39}, new byte[]{(byte) 0xFD, (byte) 0xC9}, (byte) 15);

            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x8D, (byte) 0xFF}, new byte[]{(byte) 0xCC, (byte) 0x0F}, (byte) 16);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xDC, (byte) 0x3F}, new byte[]{(byte) 0x9D, (byte) 0xCF}, (byte) 17);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x2C, (byte) 0x3F}, new byte[]{(byte) 0x6D, (byte) 0xCF}, (byte) 18);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x7D, (byte) 0xFF}, new byte[]{(byte) 0x3C, (byte) 0x0F}, (byte) 19);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xCC, (byte) 0x3E}, new byte[]{(byte) 0x8D, (byte) 0xCE}, (byte) 20);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x9D, (byte) 0xFE}, new byte[]{(byte) 0xDC, (byte) 0x0E}, (byte) 21);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x6D, (byte) 0xFE}, new byte[]{(byte) 0x2C, (byte) 0x0E}, (byte) 22);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x3C, (byte) 0x3E}, new byte[]{(byte) 0x7D, (byte) 0xCE}, (byte) 23);

            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x0C, (byte) 0x3D}, new byte[]{(byte) 0x4D, (byte) 0xCD}, (byte) 24);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x5D, (byte) 0xFD}, new byte[]{(byte) 0x1C, (byte) 0x0D}, (byte) 25);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xAD, (byte) 0xFD}, new byte[]{(byte) 0xEC, (byte) 0x0D}, (byte) 26);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xFC, (byte) 0x3D}, new byte[]{(byte) 0xBD, (byte) 0xCD}, (byte) 27);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x4D, (byte) 0xFC}, new byte[]{(byte) 0x0C, (byte) 0x0C}, (byte) 28);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x1C, (byte) 0x3C}, new byte[]{(byte) 0x5D, (byte) 0xCC}, (byte) 29);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xEC, (byte) 0x3C}, new byte[]{(byte) 0xAD, (byte) 0xCC}, (byte) 30);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xBD, (byte) 0xFC}, new byte[]{(byte) 0xFC, (byte) 0x0C}, (byte) 31);

            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x8D, (byte) 0xF0}, new byte[]{(byte) 0xCC, (byte) 0x00}, (byte) 32);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xDC, (byte) 0x30}, new byte[]{(byte) 0x9D, (byte) 0xC0}, (byte) 33);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x3C, (byte) 0x30}, new byte[]{(byte) 0x6D, (byte) 0xC0}, (byte) 34);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x7D, (byte) 0xF0}, new byte[]{(byte) 0x3C, (byte) 0x00}, (byte) 35);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xCC, (byte) 0x31}, new byte[]{(byte) 0x8D, (byte) 0xC1}, (byte) 36);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x9D, (byte) 0xF1}, new byte[]{(byte) 0xDC, (byte) 0x01}, (byte) 37);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x6D, (byte) 0xF1}, new byte[]{(byte) 0x2C, (byte) 0x01}, (byte) 38);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x3C, (byte) 0x31}, new byte[]{(byte) 0x7D, (byte) 0xC1}, (byte) 39);

            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x0C, (byte) 0x32}, new byte[]{(byte) 0x4D, (byte) 0xC2}, (byte) 40);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x5D, (byte) 0xF2}, new byte[]{(byte) 0x1C, (byte) 0x02}, (byte) 41);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xAD, (byte) 0xF2}, new byte[]{(byte) 0xEC, (byte) 0x02}, (byte) 42);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xFC, (byte) 0x32}, new byte[]{(byte) 0xBD, (byte) 0xC2}, (byte) 43);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x4D, (byte) 0xF3}, new byte[]{(byte) 0x0C, (byte) 0x03}, (byte) 44);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x1C, (byte) 0x33}, new byte[]{(byte) 0x5D, (byte) 0xC3}, (byte) 45);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xEC, (byte) 0x33}, new byte[]{(byte) 0xAD, (byte) 0xC3}, (byte) 46);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xBD, (byte) 0xF3}, new byte[]{(byte) 0xFC, (byte) 0x03}, (byte) 47);

            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x8C, (byte) 0x35}, new byte[]{(byte) 0xCD, (byte) 0xC5}, (byte) 48);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xDD, (byte) 0xF5}, new byte[]{(byte) 0x9C, (byte) 0x05}, (byte) 49);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x2D, (byte) 0xF5}, new byte[]{(byte) 0x6C, (byte) 0x05}, (byte) 50);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x7C, (byte) 0x35}, new byte[]{(byte) 0x3D, (byte) 0xC5}, (byte) 51);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xCD, (byte) 0xF4}, new byte[]{(byte) 0x8C, (byte) 0x04}, (byte) 52);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x9C, (byte) 0x34}, new byte[]{(byte) 0xDD, (byte) 0xC4}, (byte) 53);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x6C, (byte) 0x34}, new byte[]{(byte) 0x2D, (byte) 0xC4}, (byte) 54);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x3D, (byte) 0xF4}, new byte[]{(byte) 0x7C, (byte) 0x04}, (byte) 55);

            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x0D, (byte) 0xF7}, new byte[]{(byte) 0x4C, (byte) 0x07}, (byte) 56);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x5C, (byte) 0x37}, new byte[]{(byte) 0x1D, (byte) 0xC7}, (byte) 57);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xAC, (byte) 0x37}, new byte[]{(byte) 0xED, (byte) 0xC7}, (byte) 58);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xFD, (byte) 0xF7}, new byte[]{(byte) 0xBC, (byte) 0x07}, (byte) 59);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x4C, (byte) 0x36}, new byte[]{(byte) 0x0D, (byte) 0xC6}, (byte) 60);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x1D, (byte) 0xF6}, new byte[]{(byte) 0x5C, (byte) 0x06}, (byte) 61);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xED, (byte) 0xF6}, new byte[]{(byte) 0xAC, (byte) 0x06}, (byte) 62);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xBC, (byte) 0x36}, new byte[]{(byte) 0xFD, (byte) 0xC6}, (byte) 63);

            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x8D, (byte) 0xEE}, new byte[]{(byte) 0xCC, (byte) 0x1E}, (byte) 64);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xDC, (byte) 0x2E}, new byte[]{(byte) 0x9D, (byte) 0xDE}, (byte) 65);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x2C, (byte) 0x2E}, new byte[]{(byte) 0x6D, (byte) 0xDE}, (byte) 66);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x7D, (byte) 0xEE}, new byte[]{(byte) 0x3C, (byte) 0x1E}, (byte) 67);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xCC, (byte) 0x2F}, new byte[]{(byte) 0x8D, (byte) 0xDF}, (byte) 68);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x9D, (byte) 0xEF}, new byte[]{(byte) 0xDC, (byte) 0x1F}, (byte) 69);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x6D, (byte) 0xEF}, new byte[]{(byte) 0x2C, (byte) 0x1F}, (byte) 70);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x3C, (byte) 0x2F}, new byte[]{(byte) 0x7D, (byte) 0xDF}, (byte) 71);

            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x0C, (byte) 0x2C}, new byte[]{(byte) 0x4D, (byte) 0xDC}, (byte) 72);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x5D, (byte) 0xEC}, new byte[]{(byte) 0x1C, (byte) 0x1C}, (byte) 73);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xAD, (byte) 0xEC}, new byte[]{(byte) 0xEC, (byte) 0x1C}, (byte) 74);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xFC, (byte) 0x2C}, new byte[]{(byte) 0xBC, (byte) 0xDC}, (byte) 75);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x4D, (byte) 0xED}, new byte[]{(byte) 0x0C, (byte) 0x1D}, (byte) 76);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x1C, (byte) 0x2D}, new byte[]{(byte) 0x5D, (byte) 0xDD}, (byte) 77);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xEC, (byte) 0x2D}, new byte[]{(byte) 0xAD, (byte) 0xDD}, (byte) 78);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xBD, (byte) 0xED}, new byte[]{(byte) 0xFC, (byte) 0x1D}, (byte) 79);

            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x8C, (byte) 0x2B}, new byte[]{(byte) 0xCD, (byte) 0xDB}, (byte) 80);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xDD, (byte) 0xEB}, new byte[]{(byte) 0x9C, (byte) 0x1B}, (byte) 81);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x2D, (byte) 0xEB}, new byte[]{(byte) 0x6C, (byte) 0x1B}, (byte) 82);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x7C, (byte) 0x2B}, new byte[]{(byte) 0x3D, (byte) 0xDB}, (byte) 83);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xCD, (byte) 0xEA}, new byte[]{(byte) 0x8C, (byte) 0x1A}, (byte) 84);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x9C, (byte) 0x2A}, new byte[]{(byte) 0xDD, (byte) 0xDA}, (byte) 85);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x6C, (byte) 0x2A}, new byte[]{(byte) 0x2D, (byte) 0xDA}, (byte) 86);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x3D, (byte) 0xEA}, new byte[]{(byte) 0x7C, (byte) 0x1A}, (byte) 87);

            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x0D, (byte) 0xE9}, new byte[]{(byte) 0x4C, (byte) 0x19}, (byte) 88);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x5C, (byte) 0x29}, new byte[]{(byte) 0x1D, (byte) 0xD9}, (byte) 89);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xAC, (byte) 0x29}, new byte[]{(byte) 0xED, (byte) 0xD9}, (byte) 90);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xFD, (byte) 0xE9}, new byte[]{(byte) 0xBC, (byte) 0x19}, (byte) 91);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x4C, (byte) 0x28}, new byte[]{(byte) 0x0D, (byte) 0xD8}, (byte) 92);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x1D, (byte) 0xE8}, new byte[]{(byte) 0x5C, (byte) 0x18}, (byte) 93);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xED, (byte) 0xE8}, new byte[]{(byte) 0xAC, (byte) 0x18}, (byte) 94);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xBC, (byte) 0x28}, new byte[]{(byte) 0xFD, (byte) 0xD8}, (byte) 95);

            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x8C, (byte) 0x24}, new byte[]{(byte) 0xCD, (byte) 0xD4}, (byte) 96);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xDD, (byte) 0xE4}, new byte[]{(byte) 0x9C, (byte) 0x14}, (byte) 97);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x2D, (byte) 0xE4}, new byte[]{(byte) 0x6C, (byte) 0x14}, (byte) 98);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x7C, (byte) 0x24}, new byte[]{(byte) 0x3D, (byte) 0xD4}, (byte) 99);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xCD, (byte) 0xE5}, new byte[]{(byte) 0x8C, (byte) 0x15}, (byte) 100);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x9C, (byte) 0x25}, new byte[]{(byte) 0xDD, (byte) 0xD5}, (byte) 101);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x6C, (byte) 0x25}, new byte[]{(byte) 0x2D, (byte) 0xD5}, (byte) 102);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x3D, (byte) 0xE5}, new byte[]{(byte) 0x7C, (byte) 0x15}, (byte) 103);

            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x0D, (byte) 0xE6}, new byte[]{(byte) 0x4C, (byte) 0x16}, (byte) 104);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x5C, (byte) 0x26}, new byte[]{(byte) 0x1D, (byte) 0xD6}, (byte) 105);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xAC, (byte) 0x26}, new byte[]{(byte) 0xED, (byte) 0xD6}, (byte) 106);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xFD, (byte) 0xE6}, new byte[]{(byte) 0xBC, (byte) 0x16}, (byte) 107);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x4C, (byte) 0x27}, new byte[]{(byte) 0x0D, (byte) 0xD7}, (byte) 108);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x1D, (byte) 0xE7}, new byte[]{(byte) 0x5C, (byte) 0x17}, (byte) 109);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xED, (byte) 0xE7}, new byte[]{(byte) 0xAC, (byte) 0x17}, (byte) 110);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xBC, (byte) 0x27}, new byte[]{(byte) 0xFD, (byte) 0xD7}, (byte) 111);

            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x8D, (byte) 0xE1}, new byte[]{(byte) 0xCC, (byte) 0x11}, (byte) 112);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xDC, (byte) 0x21}, new byte[]{(byte) 0x9D, (byte) 0xD1}, (byte) 113);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x2C, (byte) 0x21}, new byte[]{(byte) 0x6D, (byte) 0xD1}, (byte) 114);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x7D, (byte) 0xE1}, new byte[]{(byte) 0x3C, (byte) 0x11}, (byte) 115);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xCC, (byte) 0x20}, new byte[]{(byte) 0x8D, (byte) 0xD0}, (byte) 116);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x9D, (byte) 0xE0}, new byte[]{(byte) 0xDC, (byte) 0x10}, (byte) 117);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x6D, (byte) 0xE0}, new byte[]{(byte) 0x2C, (byte) 0x10}, (byte) 118);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x3C, (byte) 0x20}, new byte[]{(byte) 0x7D, (byte) 0xD0}, (byte) 119);

            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x0C, (byte) 0x23}, new byte[]{(byte) 0x4D, (byte) 0xD3}, (byte) 120);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x5D, (byte) 0xE3}, new byte[]{(byte) 0x1C, (byte) 0x13}, (byte) 121);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xAD, (byte) 0xE3}, new byte[]{(byte) 0xEC, (byte) 0x13}, (byte) 122);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xFC, (byte) 0x23}, new byte[]{(byte) 0xBD, (byte) 0xD3}, (byte) 123);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x4D, (byte) 0xE2}, new byte[]{(byte) 0x0C, (byte) 0x12}, (byte) 124);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0x1C, (byte) 0x22}, new byte[]{(byte) 0x5D, (byte) 0xD2}, (byte) 125);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xEC, (byte) 0x22}, new byte[]{(byte) 0xAD, (byte) 0xD2}, (byte) 126);
            addExtaFreeOutputPort(togglePorts, new byte[]{(byte) 0xBD, (byte) 0xE2}, new byte[]{(byte) 0xFC, (byte) 0x12}, (byte) 127);
        } catch (Exception ex) {
            markAsNonOperational(ex);
            throw new DiscoveryException("Cannot add exta free output port", ex);
        }
    }

    @Override
    public void stop() {
        if (_serialPort != null) {
            try {
                _serialPort.close();
            } catch (Exception ex) {
                _logger.error("Cannot close serial port: " + getSerialPortName(), ex);
                markAsNonOperational(ex);
            }
        }
    }

    @Override
    public void refresh() {
    }

    @Override
    public RefreshState getRefreshState() {
        return RefreshState.NA;
    }

    @Override
    public void resetLatches() {
        //there's no way to read so no latches are used
    }

    private void addExtaFreeOutputPort(TogglePortsCollection togglePortsCollection, byte[] pushCrc, byte[] releaseCrc, byte channel) {
        Xrm01TogglePort port = new Xrm01TogglePort(_serialOutputStream, pushCrc, releaseCrc, channel);
        togglePortsCollection.add(port);
    }

    private boolean isPortOpen() {
        return _serialPort != null;
    }

    private void openPort(String comPortName) throws Exception {
        if (!isPortOpen()) {
            try {
                CommPortIdentifier portIdentifier;
                portIdentifier = CommPortIdentifier.getPortIdentifier(comPortName);

                if (portIdentifier.isCurrentlyOwned()) {
                    throw new IOException("Port already in use.");
                }

                _serialPort = (SerialPort) portIdentifier.open("Geekhome", 2000);

                _serialPort.notifyOnOutputEmpty(true);
                _serialPort.notifyOnDataAvailable(true);
                _serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
                _serialOutputStream = _serialPort.getOutputStream();

                // settings
                _serialPort.disableReceiveFraming();
                _serialPort.disableReceiveThreshold();
                _serialPort.enableReceiveTimeout(1);
                _serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_EVEN);
                _serialPort.setDTR(true);
                _serialPort.setRTS(true);
            } catch (Exception e) {
                if (_serialPort != null) {
                    _serialPort.close();
                }
                _serialPort = null;
                throw e;
            }
        }
    }

    public String toString() {
        return "Exta free adapter on " + getSerialPortName();
    }

    @Override
    public void start() {
    }
}