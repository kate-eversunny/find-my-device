package com.androidgang.findmydevice.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/***
 * Базовый ViewHolder. С помощью него код сокращается в разы
 */
abstract class BaseViewHolder<T>(itemView: View): RecyclerView.ViewHolder(itemView) {
    /**
     *  Связывание информации из списка с View. По сути часть патерна Адаптер
     *  @param model Инфорамция, которая связывается с View
     * */
    abstract fun bind(model: T)
}