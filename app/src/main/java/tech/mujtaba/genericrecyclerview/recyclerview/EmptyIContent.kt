package tech.mujtaba.genericrecyclerview.recyclerview

import android.view.View
import android.widget.TextView
import tech.mujtaba.genericrecyclerview.R

/**
 * An empty cell that you can provide to the generic recycler view for it to show
 * when filtering results in an empty list. You can provide your own version of this.
 * But this is just here to make stuff easier
 */
class EmptyIContent(override var model: String) : IContent, IModel<String>{
    override val clickListener: IContent.IClickListener? = null

    private companion object {
        //A Random value so it does not get mixed with your provided item type
        const val ITEM_TYPE : Int = 324126
    }

    private lateinit var messageText : TextView

    override fun getViewResource(): Int {
        return R.layout.empty_recycler_view_item
    }

    override fun getItemType(): Int {
        return ITEM_TYPE
    }

    override fun initView(view: View) {
        messageText = view.findViewById(R.id.messageText)
    }

    override fun populateView(view: View) {
        messageText.text = model
    }

    /**
     * Overridden setter because we don't need it
     */
    override var contents: MutableList<IContent>? = null
        set(value) {}
    /**
     * Overridden setter because we don't need it
     */
    override var parent: IContent? = null
        set(value) {}
}