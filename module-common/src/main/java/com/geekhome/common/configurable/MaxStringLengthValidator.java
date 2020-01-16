package com.geekhome.common.configurable;

public class MaxStringLengthValidator {
}

/*
class MaxStringLengthValidator(private val maxLength: Int)
    : Validator<String>
{
    override var reason: Int = R.string.validator_max_length

    override fun validate(fieldData: String): Boolean {
        if (fieldData.isNotEmpty()) {
            return fieldData.length <= maxLength
        }

        return true
    }
}
 */
