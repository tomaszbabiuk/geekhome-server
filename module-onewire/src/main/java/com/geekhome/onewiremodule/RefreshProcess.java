package com.geekhome.onewiremodule;

import com.dalsemi.onewire.OneWireException;
import com.dalsemi.onewire.adapter.DSPortAdapter;
import com.dalsemi.onewire.adapter.USerialAdapter;
import com.geekhome.common.RefreshException;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;

import java.util.ArrayList;
import java.util.List;

class RefreshProcess implements Runnable {
    interface ITemperatureRefreshedListener {
        void execute(DiscoveryInfo discoveryInfo, double value);
    }

    interface IInputSwitchRefreshedListener {
        void execute(DiscoveryInfo discoveryInfo, int channel, boolean level, boolean isSensed);
    }

    private ThreadsSynchronizer _synchronizer;
    private ILogger _logger = LoggingService.getLogger();
    private String _serialPortName;
    private final List<AdapterTask> _pipe;
    private IAdapterRefreshedListener _adapterRefreshedListener;
    private USerialAdapter _adapter;
    private ITemperatureRefreshedListener _temperatureRefreshedListener;
    private IInputSwitchRefreshedListener _inputSwitchRefreshedListener;

    void setSynchronizer(ThreadsSynchronizer synchronizer) {
        _synchronizer = synchronizer;
    }

    void setTemperatureRefreshedListener(ITemperatureRefreshedListener listener) {
        _temperatureRefreshedListener = listener;
    }

    void setInputSwitchRefreshedListener(IInputSwitchRefreshedListener listener) {
        _inputSwitchRefreshedListener = listener;
    }

    private void onTemperatureRefreshed(DiscoveryInfo discoveryInfo, double temperature) {
        if (_temperatureRefreshedListener != null) {
            _temperatureRefreshedListener.execute(discoveryInfo, temperature);
        }
    }

    private void onInputSwitchRefreshed(DiscoveryInfo discoveryInfo, int channel, boolean level, boolean isSensed) {
        if (_inputSwitchRefreshedListener != null) {
            _inputSwitchRefreshedListener.execute(discoveryInfo, channel, level, isSensed);
        }
    }

    RefreshProcess(String serialPortName, List<AdapterTask> pipe, IAdapterRefreshedListener adapterRefreshedListener) {
        _serialPortName = serialPortName;
        _pipe = pipe;
        _adapterRefreshedListener = adapterRefreshedListener;
    }

    private void free() {
        try {
            if (_adapter != null) {
                _adapter.freePort();
            }
        } catch (Exception ex) {
            _logger.error(String.format("Cannot release 1-wire adapter on port: %s", _serialPortName), ex);
        }
    }

    private USerialAdapter initializeAdapter(String port) throws OneWireException {
        USerialAdapter adapter = new USerialAdapter();
        adapter.selectPort(port);
        adapter.reset();
        return adapter;
    }

    @Override
    public void run() {
        try {
            _adapter = initializeAdapter(_serialPortName);
            _adapter.beginExclusive(true);
            _adapter.setSearchAllDevices();
            _adapter.targetAllFamilies();
            _adapter.setSpeed(DSPortAdapter.SPEED_OVERDRIVE);
            boolean hasRefreshErrors = false;

            while (_pipe.size() > 0) {
                AdapterTask task;
                task = _pipe.remove(0);
                if (task.getType() == TaskType.RefreshTemperature) {
                    TemperatureContainerWrapper tc = (TemperatureContainerWrapper) (task.getDiscoveryInfo().getContainer());
                    Double temp = tc.tryReadTemperature();
                    if (temp != null) {
                        onTemperatureRefreshed(task.getDiscoveryInfo(), temp);
                    }
                    _pipe.add(task);
                } else if (task.getType() == TaskType.ReadSensedSwitchValue) {
                    SwitchContainerWrapper switchContainer = (SwitchContainerWrapper)(task.getDiscoveryInfo().getContainer());
                    ArrayList<SwitchContainerWrapper.SwitchContainerReading> readings = switchContainer.tryRead(true);
                    hasRefreshErrors = analyseSwitchReadings(hasRefreshErrors, task, readings);
                } else if (task.getType() == TaskType.ReadSwitchValue) {
                    SwitchContainerWrapper switchContainer = (SwitchContainerWrapper)(task.getDiscoveryInfo().getContainer());
                    ArrayList<SwitchContainerWrapper.SwitchContainerReading> readings = switchContainer.tryRead(false);
                    hasRefreshErrors = analyseSwitchReadings(hasRefreshErrors, task, readings);
                } else if (task.getType() == TaskType.WriteSwitchValue) {
                    SwitchContainerWrapper switchContainer = (SwitchContainerWrapper)(task.getDiscoveryInfo().getContainer());
                    switchContainer.tryExecute();
                } else if (task.getType() == TaskType.ValidateIdentity) {
                    IdentityContainerWrapper identityContainer = (IdentityContainerWrapper)(task.getDiscoveryInfo().getContainer());
                    identityContainer.tryExecute();
                } else if (task.getType() == TaskType.RefreshLoopFinished) {
                        _adapterRefreshedListener.refreshed(hasRefreshErrors);
                        hasRefreshErrors = false;
                } else if (task.getType() == TaskType.Continue) {
                    _pipe.add(task);
                } else if (task.getType() == TaskType.Break) {
                    break;
                }
            }
            _adapter.endExclusive();
        } catch (Exception e) {
            _adapterRefreshedListener.refreshed(true);
            Exception ex = new RefreshException("Unhandled 1-wire protocol error", e);
            throwAdapterError(ex);
        }
        free();
        if (_synchronizer != null) {
            _synchronizer.doNotify();
        }
    }

    private boolean analyseSwitchReadings(boolean hasRefreshErrors, AdapterTask task, ArrayList<SwitchContainerWrapper.SwitchContainerReading> readings) {
        if (readings == null) {
            hasRefreshErrors = true;
        } else {
            for (SwitchContainerWrapper.SwitchContainerReading reading : readings) {
                onInputSwitchRefreshed(task.getDiscoveryInfo(), reading.getChannel(), reading.getLevel(), reading.isSensed());
            }
        }
        return hasRefreshErrors;
    }

    private void throwAdapterError(Exception ex) {
        Thread t = Thread.currentThread();
        t.getUncaughtExceptionHandler().uncaughtException(t, ex);
    }
}