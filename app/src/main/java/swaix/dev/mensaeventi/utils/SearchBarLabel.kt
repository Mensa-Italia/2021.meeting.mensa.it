package swaix.dev.mensaeventi.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.android.material.textfield.TextInputEditText
import swaix.dev.mensaeventi.R

class SearchBarLabel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : MotionLayout(context, attrs, defStyle) {


    private var _listener: OnEventListener? = null

    fun addListener(listener: OnEventListener) {
        _listener = listener
    }

    init {
        // Init View
        val rootView = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.layout_search_bar_label, this, true)
        val titleView = rootView.findViewById<TextView>(R.id.search_label)
        val iconSearch = rootView.findViewById<ImageView>(R.id.search_icon)
        val searchValue = rootView.findViewById<TextInputEditText>(R.id.search_value)

        // Load Values from XML
        val attrsArray = getContext().obtainStyledAttributes(attrs, R.styleable.SearchBarLabel, defStyle, 0)
        val titleString = attrsArray.getString(R.styleable.SearchBarLabel_label)
        attrsArray.recycle()

        titleView.text = titleString


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

    interface OnEventListener {
        fun onKeyboardOpen(v: View)
        fun onTextChanged(value: String)
    }
}