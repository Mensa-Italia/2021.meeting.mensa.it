package swaix.dev.mensaeventi.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import swaix.dev.mensaeventi.R
import swaix.dev.mensaeventi.databinding.LayoutSearchBarLabelBinding





@SuppressLint("ClickableViewAccessibility")
class SearchBarLabel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : MotionLayout(context, attrs, defStyle) {


    private var _listener: OnEventListener? = null

    fun addListener(listener: OnEventListener) {
        _listener = listener
    }

    private fun EditText.showKeyboard() {
        requestFocus()
        val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun EditText.hideKeyboard() {
        clearFocus()
        ViewCompat.getWindowInsetsController(this)?.hide(WindowInsetsCompat.Type.ime())
    }

    init {
        // Init View
        val binding = LayoutSearchBarLabelBinding.inflate(LayoutInflater.from(context), this, true)
        with(binding) {
            val attrsArray = getContext().obtainStyledAttributes(attrs, R.styleable.SearchBarLabel, defStyle, 0)
            val titleString = attrsArray.getString(R.styleable.SearchBarLabel_label)
            attrsArray.recycle()

            searchLabel.text = titleString
            root.setTransitionListener(object : TransitionListener {
                override fun onTransitionStarted(motionLayout: MotionLayout, i: Int, i1: Int) {
                    Log.i(TAG, "onTransitionStarted: ")
                }

                override fun onTransitionChange(motionLayout: MotionLayout, i: Int, i1: Int, v: Float) {

                }

                override fun onTransitionCompleted(motionLayout: MotionLayout, i: Int) {
                    if (i == R.id.searchEnd)
                        searchValue.showKeyboard()
                    else
                        searchValue.hideKeyboard()
                }

                override fun onTransitionTrigger(motionLayout: MotionLayout, i: Int, b: Boolean, v: Float) {}
            })

            searchValue.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    _listener?.onTextChanged(p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

        }

    }

    interface OnEventListener {
        fun onTextChanged(value: String)
    }
}