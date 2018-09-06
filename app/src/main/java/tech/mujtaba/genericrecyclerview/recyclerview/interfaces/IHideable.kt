package tech.mujtaba.genericrecyclerview.recyclerview.interfaces

import android.view.View

/**
 * Defines a view that is hideable. The recyclerview inflating this view
 * will taker care of providing it the view reference. Any implementing class can
 * just call the visibility method
 */
interface IHideable {

    /**
     * When overriding this property, lateinit it, and the recyclerview
     * will take care of initializing it
     */
    var view : View

    /**
     * A boolean that tells you whether this property is hidden or not. Do not set this
     * value directly. This is meant to be used only as a getter property
     */
    var isHidden : Boolean

    /**
     * Use this method to change the objects visibility
     * @param shouldHide Boolean that decides whether to show or hide this view
     */
    fun visibility(shouldHide : Boolean) {
        isHidden = shouldHide
        handleVisibility()
    }

    /**
     * Convenience method to toggle the visibility
     */
    fun toggle() {
        isHidden = !isHidden
        handleVisibility()
    }

    /**
     * Override this method to give the view another
     * visiblity(other than GONE). E.g. You can return View.Invisible here
     */
    fun hideMode() : Int {
        return View.GONE
    }

    /**
     * You don't have to call this method. The recyclerview does this for you
     */
    fun handleVisibility() {
        if (isHidden) {
            view.visibility = hideMode()
        }else view.visibility = View.VISIBLE
    }
    
}