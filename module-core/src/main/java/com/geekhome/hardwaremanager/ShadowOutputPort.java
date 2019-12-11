package com.geekhome.hardwaremanager;

import com.geekhome.common.PortBase;

public class ShadowOutputPort<T> extends PortBase implements IOutputPort<T> {

    private IOutputPort<T> _target;
    private IOutputPort<T> _base;

    public ShadowOutputPort(String id, IOutputPort<T> base) {
        super(id);
        _base = base;
    }

    public void setTarget(IOutputPort<T> target) {
        _target = target;
    }

    @Override
    public void write(T value) throws Exception {
        if (_target != null) {
            _target.write(value);
        } else {
            _base.write(value);
        }
    }

    @Override
    public T read() {
        if (_target != null) {
            return _target.read();
        }

        return _base.read();
    }
}
