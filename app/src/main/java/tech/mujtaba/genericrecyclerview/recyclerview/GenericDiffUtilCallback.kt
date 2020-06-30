package tech.mujtaba.genericrecyclerview.recyclerview

import androidx.recyclerview.widget.DiffUtil
import tech.mujtaba.genericrecyclerview.recyclerview.content.IContent

internal class GenericDiffUtilCallback(private val newList: List<IContent>, private val oldList: List<IContent> = listOf()) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].isSameItemAs(newList[newItemPosition])

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].isContentSameAs(newList[newItemPosition])
}