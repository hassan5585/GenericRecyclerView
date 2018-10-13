package tech.mujtaba.genericrecyclerview.recyclerview.convenience

import android.view.View
import tech.mujtaba.genericrecyclerview.R
import tech.mujtaba.genericrecyclerview.recyclerview.contractclasses.IContentChild
import tech.mujtaba.genericrecyclerview.recyclerview.contractclasses.IContentHeader

class EmptyContentParent : IContentHeader {

    private companion object {
        const val VIEW_TYPE = 34387
    }

    override var children: MutableList<IContentChild> = mutableListOf()

    override fun getViewResource(): Int {
       return R.layout.empty_view
    }

    override fun getItemType(): Int {
        return VIEW_TYPE
    }

    override fun initView(view: View) {

    }

    override fun populateView(view: View) {
    }
}