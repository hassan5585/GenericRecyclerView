package tech.mujtaba.genericrecyclerview.recyclerview.content

import androidx.annotation.LayoutRes
import android.view.View

/**
 * This interface represents one cell of a recyclerView and it is meant to be used with a GenericRecyclerView
 */
interface IContent {

    /**
     * Return the layout id of the view you want to be inflated here. The recyclerview will inflate that view and
     * return it back in the populateView function
     */
    @LayoutRes
    fun getViewResource(): Int

    /**
     * Distinguish between two IContent objects on the list
     *
     */
    fun isSameItemAs(providedObject: IContent?): Boolean

    /**
     * Given that the items are the same, are their contents the same as well
     */
    fun isContentSameAs(providedObject: IContent?): Boolean

    /**
     * Return the span size this cell wants in its layout
     */
    fun getSpanSize(): Int {
        return 1
    }

    /**
     *  Try to return a unique item type here, otherwise you might have problems with
     *  recycler view inflating the wrong type of view for you. Aggregate those view Ids in this
     *  interface's companion object
     */
    fun getItemType(): Int


    /**
     * The recyclerview will inflate the view provided by getViewResource
     * and pass the view here. You then have to make sure the view values change
     * according to any data that you may have
     */
    fun populateView(view: View)
}
