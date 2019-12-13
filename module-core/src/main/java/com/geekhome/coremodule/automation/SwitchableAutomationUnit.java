///*
//package com.geekhome.coremodule.automation;
//
//import com.geekhome.coremodule.MultistateDevice;
//import com.geekhome.hardwaremanager.IOutputPort;
//import com.geekhome.hardwaremanager.IPort;
//import com.geekhome.http.ILocalizationProvider;
//
//import java.util.Calendar;
//
//public class SwitchableAutomationUnit<D extends MultistateDevice> extends MultistateDeviceAutomationUnit<D> {
//    private IOutputPort<Boolean> _outputPort;
//
//    protected IOutputPort<Boolean> getOutputPort() {
//        return _outputPort;
//    }
//
//    public SwitchableAutomationUnit(IOutputPort<Boolean> outputPort, D device, ILocalizationProvider localizationProvider) throws Exception {
//        super(device, localizationProvider);
//        _outputPort = outputPort;
//        changeStateInternal("off", ControlMode.Auto);
//    }
//
//    @Override
//    public IPort[] getUsedPorts() {
//        return new IPort[] { _outputPort };
//    }
//
//    @Override
//    public void calculate(Calendar now) throws Exception {
//        if (getStateId().equals("on")) {
//            changeOutputPortStateIfNeeded(_outputPort, true);
//        } else if (getStateId().equals("off")) {
//            changeOutputPortStateIfNeeded(_outputPort, false);
//        }
//    }
//
//    @Override
//    protected void calculateInternal(Calendar now) throws Exception {
//
//    }
//
//    @Override
//    protected boolean isSignaled() {
//        return _outputPort.read();
//    }
//}*/
