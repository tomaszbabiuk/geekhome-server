package com.geekhome.common.configurable;

import com.geekhome.common.localization.Resource;

public abstract class FieldDefinition<T> {
    private String _name;
    private Resource _hint;
    private Class<T> _valueClazz;
    private FieldBuilder<T> _builder;
    private Validator<T> _validator;
    private int _maxSize;
    private boolean _required;

    public String getName() {
        return _name;
    }

    public Resource getHint() {
        return _hint;
    }

    public Class<T> getValueClazz() {
        return _valueClazz;
    }

    public Validator<T> getValidator() {
        return _validator;
    }

    public FieldBuilder<T> getBuilder() {
        return _builder;
    }


    public int getMaxSize() {
        return _maxSize;
    }

    public boolean isRequired() {
        return _required;
    }

    protected FieldDefinition(String name, Resource hint, Class<T> valueClazz, FieldBuilder<T> builder,
                              Validator<T> validator, boolean required, int maxSize) {
        _name = name;
        _hint = hint;
        _valueClazz = valueClazz;
        _builder = builder;
        _validator = validator;
        _required = required;
        _maxSize = maxSize;
    }
}
