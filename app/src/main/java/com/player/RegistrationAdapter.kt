package com.player

import android.graphics.Color
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RegistrationAdapter (private val regDataset: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val regDataQuantity: Int = 4

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
        return position > regDataset.size-2
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
            if (regDataset[position]=="Регистрация")
            {

                holder.regRadioButton?.isChecked = true
                holder.loggingRadioButton?.isChecked = false
                holder.regRadioButton?.setTextColor(Color.parseColor("#ffffff"))
                holder.regRadioButton?.setBackgroundColor(Color.parseColor("#0000ff"))
            }
            else {
                holder.loggingRadioButton?.isChecked = true
                holder.regRadioButton?.isChecked = false
            }

        } else if (holder is ItemViewHolder) {
            holder.editTextView?.setHint(regDataset[position])
            if (regDataset.size>regDataQuantity) {
                when (position) {
                    2 -> holder.editTextView?.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    3 -> holder.editTextView?.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    4 -> {
                        holder.editTextView?.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }
                }
            }
            else if (position==2) holder.editTextView?.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        } else if (holder is FooterViewHolder) {
            holder.footerTextView?.text=regDataset[position]
            holder.footerTextView?.setTextColor(Color.parseColor("#0000ff"))
        }
    }



    override fun getItemCount(): Int {
        return regDataset.size
    }


    inner class HeaderViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        var loggingRadioButton: RadioButton? = null
        var regRadioButton: RadioButton? = null

        init {
            loggingRadioButton= itemView.findViewById(R.id.log)
            regRadioButton= itemView.findViewById(R.id.reg)
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var editTextView: EditText? = null
        init {
            editTextView = itemView.findViewById(R.id.editText)
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


}