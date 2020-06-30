package tech.mujtaba.genericrecyclerview.recyclerview.content

/**
 * Represent one cell that is a child of some other cell of type IContentHeader
 */
interface IContentChild : IContent {
    /**
     * The parent object for this IContent object
     */
    val parent : IContentHeader
}