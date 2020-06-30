package tech.mujtaba.genericrecyclerview.recyclerview.actions

interface IClickable {

    /**
     * Will be automatically invoked when this cell is clicked. If you want to react to the click,
     * override this method
     */
    fun onClick(content : IClickable)
}