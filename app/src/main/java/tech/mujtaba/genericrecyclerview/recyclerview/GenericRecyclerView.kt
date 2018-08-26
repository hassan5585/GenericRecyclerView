package tech.mujtaba.genericrecyclerview.recyclerview

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tech.mujtaba.genericrecyclerview.recyclerview.contractclasses.IContent

open class GenericRecyclerView @JvmOverloads constructor(context: Context,
                               attrs: AttributeSet? = null,
                               defStyle: Int = 0) : RecyclerView(context, attrs, defStyle) {

    var listUsedForAdapter: MutableList<IContent> = mutableListOf()
        private set
    private var unFilteredList: MutableList<IContent> = mutableListOf()
    private var unFlattenedList: MutableList<IContent> = mutableListOf()
    private val viewMapWithResourceIds: MutableMap<Int, Int> = mutableMapOf()
    private val viewMapWithViewObjects: MutableMap<Int, View> = mutableMapOf()
    private var adapter: GenericAdapter? = null
    private var isInFilteredState = false

    // Set this property to show an empty view when filtering does not return anything.
    // If you do not set a value here, it will just show an empty list
    var emptyFilter: IContent? = null

    init {
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
    }

    /**
     * This view will not let you set any other adapter other than a Generic Adapter
     * It will throw an exception if you try to set
     */
    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter is GenericAdapter) {
            super.setAdapter(adapter)
        } else throw ListException(ListException.EXCEPTION_WRONG_ADAPTER)
    }

    /**
     * Use this method to replace the list used by this recycler view. It replaces the entire list
     * and is not smart about it. Try to use the append function because that caters to the content
     * already present in the list
     */
    fun setList(providedList: List<IContent>?) {
        providedList?.let {
            if (it.isNotEmpty()) {
                listUsedForAdapter.clear()
                unFlattenedList.clear()
                unFlattenedList.addAll(it)
                listUsedForAdapter = flattenList(it)
                createViewMapping(listUsedForAdapter)
                initAdapterSetList()
            }
        }
    }

    private fun initAdapterSetList() {
        if (adapter == null) {
            adapter = GenericAdapter()
            setAdapter(adapter)
        } else {
            adapter?.notifyDataSetChanged()
        }
    }

    /*
     Pass in a specific parent object, if you just want to sort within that object's children.
     Otherwise, every parent in the listUsedForAdapter will be asked to sort its children
     Also, any filter that you have applied will be removed if you call this function
      */
    fun sort(parent: IContent? = null) {
        adapter?.let {
            resetFilter()
            val tempList: MutableList<IContent> = mutableListOf()
            tempList.addAll(unFlattenedList)
            if (parent == null) {
                for (content in tempList) {
                    if (content.hasContent()) {
                        content.sort()
                    }
                }
            } else {
                for (content in tempList) {
                    if (content.hasContent() && content == parent) {
                        content.sort()
                    }
                }
            }
            listUsedForAdapter.clear()
            listUsedForAdapter.addAll(flattenList(tempList))
            it.notifyDataSetChanged()
        }
    }

    /**
     * Use this function to scroll to the position of any IContent object in the list
     * TODO Add smooth scrolling
     */
    fun scrollToContent(content: IContent, isSmooth: Boolean = false) {
        if (!isSmooth) {
            if (layoutManager is LinearLayoutManager) {
                (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(listUsedForAdapter.indexOf(content), 0)
            } else if (layoutManager is GridLayoutManager) {
                (layoutManager as GridLayoutManager).scrollToPositionWithOffset(listUsedForAdapter.indexOf(content), 0)
            } else {
                layoutManager?.scrollToPosition(listUsedForAdapter.indexOf(content))
            }
        } else {
            // TODO Add smooth scrolling code here
        }
    }


    /**
     * If you want to provide a predicate to start the scroll, use this function. It scrolls to the first
     * value that matches that predicate and ignores the rest
     */
    fun scrollToContent(filterPredicate: ((content: IContent) -> Boolean)?, isSmooth: Boolean = false) {
        filterPredicate?.let {
            for (c in listUsedForAdapter) {
                if (it(c)) {
                    scrollToContent(c, isSmooth)
                    break
                }
            }
        }
    }

    /**
     * Removes any sorting from the listUsedForAdapter and brings it back to the original sort order
     */
    fun removeSort() {
        adapter?.let {
            listUsedForAdapter.clear()
            listUsedForAdapter.addAll(flattenList(unFlattenedList))
            it.notifyDataSetChanged()
        }
    }


    private fun flattenListAndReturnAppendableList(l: List<IContent>): Int {
        val tempList = flattenList(l)
        var sizeToAppend = 0
        Observable.fromIterable(tempList)
                .filter {
                    !listUsedForAdapter.contains(it)
                }
                .toList()
                .subscribe { it1 ->
                    sizeToAppend = it1.size
                    listUsedForAdapter.addAll(it1)
                    createViewMapping(listUsedForAdapter)
                }
        return sizeToAppend
    }

    /**
     * Apply a function to the entire list of contents. If the list is in a filtered state,
     * the function will apply to only the filtered items
     */
    fun applyFunction(function: ((IContent) -> Unit)?) {
        function?.let {
            listUsedForAdapter.map(it)
            adapter?.notifyDataSetChanged()
        }
    }


    /**
     * Use this method to display a listUsedForAdapter to the view. If the view already has a listUsedForAdapter, it will append your listUsedForAdapter at the end.
     * Otherwise it will create a new adapter and set your listUsedForAdapter there.
     */
    fun append(providedList: List<IContent>?, shouldCheckIfAlreadyThere: Boolean = true) {
        providedList?.let {
            unFlattenedList.addAll(it)
            val size = listUsedForAdapter.size
            if (size == 0) {
                setList(it)
            } else {
                if (shouldCheckIfAlreadyThere) {
                    //Update old items

                    Observable.defer {
                        Observable.fromIterable(flattenList(it))
                                .filter { it2 ->
                                    listUsedForAdapter.contains(it2)
                                }

                    }.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { it2 ->
                                val index = listUsedForAdapter.indexOf(it2)
                                listUsedForAdapter[index] = it2
                                adapter?.notifyItemChanged(index)
                            }
                }


                //Append new Items to the list
                Observable.fromCallable {
                    flattenListAndReturnAppendableList(it)
                }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { it2 ->
                            adapter?.notifyItemRangeInserted(size, it2)
                        }
            }
        }
    }

    /**
     * Append a single item to the end of the recyclerView
     * If the item added has subItems, those will also be added beneath it
     */
    fun append(item: IContent?) {
        item?.let {
            append(mutableListOf(it))
        }
    }


    /**
     * Use this function to filter this listUsedForAdapter.
     * @param filterPredicate This is a function that takes in a IContent value and returns a boolean
     * If it returns true for a value, that will be added to the listUsedForAdapter, otherwise it will be removed
     * If you pass in a null to this function, it will clear all filters and return to the original
     * state of the last
     */
    fun filter(filterPredicate: ((content: IContent) -> Boolean)?) {
        if (filterPredicate != null) {
            Observable.fromCallable {
                if (!isInFilteredState) {
                    unFilteredList.addAll(listUsedForAdapter)
                }
                isInFilteredState = true
                listUsedForAdapter.clear()
                listUsedForAdapter.addAll(unFilteredList.filter(filterPredicate))
                if (listUsedForAdapter.size == 0) {
                    emptyFilter?.let {
                        //Empty view added to list in case of empty filter
                        listUsedForAdapter.add(it)

                    }
                }
                createViewMapping(listUsedForAdapter)
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        adapter?.notifyDataSetChanged()
                    }
        } else {
            resetFilter()
        }
    }

    /**
     * Use this function to clear the filter and return the listUsedForAdapter to its original state
     */
    fun resetFilter() {
        if (isInFilteredState) {
            isInFilteredState = false
            listUsedForAdapter.clear()
            listUsedForAdapter.addAll(unFilteredList)
            createViewMapping(listUsedForAdapter)
            unFilteredList.clear()
            adapter?.notifyDataSetChanged()
        }
    }


    /**
     * Creates a map of different viewtypes and their corresponding xml resources to inflate
     */
    private fun createViewMapping(list: List<IContent>) {
        viewMapWithResourceIds.clear()
        viewMapWithViewObjects.clear()
        for (content in list) {
            viewMapWithResourceIds[content.getItemType()] = content.getViewResource()
            if (content.getViewResource() == 0) {
                viewMapWithViewObjects[content.getItemType()] = content.getViewObject() ?: View(context)
            }
        }
    }


    /**
     * Will flatten any IContent listUsedForAdapter to make all IContent objects equal to the
     * recyclerview. But it preserves the order of the items, and hence, a header will have its corresponding items
     * coming after it
     */
    private fun flattenList(list: List<IContent>): MutableList<IContent> {
        return IContent.flattenList(list)
    }


    private inner class GenericAdapter : Adapter<GenericViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
            val viewInflateId = viewMapWithResourceIds[viewType]
            if (viewInflateId == null || viewInflateId == 0) {
                viewMapWithViewObjects[viewType]?.let {
                    return GenericViewHolder(it)
                }
            } else {
                return GenericViewHolder(LayoutInflater.from(context).inflate(viewInflateId, parent, false))
            }
            //If correctly implemented IContent objects are provided, this should not occur
            return GenericViewHolder(LayoutInflater.from(context).inflate(viewInflateId
                    ?: 0, parent, false))
        }

        override fun getItemViewType(position: Int): Int {
            return listUsedForAdapter[position].getItemType()
        }

        override fun getItemCount(): Int {
            return listUsedForAdapter.size
        }

        override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
            holder.setData(listUsedForAdapter[position])
        }

    }


    private class GenericViewHolder(itemView: View) : ViewHolder(itemView) {
        private var content: IContent? = null
        private val clickListener: View.OnClickListener = OnClickListener {
            content?.onClick()
        }

        init {
            itemView.setOnClickListener(clickListener)
        }

        fun setData(content: IContent) {
            this.content = content
            this.content?.initView(itemView)
            this.content?.populateView(itemView)
        }
    }

    private class ListException(exception: String) : Exception(exception) {

        companion object {
            const val EXCEPTION_WRONG_ADAPTER = "This RecyclerView cannot work with any adapter other than a GenericAdapter"
        }
    }
}