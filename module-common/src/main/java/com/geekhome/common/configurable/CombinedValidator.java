package com.geekhome.common.configurable;

public class CombinedValidator {
}

/*
class CombinedValidator<T>(val validators: Array<Validator<T>>)
    : Validator<T>
{
    override var reason: Int = R.string.validator_not_set

    override fun validate(fieldData: T): Boolean {
        validators.forEach {
            if (!it.validate(fieldData)) {
                reason = it.reason
                return false
            }
        }

        return true
    }
}

 */
