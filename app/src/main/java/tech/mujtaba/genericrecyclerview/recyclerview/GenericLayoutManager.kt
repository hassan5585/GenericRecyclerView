package tech.mujtaba.genericrecyclerview.recyclerview

import android.content.Context
import android.support.v7.widget.GridLayoutManager


/**
 * This layout manager only works with a GenericRecyclerView because it's span size is dependant
 * on the recyclerview items being an IContent Object
 */
class GenericLayoutManager(val recyclerView: GenericRecyclerView, context : Context, spanCount : Int, orientationInt: Int)
    : GridLayoutManager(context,spanCount,orientationInt,false) {


    init {
        spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return recyclerView.listUsedForAdapter[position].getSpanSize()
            }
        }
    }




}