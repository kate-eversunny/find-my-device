package com.androidgang.findmydevice.adapters

import android.view.View

/**
 *  Базовый интерфейс для создания колбэков
 * */
interface BaseAdapterCallback<T> {
    /**
     * Нажатие
     * @param model Объект, который хранился в этом view
     * @param view View, на который нажали
     * */
    fun onItemClick(model: T, view: View)

}