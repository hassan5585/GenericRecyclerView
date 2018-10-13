package tech.mujtaba.genericrecyclerview.recyclerview.convenience

import tech.mujtaba.genericrecyclerview.recyclerview.contractclasses.IContentChild
import tech.mujtaba.genericrecyclerview.recyclerview.contractclasses.IContentHeader

/**
 * A header that can hide its children upon request. It hides them by removing them from the list it provides the
 * recyclerview. But it keeps a copy of those items in a separate list. That list can then be restored later if
 * required
 */
abstract class IContentHeaderHideable : IContentHeader {

    override var children: MutableList<IContentChild> = mutableListOf()

    var isCleared = false
    private set

    private val childrenReferenceCopy : MutableList<IContentChild> = mutableListOf()

    fun clear() {
        if (!isCleared) {
            childrenReferenceCopy.clear()
            childrenReferenceCopy.addAll(children)
            children.clear()
            isCleared = true
        }
    }

    fun restore() {
        if (isCleared) {
            children.clear()
            children.addAll(childrenReferenceCopy)
            childrenReferenceCopy.clear()
            isCleared = false
        }
    }

    open fun toggle() {
        if (isCleared) {
            restore()
        }else clear()
    }
}