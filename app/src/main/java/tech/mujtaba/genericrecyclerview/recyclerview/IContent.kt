package tech.mujtaba.genericrecyclerview.recyclerview

import android.view.View
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

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

    fun getCount(): Int {
        var count = 1
        contents?.let {
            count += it.size
        }
        return count
    }

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
     * Will return true if this Content has a list of other contents it wants to show
     * If the list is null or empty, it will return false
     */
    fun hasContent(): Boolean {
        contents?.let {
            if (it.isNotEmpty()) {
                return true
            }
            return false
        }
        return false
    }

    /**
     * Call this function to make sure your children know of you as a parent
     */
    fun assignParentToChildren(){
        contents?.let {
            for (c in it) {
                c.parent = this
            }
        }
    }

    /**
     * Call this function to remove yourself as a parent from children
     */
    fun removeParentFromChildren(){
        contents?.let {
            for (c in it) {
                c.parent = null
            }
        }
    }

    /**
     * Will return true if this object has a parent
     */
    fun hasParent(): Boolean {
        parent?.let {
            return true
        }
        return false
    }

    /**
     * Return a list of contents that will appear below this one. This will then act as a header
     * for the returned list
     */
    var contents : MutableList<IContent>?


    /**
     * Sorts children based on the default comparator. If the default comparator is not suitable,
     * provide a different one in getComparator function
     */
    fun sort() {
        contents?.let {
            Collections.sort(it, getComparator())
        }
    }

    /**
     * Override this function to provide your own comparator. Remember to remove superclass version
     * before returning. This comparator is for how this parent wants to sort its children
     */
    fun getComparator() : Comparator<IContent> {
        return comparator
    }


    /**
     * Sets a list of children and stores them in the contents variable. Also make sure those children know
     * of this object as a parent. Do not set the contents variable directly,If you do, you will have to make sure the children
     * know about their parent. Use this function instead
     */
    fun <T : IContent> setChildren(list: MutableList<T>?){
        list?.let {
            if (contents == null) {
                contents = mutableListOf()
            }
            contents?.clear()
            contents?.addAll(list)
            assignParentToChildren()
        }
    }


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

    /**
     * The parent object for this IContent object
     */
    var parent : IContent?

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
                if (t.hasContent()) {
                    t.contents?.let {
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
        fun unFlattenList(list: List<IContent>): MutableList<IContent> {
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
        }

    }

    interface IClickListener {
        fun onClick(content : IContent)
    }
}
