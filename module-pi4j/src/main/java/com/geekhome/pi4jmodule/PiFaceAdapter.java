package com.geekhome.pi4jmodule;

import com.geekhome.common.*;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.utils.Sleeper;
import com.geekhome.hardwaremanager.InputPortsCollection;
import com.geekhome.hardwaremanager.OutputPortsCollection;
import com.geekhome.hardwaremanager.TogglePortsCollection;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.OperationMode;
import com.pi4j.wiringpi.Spi;

import java.io.IOException;
import java.util.Calendar;
import java.util.Hashtable;

class PiFaceAdapter extends NamedObject implements IHardwareManagerAdapter {
    private ILogger _logger = LoggingService.getLogger();

    private static final byte REGISTER_IODIR_A = 0x00;
    private static final byte REGISTER_IODIR_B = 0x01;
    private static final byte REGISTER_GPINTEN_A = 0x04;
    private static final byte REGISTER_GPINTEN_B = 0x05;
    private static final byte REGISTER_DEFVAL_A = 0x06;
    private static final byte REGISTER_DEFVAL_B = 0x07;
    private static final byte REGISTER_INTCON_A = 0x08;
    private static final byte REGISTER_INTCON_B = 0x09;
    private static final byte REGISTER_IOCON_A = 0x0A;
    private static final byte REGISTER_IOCON_B = 0x0B;
    private static final byte REGISTER_GPPU_A = 0x0C;
    private static final byte REGISTER_GPPU_B = 0x0D;
    private static final byte REGISTER_GPIO_A = 0x12;
    private static final byte REGISTER_GPIO_B = 0x13;
    private static final byte READ_FLAG  = 0b00000001;
    private static final int SPI_SPEED = 1000000;

    private ILocalizationProvider _localizationProvider;
    private PiFaceAddress _address;
    private RefreshState _refreshState;
    private byte _latchesState;
    private Hashtable<Byte, SynchronizedInputPort<Boolean>> _inputs;
    private Hashtable<Byte, SynchronizedOutputPort<Boolean>> _outputs;
    private boolean _running;

    PiFaceAdapter(ILocalizationProvider localizationProvider, PiFaceAddress piFaceAddress, int spiChannel) throws Exception {
        super(new DescriptiveName("PiFace Adapter "+ piFaceAddress.getCode(), "piface" + piFaceAddress.getCode()));
        _localizationProvider = localizationProvider;
        _address = piFaceAddress;
        setupRegisters(spiChannel);
        createInputs();
        createOutputs();
        refresh(Calendar.getInstance());
    }

    private void setupRegisters(int spiChannel) throws IOException {
        // setup SPI for communication
        int fd = Spi.wiringPiSPISetup(spiChannel, SPI_SPEED);
        if (fd <= -1) {
            throw new IOException("SPI port setup failed.");
        }

        //enable addressing
        write(REGISTER_IOCON_A, (byte)0b00001000);
        write(REGISTER_IOCON_B, (byte)0b00001000);

        // set all default pins directions
        write(REGISTER_IODIR_A, (byte) 0x00);
        write(REGISTER_IODIR_B, (byte) 0xFF);

        // set all default pin interrupts
        write(REGISTER_GPINTEN_A, (byte) 0x00);
        write(REGISTER_GPINTEN_B, (byte) 0xFF);

        // set all default pin interrupt default values
        write(REGISTER_DEFVAL_A, (byte) 0x00);
        write(REGISTER_DEFVAL_B, (byte) 0x00);

        // set all default pin interrupt comparison behaviors
        write(REGISTER_INTCON_A, (byte) 0x00);
        write(REGISTER_INTCON_B, (byte) 0x00);

        // set all default pin states
        write(REGISTER_GPIO_A, (byte) 0x00);
        write(REGISTER_GPIO_B, (byte) 0xFF);

        // set all default pin pull up resistors
        write(REGISTER_GPPU_A, (byte) 0x00);
        write(REGISTER_GPPU_B, (byte) 0xFF);
    }

    private void write(byte register, byte data) {
        byte packet[] = new byte[3];
        packet[0] = (_address.getSpiAddress());   // _address byte
        packet[1] = register;    // register byte
        packet[2] = data;        // data byte

        Spi.wiringPiSPIDataRW(0, packet, 3);
    }

