package tech.mujtaba.genericrecyclerview.recyclerview.layoutmanagers

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import tech.mujtaba.genericrecyclerview.recyclerview.GenericRecyclerView


/**
 * This layout manager only works with a GenericRecyclerView because it's span size is dependant
 * on the recyclerview items being an IContent Object
 *
 * The nice thing is this grid layout manager can be used as a linearlayoutmanager by just providing a spancount of 1 while
 * creating it. It takes care of the rest
 *
 *
 * When using this class, please make sure that all IContent objects you pass the recyclerview override and return the spansize
 * they want. The default value is 1
 */
class GenericGridLayoutManager(val recyclerView: GenericRecyclerView, context : Context, spanCount : Int, orientationInt: Int)
    : GridLayoutManager(context,spanCount,orientationInt,false) {


    init {
        spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return recyclerView.listUsedForAdapter[position].getSpanSize()
            }
        }
    }




}