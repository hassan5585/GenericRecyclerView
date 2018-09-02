package tech.mujtaba.genericrecyclerview.recyclerview.interfaces.contentcells

import tech.mujtaba.genericrecyclerview.recyclerview.interfaces.IModel

/**
 * Convenience interface that works with a model object and behaves like a child
 */
interface IContentChildWithModel<T> : IContentChild, IModel<T> {
}