package com.geekhome.hardwaremanager;

import com.geekhome.common.PortBase;

public class ShadowTogglePort<T> extends PortBase implements IShadowTogglePort {

    private ITogglePort _target;
    private ITogglePort _base;

    public ShadowTogglePort(String id, ITogglePort base) {
        super(id);
        _base = base;
    }

    public void setTarget(ITogglePort target) {
        _target = target;
    }

    @Override
    public void toggleOn() throws Exception {
        if (_target != null) {
            _target.toggleOn();
        }

        _base.toggleOn();
    }

    @Override
    public void toggleOff() throws Exception {
        if (_target != null) {
            _target.toggleOff();
        }

        _base.toggleOff();
    }

    @Override
    public boolean hasTarget() {
        return _target != null;
    }
}