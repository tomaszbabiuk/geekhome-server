package com.geekhome.common.configurable;

import com.geekhome.common.localization.Resource;

public abstract class Field<T> {
    private Resource _name;
    private Resource _hint;
    private Class<T> _clazz;
    private final FieldBuilder<String> _builder;
    private FieldBuilder<T> _fieldBuilder;
    private Validator<T> _validator;
    private T _value;

    public Resource getName() {
        return _name;
    }

    public Resource getHint() {
        return _hint;
    }

    public Class<T> getClazz() {
        return _clazz;
    }

    public Validator<T> getValidator() {
        return _validator;
    }

    public T getValue() {
        return _value;
    }

    public void setValue(T value) {
        _value = value;
    }

    public String toPersistableString() {
        if (_value == null) {
            return null;
        }

        return _fieldBuilder.toPersistableString(getValue());
    }

    public void setValueFromString(String valueAsString) {
        _value = (valueAsString == null) ? null : _fieldBuilder.fromPersistableString(valueAsString);
    }

    protected Field(Resource name, Resource hint, Class<T> clazz, FieldBuilder<String> builder, Validator<T> validator) {
        _name = name;
        _hint = hint;
        _clazz = clazz;
        _builder = builder;
        _validator = validator;
    }
}

/*
abstract class Field<T : Any>(
    val name: String,
    @StringRes val hint: Int,
    val clazz: KClass<T>,
    private val builder: FieldBuilder<T>,
    val validator: Validator<T>? = null
) {
    var value : T? = null

    fun toPersistableString() : String? {
        if (value == null) {
            return null
        }

        return builder.toPersistableString(value!!)
    }

    fun setValueFromString(valueAsString: String?) {
        value = if (valueAsString == null) {
            null
        } else {
            builder.fromPersistableString(valueAsString)
        }
    }
}
 */
