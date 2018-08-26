package tech.mujtaba.genericrecyclerview.recyclerview.contractclasses

import android.view.View
import kotlin.Comparator

/**
 * This interface represents one cell of a recyclerView and it is meant to be used with a GenericRecyclerView
 */
interface IContent {

    /**
     * Return the layout id of the view you want to be inflated here. The recyclerview will inflate that view and
     * return it back in the populateView function
     */
    fun getViewResource(): Int

    /**
     * Return a view object that will be attached to the recyclerview. Use this method instead of getViewResource()
     * if you want to use a custom view. Do not use both
     */
    fun getViewObject() : View? {
        return null
    }

    /**
     * Will be automatically invoked when this cell is clicked. If you want to react to the click,
     * override this method
     */
    fun onClick() {
        clickListener?.onClick(this)
    }

    /**
     * A default equals function you can use for comparisons
     */
    fun isEqualTo(providedObject : Any?) : Boolean{
        if(providedObject == null) return false
        if(providedObject !is IContent) return false
        if ((providedObject is IModel<*>) and (this is IModel<*>)) {
            if ((providedObject as IModel<*>)== (this as IModel<*>)) {
                return true
            }
        }
        return false
    }

    /**
     * Return the span size this cell wants in its layout
     */
    fun getSpanSize() : Int {
        return 1
    }

    /**
     *  Try to return a unique item type here, otherwise you might have problems with
     *  recycler view inflating the wrong type of view for you. Aggregate those view Ids in this
     *  interface's companion object
     */
    fun getItemType(): Int



    /**
     * Return a position this content cell would like to have within its parent
     * By default, all children will have the same sorting order. So their order depends
     * before sorting, on the order they have in the list of contents
     */
    fun getPreferredPositionInParent(): Int {
        return 0
    }

    /**
     * Override and initialize your views here. This method is guaranteed to be called before
     * populateView so you can get and store references to your views here.
     */
    fun initView(view : View)

    /**
     * The recyclerview will inflate the view provided by getViewResource
     * and pass the view here. You then have to make sure the view values change
     * according to any data that you may have
     */
    fun populateView(view: View)





    /**
     * Override to provide a valid ID
     */
    fun getId(): Int {
        return 0
    }

    /**
     * Allows you to provide a click listener when building an IContent object
     * and the listener will be notified of any clicks on the IContent
     */
    val clickListener : IClickListener?



    companion object {

        val comparator: Comparator<IContent> = Comparator { i1, i2 ->
            i1.getPreferredPositionInParent() - i2.getPreferredPositionInParent()
        }

        /**
         * Convenience method to flatten a list of IContent objects
         */
        fun flattenList(list: List<IContent>): MutableList<IContent> {
            val tempList: MutableList<IContent> = mutableListOf()
            for (t in list) {
                tempList.add(t)
                if (t is IContentHeader) {
                    t.children?.let {
                        tempList.addAll(flattenList(it))
                    }
                }
            }
            return tempList
        }

        /**
         * TODO WIP Unflattens the list provided and returns it. The method seems
         * complete but I have to still test it
         */
        /*fun unFlattenList(list: List<IContent>): MutableList<IContent> {
            val map : HashMap<IContent, MutableList<IContent>?> = hashMapOf()
            val tempList: MutableList<IContent> = mutableListOf()
            for (t in list) {
                if (t.hasParent()) {
                    if (map.containsKey(t.parent)) {
                        map[t.parent]?.add(t)
                    }else {
                        map[t.parent!!] = mutableListOf(t)
                    }
                }else {
                    map[t] = mutableListOf()
                }
            }
            for ((key, value) in map) {
                tempList.add(key)
                value?.let {
                    if (it.isNotEmpty()) {
                        tempList.addAll(it)
                    }
                }
            }
            return tempList
        }*/

    }

    interface IClickListener {
        fun onClick(content : IContent)
    }
}
