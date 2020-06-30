package tech.mujtaba.genericrecyclerview.recyclerview

import android.view.View
import tech.mujtaba.genericrecyclerview.recyclerview.content.IContent

/**
 * An empty cell that you can provide to the generic recycler view for it to show
 * when no list has ever been set on the recyclerview. You can provide your own version of this.
 * But this is just here to make stuff easier
 *
 * Please make sure that your provided item types does not conflict with the one this class uses
 */
class EmptyIContent(private val emptyViewResId: Int) : IContent {

    override fun getViewResource() = emptyViewResId

    override fun isContentSameAs(providedObject: IContent?) = false

    override fun getItemType() = ITEM_TYPE_GENERIC_EMPTY

    override fun populateView(view: View) {}

    override fun isSameItemAs(providedObject: IContent?) = false
}