package tech.mujtaba.genericrecyclerview.recyclerview.interfaces

/**
 * Defines an object that can be selected and can maintain that state
 */
interface ISelectable {

    var isSelected : Boolean

    /**
     * Override this method to provide an implementation of
     * what should happen if this object is in a selected state
     * and vice versa
     */
    fun handleSelectedState()
}