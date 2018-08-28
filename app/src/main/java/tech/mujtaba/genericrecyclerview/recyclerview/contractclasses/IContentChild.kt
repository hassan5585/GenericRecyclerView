package tech.mujtaba.genericrecyclerview.recyclerview.contractclasses

/**
 * Represent one cell that is a child of some other cell of type IContentHeader
 */
interface IContentChild : IContent {
    /**
     * The parent object for this IContent object
     */
    var parent : IContentHeader

    /**
     * Will return true if this object has a parent
     */
    fun hasParent(): Boolean {
        return parent != null
    }

    /**
     * Return a position this content cell would like to have within its parent
     * By default, all children will have the same sorting order. So their order depends
     * before sorting, on the order they have in the list of contents
     */
    fun getPreferredPositionInParent(): Int {
        return 0
    }

}