package tech.mujtaba.genericrecyclerview.recyclerview.layoutmanagers

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import tech.mujtaba.genericrecyclerview.recyclerview.GenericRecyclerView


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