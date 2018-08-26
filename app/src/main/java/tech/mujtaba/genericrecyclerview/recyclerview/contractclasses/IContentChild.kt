package tech.mujtaba.genericrecyclerview.recyclerview.contractclasses

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

}