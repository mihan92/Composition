package com.example.composition.domain.entity


import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.composition.R

interface OnOptionClickListener{

    fun onOptionClick(option: Int)
}

@BindingAdapter("minAnswers")
fun bindMinAnswers(textView: TextView, count: Int){
    textView.text = String.format(
        textView.context.getString(R.string.tv_min_right_answers),
        count
    )
}

@BindingAdapter("countRightAnswers")
fun bindRightAnswers(textView: TextView, count: Int){
    textView.text = String.format(
        textView.context.getString(R.string.tv_right_answers),
        count
    )
}

@BindingAdapter("generateQuestion")
fun bindGenerateQuestion(textView: TextView, number: Int){
    textView.text = number.toString()
}

@BindingAdapter("visibleNumber")
fun bindVisibleNumber(textView: TextView, number: Int){
    textView.text = number.toString()
}

@BindingAdapter("onOptionClickListener")
fun bindOnOptionClickListener(button: Button, clickListener: OnOptionClickListener){
    button.setOnClickListener {
        clickListener.onOptionClick(button.text.toString().toInt())
    }
}
