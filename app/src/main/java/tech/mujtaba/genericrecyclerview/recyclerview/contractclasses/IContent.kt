package tech.mujtaba.genericrecyclerview.recyclerview.contractclasses

import android.view.View
import tech.mujtaba.genericrecyclerview.recyclerview.interfaces.IModel

/**
 * This interface represents one cell of a recyclerView and it is meant to be used with a GenericRecyclerView
 */
interface IContent {

    /**
     * Return the layout id of the view you want to be inflated here. The recyclerview will inflate that view and
     * return it back in the populateView function
     */
    fun getViewResource(): Int

  /*  *//**
     * Return a view object that will be attached to the recyclerview. Use this method instead of getViewResource()
     * if you want to use a custom view. Do not use both
     *//*
    fun getViewObject() : View? {
        return null
    }*/


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




    companion object {

        /**
         * Convenience method to flatten a list of IContent objects
         */
        fun <T : IContent> flattenList(list: List<T>): MutableList<IContent> {
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
         * Un-flattens the list provided and returns it.
         */
        fun <T : IContent> unFlattenList(list: List<T>): List<IContent> {
            return list.filter { it !is IContentChild }
        }

    }

}
