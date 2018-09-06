package tech.mujtaba.genericrecyclerview.recyclerview.interfaces.contentcells

import android.view.View
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
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
        fun flattenList(list: List<IContent>): List<IContent> {
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
         * Wraps the flattenlist function in an observable for easier flattening on a
         * background thread
         */
        fun flattenListObservable(list: List<IContent>) : Single<List<IContent>>{
            return Single.defer {
                Single.just(flattenList(list))
            }.subscribeOn(Schedulers.io())
        }

        /**
         * Un-flattens the list provided and returns it.
         */
        fun unFlattenList(list: List<IContent>): List<IContent> {
            return list.filter { it !is IContentChild }
        }

    }

}
