package com.codefal.mystoryapp.view.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import androidx.core.widget.doOnTextChanged
import com.codefal.mystoryapp.R
import com.google.android.material.textfield.TextInputEditText

class EditTextEmail : TextInputEditText {

    constructor(context: Context) : super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        doOnTextChanged { text, _, _, _ ->
            error = if (!Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches())
                context.getString(R.string.email_error_message)
            else
                null
        }
    }

}