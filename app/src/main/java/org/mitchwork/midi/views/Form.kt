package org.mitchwork.midi.views

class Form {

    private var validators: List<BlankValidator> = arrayListOf()

    fun addValidator(validator: BlankValidator) {
        validators = validators + validator
    }

    fun validate(): Boolean {
        var valid = true
        for (validator in validators) {
            if (!validator.validate()) {
                valid = false
            }
        }
        return valid
    }
}