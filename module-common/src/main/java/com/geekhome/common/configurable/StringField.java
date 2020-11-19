package com.geekhome.common.configurable;

import com.geekhome.common.localization.Resource;

public class StringField extends FieldDefinition<String> {

    public StringField(String name, Resource hint, Validator<String> validator, boolean required, int maxSize) {
        super(name, hint, String.class, new StringFieldBuilder(), validator, required, maxSize);
    }
}
