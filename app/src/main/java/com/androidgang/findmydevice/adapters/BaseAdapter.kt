package com.androidgang.findmydevice.adapters

import androidx.recyclerview.widget.RecyclerView


/**
 *  Базовый класс для RecycleView. В нём реализованы все необходимые методы.
 *  Благодаря этому классу, код сокращается в разы для написания адаптеров
 * */
abstract class BaseAdapter<P>: RecyclerView.Adapter<BaseViewHolder<P>>() {


    /**Список информации, который будет устанолен в RecycleView*/
    var mDataList: MutableList<P> = ArrayList()
    /** Callback*/
    protected var mCallback : BaseAdapterCallback<P>? = null

    /** Флаг, который показывает наличие объектов в списке*/
    var hasItems = false

    /**Метод для присоединения колбэков
     * @param callback Присоединяемый callback
     * */
    fun attachCallback(callback: BaseAdapterCallback<P>) {
        this.mCallback = callback
    }


    /** Устанока списка
     *  @param dataList Список инфорамции
     * */
    fun setList(dataList: List<P>) {
        mDataList.addAll(dataList)
        hasItems = true
        notifyDataSetChanged()
    }


    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the RecyclerView.ViewHolder.itemView to reflect the item at the given position.
     * @param holder VH: The ViewHolder which should be updated to represent the contents of the item at the given position in the data set
     * @param position The position of the item within the adapter's data set.
     * */
    override fun onBindViewHolder(holder: BaseViewHolder<P>, position: Int) {
        holder.bind(mDataList[position])
        holder.itemView.setOnClickListener {
            mCallback?.onItemClick(mDataList[position], holder.itemView)
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return number of items in the data
     * */
    override fun getItemCount(): Int {
        return mDataList.count()
    }
}