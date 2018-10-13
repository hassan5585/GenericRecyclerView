package tech.mujtaba.genericrecyclerview.recyclerview

import tech.mujtaba.genericrecyclerview.recyclerview.contractclasses.IContent
import tech.mujtaba.genericrecyclerview.recyclerview.interfaces.IModel
import kotlin.reflect.KClass

class Extensions {
    fun <T : Any> IContent.isTypeOf(clazz: KClass<T>) : Boolean{
        if (this !is IModel<*>) return false
        this.model?.let {
            return it::class == clazz
        }
        return false
    }
}