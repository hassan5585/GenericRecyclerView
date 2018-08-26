package tech.mujtaba.genericrecyclerview.recyclerview.contractclasses

/**
 * Convenience interface that works with a model object and behaves like a child
 */
interface IContentChildWithModel<T> : IContentChild, IModel<T> {
}