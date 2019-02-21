package org.mitchwork.midi.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import org.mitchwork.midi.R

class BlankValidator(
    private val context: Context,
    private val textView: TextView
) : TextWatcher {

    init {
        textView.addTextChangedListener(this)
    }

    override fun afterTextChanged(s: Editable?) {
        if (s == null) return
        if (s.isEmpty()) {
            textView.error = context.getString(R.string.form_validation_empty)
        } else {
            textView.error = null
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    fun validate(): Boolean {
        if (textView.text.isBlank()) {
            textView.error = context.getString(R.string.form_validation_empty)
            return false
        }
        textView.error = null
        return true
    }
}