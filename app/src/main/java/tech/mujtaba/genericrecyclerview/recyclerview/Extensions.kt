package tech.mujtaba.genericrecyclerview.recyclerview

import tech.mujtaba.genericrecyclerview.recyclerview.interfaces.contentcells.IContent
import tech.mujtaba.genericrecyclerview.recyclerview.interfaces.IModel
import kotlin.reflect.KClass

class Extensions {

    /**
     * Call this method to find out whether the IContent object is an IModel as well of
     * the right type
     * @param clazz The Kotlin class of the type you want the IModel to be
     */
    fun <T : Any> IContent.isTypeOf(clazz: KClass<T>) : Boolean{
        if (this !is IModel<*>) return false
        model?.let {
            return it::class == clazz
        }
        return false
    }

    /**
     * Don't call this method unless you have checked the type using isTypeOf function
     * @throws ClassCastException
     */
    fun <T : Any> IContent.getModel() : T{
        return ((this as IModel<*>).model) as T
    }

    /**
     * Convenience method to flatten a list
     */
    fun List<IContent>.flatten() : List<IContent>{
        return IContent.flattenList(this)
    }

    /**
     * Convenience method to unflatten a list
     */
    fun List<IContent>.unflatten(): List<IContent> {
        return IContent.unFlattenList(this)
    }
}