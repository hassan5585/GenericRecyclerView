package tech.mujtaba.genericrecyclerview.recyclerview.contractclasses

import java.util.*

/**
 * This interface represents a cell in a recycler view which is also a header to other IContentChild Objects
 */
interface IContentHeader : IContent {

    /**
     * Return a list of contents that will appear below this one. This will then act as a header
     * for the returned list
     */
    var children : MutableList<IContentChild>



    /**
     * Call this function to make sure your children know of you as a parent. If you don't,
     * the parent will know of the children, but the children won't know of the parent
     */
    fun assignParentToChildren(){
        children?.let {
            for (c in it) {
                c.parent = this
            }
        }
    }

    /**
     * Return the count of children
     */
    fun getChildCount(): Int {
        return children?.size ?: 0
    }

    /**
     * Sorts children based on the default comparator. If the default comparator is not suitable,
     * provide a different one in getComparator function
     */
    fun sort() {
        children?.let {
            Collections.sort(it, getComparator())
        }
    }

    /**
     * Override this function to provide your own comparator. Remember to remove superclass version
     * before returning. This comparator is for how this parent wants to sort its children
     */
    fun getComparator() : Comparator<IContentChild> {
        return comparator
    }


    companion object {
        val comparator: Comparator<IContentChild> = Comparator { i1, i2 ->
            i1.getPreferredPositionInParent() - i2.getPreferredPositionInParent()
        }
    }

}