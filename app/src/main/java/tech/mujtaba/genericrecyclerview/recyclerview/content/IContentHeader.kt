package tech.mujtaba.genericrecyclerview.recyclerview.content

/**
 * This interface represents a cell in a recycler view which is also a header to other IContentChild Objects
 */
interface IContentHeader : IContent

fun IContentHeader.getChildrenFromList(list: List<IContent>): List<IContentChild> {
    return list.filter { it is IContentChild && it.parent == this } as List<IContentChild>
}