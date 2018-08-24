package tech.mujtaba.genericrecyclerview.recyclerview

/**
 * A model interface that provides you an immutable value. This can be used with IContent
 * to tie it down with an object
 */
interface IModel<T> {
    val model : T


    fun compareTo(providedModel: Any?) : Boolean{
        if(providedModel == null) return false
        if(providedModel !is IModel<*>) return false
        if(providedModel.model == null) return false
        return try {
            model == ((providedModel.model) as IModel<T>)
        } catch (e: ClassCastException) {
            false
        }
    }
}