package tech.mujtaba.genericrecyclerview.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import tech.mujtaba.genericrecyclerview.R
import tech.mujtaba.genericrecyclerview.recyclerview.content.IContent
import tech.mujtaba.genericrecyclerview.recyclerview.actions.IClickable
import java.util.concurrent.Executors

class GenericRecyclerView @JvmOverloads constructor(context: Context,
                                                    attrs: AttributeSet? = null,
                                                    defStyle: Int = 0) : RecyclerView(context, attrs) {

    val externalList: List<IContent>
        get() = internalList
    private var internalList: MutableList<IContent> = mutableListOf()
    private var adapter: GenericAdapter? = null

    // An empty view when there is nothing provided to the recyclerview to show
    private var emptyViewResId: Int = 0

    private val emptyIContent by lazy {
        EmptyIContent(emptyViewResId)
    }

    private val coroutineDispatcher = Executors.newFixedThreadPool(1).asCoroutineDispatcher()

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.GenericRecyclerView)
            emptyViewResId = typedArray.getResourceId(R.styleable.GenericRecyclerView_emptyView, 0)
            typedArray.recycle()
            setList(listOf(emptyIContent))
        }
    }


    /**
     * This view will not let you set any other adapter other than a Generic Adapter
     */
    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter is GenericAdapter) {
            super.setAdapter(adapter)
        } else throw ListException(ListException.EXCEPTION_WRONG_ADAPTER)
    }

    fun setList(providedList: List<IContent>?) {
        providedList?.let {
            if (it.isEmpty() && emptyViewResId != 0) {
                setList(listOf(emptyIContent))
            } else GlobalScope.launch(coroutineDispatcher) {
                try {
                    val diffResult = DiffUtil.calculateDiff(GenericDiffUtilCallback(it, externalList))
                    GlobalScope.launch(Dispatchers.Main) {
                        if (adapter == null) {
                            adapter = GenericAdapter()
                            setAdapter(adapter)
                        }
                        internalList = providedList.toMutableList()
                        diffResult.dispatchUpdatesTo(adapter!!)
                    }
                } catch (exception: Exception) {
                    // Do Nothing
                }
            }
        }
    }

    /**
     * Use this function to scroll to the position of any IContent object in the list
     * TODO Add smooth scrolling
     */
    fun scrollToContent(content: IContent, isSmooth: Boolean = false) {
        if (!isSmooth) {
            when (layoutManager) {
                is LinearLayoutManager -> (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(internalList.indexOf(content), 0)
                is GridLayoutManager -> (layoutManager as GridLayoutManager).scrollToPositionWithOffset(internalList.indexOf(content), 0)
                else -> layoutManager?.scrollToPosition(internalList.indexOf(content))
            }
        } else {
            // TODO Add smooth scrolling code here
            when (layoutManager) {

            }
        }
    }


    /**
     * If you want to provide a predicate to start the scroll, use this function. It scrolls to the first
     * value that matches that predicate and ignores the rest
     */
    fun scrollToContent(filterPredicate: ((content: IContent) -> Boolean)?, isSmooth: Boolean = false) {
        filterPredicate?.let {
            for (c in internalList) {
                if (it(c)) {
                    scrollToContent(c, isSmooth)
                    break
                }
            }
        }
    }

    private inner class GenericAdapter : Adapter<GenericViewHolder>() {

        /**
         * If a viewInflateId is null in this method, it will cause a crash
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
            val firstItemContent = internalList.firstOrNull { it.getItemType() == viewType }
            val viewInflateId = firstItemContent?.getViewResource()
            return GenericViewHolder(LayoutInflater.from(context).inflate(viewInflateId
                    ?: 0, parent, false),
                    firstItemContent is IClickable)
        }

        override fun getItemViewType(position: Int): Int {
            return internalList[position].getItemType()
        }

        override fun getItemCount(): Int {
            return internalList.size
        }

        override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
            holder.setData(internalList[position])
        }
    }


    private class GenericViewHolder(itemView: View, isClickable: Boolean) : ViewHolder(itemView) {

        private var content: IContent? = null
        private val clickListener: OnClickListener by lazy {
            OnClickListener {
                (content as? IClickable)?.run {
                    onClick(this)
                }
            }
        }

        init {
            if (isClickable) {
                itemView.setOnClickListener(clickListener)
            }
        }

        fun setData(content: IContent) {
            this.content = content
            this.content?.populateView(itemView)
        }
    }

    private class ListException(exception: String) : Exception(exception) {

        companion object {
            const val EXCEPTION_WRONG_ADAPTER = "This RecyclerView cannot work with any adapter other than a GenericAdapter"
        }
    }
}