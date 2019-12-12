package com.geekhome.hardwaremanager;

import com.geekhome.common.PortBase;

public class ShadowInputPort<T> extends PortBase implements IShadowInputPort<T> {

    private IInputPort<T> _target;
    private IInputPort<T> _base;

    public ShadowInputPort(String id, IInputPort<T> base) {
        super(id);
        _base = base;
    }

    public void setTarget(IInputPort<T> target) {
        _target = target;
    }

    @Override
    public boolean hasTarget() {
        return _target != null;
    }

    @Override
    public T read() {
        if (_target != null) {
            return _target.read();
        }

        return _base.read();
    }
}
