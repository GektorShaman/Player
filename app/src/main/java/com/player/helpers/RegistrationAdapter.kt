package com.player.helpers

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import java.util.ArrayList
import android.widget.Toast
import com.player.R


class RegistrationAdapter (private val context: Context, private val regDataset: List<String>, private val footerClick: (Int, String, String)->Unit, val itemCheck:()->Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val validateList: ArrayList<EditText?> = ArrayList<EditText?>()
    private val regDataQuantity: Int = 4

    override fun getItemViewType(position: Int): Int {
        if (isPositionHeader(position)) {
            return TYPE_HEADER
        } else if (isPositionFooter(position)) {
            return TYPE_FOOTER
        }
        return TYPE_ITEM
    }

    private fun isPositionHeader(position: Int): Boolean {
        return position == 0
    }

    private fun isPositionFooter(position: Int): Boolean {
        return position > regDataset.size - 2
    }

    private fun <T : RecyclerView.ViewHolder> T.onClick(event: (type: Int, userLogin: String, userPassword: String) -> Unit): T {
        itemView.setOnClickListener {
            if (validateList.size > 2) {
                if (isValidRegistration()) event.invoke(
                    0,
                    validateList[0]?.text.toString(),
                    validateList[3]?.text.toString()
                )
            } else if (isValidLogin()) event.invoke(
                1,
                validateList[0]?.text.toString(),
                validateList[1]?.text.toString()
            )
        }
        return this
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_ITEM) {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_layout, viewGroup, false)
            return ItemViewHolder(view)
        } else if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.header_layout, viewGroup, false)
            return HeaderViewHolder(view)
        } else if (viewType == TYPE_FOOTER) {
            val view = LayoutInflater.from(viewGroup.context).inflate(
                R.layout.footer_layout,
                viewGroup, false
            )
            return FooterViewHolder(view)
        }
        throw RuntimeException("there is no type that matches the type $viewType + make sure your using types correctly")
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            if (regDataset[position] == context.getString(R.string.reg)) {
                holder.regRadioButton?.isChecked = true
                holder.loggingRadioButton?.isChecked = false
                holder.regRadioButton?.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.regRadioButton?.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.blue
                    )
                )
                holder.regRadioButton?.setOnCheckedChangeListener(holder)
            } else {
                holder.loggingRadioButton?.isChecked = true
                holder.regRadioButton?.isChecked = false
                holder.loggingRadioButton?.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
                holder.loggingRadioButton?.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.blue
                    )
                )
                holder.loggingRadioButton?.setOnCheckedChangeListener(holder)
            }

        } else if (holder is ItemViewHolder) {
            holder.editTextView?.hint = regDataset[position]
            holder.editTextView?.isSingleLine = true
            if (regDataset.size > regDataQuantity) {
                when (position) {
                    2 -> {
                        holder.editTextView?.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    }
                    3 -> {
                        holder.editTextView?.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        holder.editLayout?.isPasswordVisibilityToggleEnabled = true
                    }
                    4 -> {
                        holder.editTextView?.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD
                        holder.editLayout?.isPasswordVisibilityToggleEnabled = true
                        holder.editTextView?.imeOptions = EditorInfo.IME_ACTION_DONE
                        holder.editTextView?.setOnEditorActionListener(OnEditorActionListenerEdit())
                    }
                }
            } else if (position == 2) {
                holder.editTextView?.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                holder.editLayout?.isPasswordVisibilityToggleEnabled = true
                holder.editTextView?.imeOptions = EditorInfo.IME_ACTION_DONE
                holder.editTextView?.setOnEditorActionListener(OnEditorActionListenerEdit())
            }

            holder.editTextView?.addTextChangedListener(FieldValidation(holder.editTextView!!))
            validateList.add(holder.editTextView)
        } else if (holder is FooterViewHolder) {
            holder.onClick(footerClick)
            holder.footerTextView?.text = regDataset[position]
            holder.footerTextView?.setTextColor(ContextCompat.getColor(context, R.color.blue))
        }
    }



    override fun getItemCount(): Int {
        return regDataset.size
    }


    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        CompoundButton.OnCheckedChangeListener {
        var loggingRadioButton: RadioButton? = null
        var regRadioButton: RadioButton? = null

        init {
            loggingRadioButton = itemView.findViewById(R.id.log)
            regRadioButton = itemView.findViewById(R.id.reg)
        }

        override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
            itemCheck()
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var editTextView: EditText? = null
        var editLayout: TextInputLayout? = null

        init {
            editTextView = itemView.findViewById(R.id.editText)
            editLayout = itemView.findViewById(R.id.editLayout)
        }
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var footerTextView: TextView? = null

        init {
            footerTextView = itemView.findViewById(R.id.textViewFooter)
        }
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
        private const val TYPE_FOOTER = 2
    }

    inner class FieldValidation(private val view: EditText) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            /*when (view.inputType) {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS -> validateEmail()
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD -> validatePassword()
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD -> validateConfirmPassword()
                else -> validateLogin()
            }*/
        }
    }

    inner class OnEditorActionListenerEdit() : TextView.OnEditorActionListener
    {
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (validateList.size > 2) {
                    if (isValidRegistration()) footerClick(
                        0,
                        validateList[0]?.text.toString(),
                        validateList[3]?.text.toString()
                    )
                } else if (isValidLogin()) footerClick(
                    1,
                    validateList[0]?.text.toString(),
                    validateList[1]?.text.toString()
                )
            }
            return false
        }
    }

    fun isValidRegistration(): Boolean {
        return (validateEmail() and validatePassword() and validateConfirmPasswordWhenReg() and validateLogin())
    }

    fun isValidLogin(): Boolean {
        return (validatePassword() and validateLogin())
    }

    fun validateLogin(): Boolean {

        if (validateList[0]?.text.toString().trim().isEmpty()) {
            validateList[0]?.error = "Requered Field"
            validateList[0]?.requestFocus()
            return false
        }
        return true
    }

    fun validateEmail(): Boolean {
        if (validateList.size > 2) {
            if (validateList[1]?.text.toString().trim().isEmpty()) {
                validateList[1]?.error = "Requered Field"
                validateList[1]?.requestFocus()
                return false
            } else if (!validateList[1]?.text.toString().isEmailValid()) {
                validateList[1]?.error = "Invalid Email"
                validateList[1]?.requestFocus()
                return false
            }
        }
        return true

    }

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    fun validatePassword(): Boolean {
        val elementPosition: Int
        if (validateList.size > 2)
            elementPosition = 2
        else elementPosition = 1
        if (validateList[elementPosition]?.text.toString().trim().isEmpty()) {
            validateList[elementPosition]?.error = "Requered Field"
            validateList[elementPosition]?.requestFocus()
            return false
        } else if (validateList[elementPosition]?.text.toString().length < 6) {
            validateList[elementPosition]?.error = "Password must be more than 6 symbols"
            validateList[elementPosition]?.requestFocus()
            return false
        } else if (!validateList[elementPosition]?.text.toString().contains("[0-9]".toRegex())) {
            validateList[elementPosition]?.error = "Password must have at least 1 digit"
            validateList[elementPosition]?.requestFocus()
            return false
        } else if (!validateList[elementPosition]?.text.toString().contains("[A-Z]".toRegex())) {
            validateList[elementPosition]?.error = "Password must have upper case"
            validateList[elementPosition]?.requestFocus()
            return false
        } else if (!validateList[elementPosition]?.text.toString().contains("[a-z]".toRegex())) {
            validateList[elementPosition]?.error = "Password must have lower case"
            validateList[elementPosition]?.requestFocus()
            return false
        } else if (!validateList[elementPosition]?.text.toString().contains("[!-/]".toRegex())) {
            validateList[elementPosition]?.error = "Password must have special symbols"
            validateList[elementPosition]?.requestFocus()
            return false
        }
        return true
    }

    private fun validateConfirmPassword(): Boolean {
        if (validateList[3]?.text.toString().trim().isEmpty()) {
            validateList[3]?.error = "Requered Field"
            validateList[3]?.requestFocus()
            return false
        }
        return true
    }

    private fun validateConfirmPasswordWhenReg(): Boolean {
        if (validateList[3]?.text.toString().trim().isEmpty()) {
            validateList[3]?.error = "Requered Field"
            validateList[3]?.requestFocus()
            return false
        } else if (validateList[2]?.text.toString() != validateList[3]?.text.toString()) {
            validateList[3]?.error = "Passwords don't match"
            validateList[3]?.requestFocus()
            return false
        }
        return true
    }
}