package com.codefal.mystoryapp.view.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import com.codefal.mystoryapp.R

class EditTextPassword : AppCompatEditText {
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
            error = if (text.toString().length <= 7)
                context.getString(R.string.password_error_message)
            else
                null
        }

    }

}