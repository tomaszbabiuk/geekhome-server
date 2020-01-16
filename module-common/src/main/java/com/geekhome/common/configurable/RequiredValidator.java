package com.geekhome.common.configurable;

import com.geekhome.common.localization.Resource;

public class RequiredValidator implements Validator<String> {
    @Override
    public Resource getReason() {
        return new Resource("validator_required_field",
                "This field is required",
                "To pole jest wymagane");
    }

    @Override
    public boolean validate(String fieldValue) {
        return fieldValue != null && !fieldValue.isEmpty();
    }
}

/*
class RequiredValidator() : Validator<String>
{
    override var reason: Int = R.string.validator_required

    override fun validate(fieldData: String): Boolean {
        return fieldData.isNotEmpty()
    }
}
 */
