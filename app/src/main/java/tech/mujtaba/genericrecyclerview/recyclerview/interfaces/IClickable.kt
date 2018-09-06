package tech.mujtaba.genericrecyclerview.recyclerview.interfaces

interface IClickable {

    /**
     * Allows you to provide a click listener when building an object
     * and the listener will be notified of any clicks on this object
     */
    val onClickListener : IClickListener?

    /**
     * Will be automatically invoked when this cell is clicked. If you want to react to the click,
     * override this method
     */
    fun onClick() {
        onClickListener?.onClick(this)
    }


    interface IClickListener {
        fun onClick(content : IClickable)
    }

}