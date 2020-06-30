package tech.mujtaba.genericrecyclerview.recyclerview.layoutmanagers

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
class GenericGridLayoutManager(context: Context, spanCount: Int, orientationInt: Int)
    : GridLayoutManager(context, spanCount, orientationInt, false) {

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return (view as? GenericRecyclerView)?.externalList?.get(position)?.getSpanSize()
                        ?: 1
            }
        }
    }

}