    private byte read(byte register){
        byte packet[] = new byte[3];
        packet[0] = (byte)(_address.getSpiAddress()|READ_FLAG);   // _address byte
        packet[1] = register;                    // register byte
        packet[2] = 0b00000000;                  // data byte

        int result = Spi.wiringPiSPIDataRW(0, packet, 3);
        if(result >= 0) {
            return packet[2];
        } else {
            throw new RuntimeException("Invalid SPI read operation: " + result);
        }
    }

    private void createInputs() {
        Hashtable<Byte, SynchronizedInputPort<Boolean>> inputs = new Hashtable<>();
        for (byte i = 0; i < 8; i++) {
            String portId = String.format("PIFACE-%s-IN%d", _address.getCode(), i);
            SynchronizedInputPort<Boolean> input = new SynchronizedInputPort<>(portId, false);
            inputs.put(i, input);
        }
        _inputs = inputs;
    }

    private void createOutputs() {
        Hashtable<Byte, SynchronizedOutputPort<Boolean>> outputs = new Hashtable<>();
        for (byte i=0; i<8; i++) {
            String portId = String.format("PIFACE-%s-OUT%d", _address.getCode(), i);
            SynchronizedOutputPort<Boolean> output = new SynchronizedOutputPort<>(portId, false);
            outputs.put(i, output);
        }
        _outputs = outputs;
    }

    @Override
    public void discover(InputPortsCollection<Boolean> digitalInputPorts, OutputPortsCollection<Boolean> digitalOutputPorts,
                         InputPortsCollection<Double> powerInputPorts, OutputPortsCollection<Integer> powerOutputPorts,
                         InputPortsCollection<Double> temperaturePorts, TogglePortsCollection togglePorts,
                         InputPortsCollection<Double> humidityPorts, InputPortsCollection<Double> luminosityPorts) throws DiscoveryException {

        for (SynchronizedInputPort<Boolean> input : _inputs.values()) {
            digitalInputPorts.add(input);
        }

        for (SynchronizedOutputPort<Boolean> output : _outputs.values()) {
            digitalOutputPorts.add(output);
        }
    }

    @Override
    public boolean isOperational() {
        return true;
    }

    @Override
    public void start() {
        _running = true;
        createRefreshThread().start();
    }

    @Override
    public String getStatus() {
        return _localizationProvider.getValue("C:NA");
    }

    @Override
    public void reconfigure(OperationMode operationMode) {
    }

    @Override
    public void stop() {
        _running = false;
    }

    @Override
    public void refresh(Calendar now) throws Exception {
        _refreshState = RefreshState.Refreshing;
        try {
            refreshInputs();
            refreshOutputs();
        } finally {
            _refreshState = RefreshState.RefreshSuccess;
        }
    }

    private void refreshInputs() {
        byte pinInputState = read(REGISTER_GPIO_B);
        for (byte i=0; i<8; i++) {
            SynchronizedInputPort<Boolean> inputPort = _inputs.get(i);
            boolean inputBitValue = isBitSet(pinInputState, i);
            boolean inputLatchValue = isBitSet(_latchesState, i);
            if (inputBitValue != inputLatchValue) {
                _logger.debug(String.format("PiFace latch changed! %s, value=%b, latch=%b", inputPort.getId(), inputBitValue, inputLatchValue));
                inputPort.setValue(!inputPort.getValue());
            } else {
                inputPort.setValue(!inputBitValue);
            }
        }
    }

    private void refreshOutputs() {
        byte pinOutputState = 0;
        for (byte i=0; i<8; i++) {
            SynchronizedOutputPort<Boolean> outputPort = _outputs.get(i);
            boolean bitValue = outputPort.read();
            if (bitValue) {
                pinOutputState = (byte)(pinOutputState | (1 << i));
            }
        }
        write(REGISTER_GPIO_A, pinOutputState);
    }

    @Override
    public RefreshState getRefreshState() {
        return _refreshState;
    }

    @Override
    public void resetLatches() {
        _latchesState = 0;
    }

    private static Boolean isBitSet(byte b, int bit) {
        return (b & (1 << bit)) != 0;
    }

    private Thread createRefreshThread() {

        return new Thread(new Runnable() {
            @Override
            public void run() {
                while (_running) {
                    if (_refreshState != RefreshState.Refreshing) {
                        byte pinInputState = read(REGISTER_GPIO_B);
                        _latchesState = (byte) (_latchesState | pinInputState);
                        Sleeper.trySleep(50);
                    }
                }
            }
        });
    }
}
