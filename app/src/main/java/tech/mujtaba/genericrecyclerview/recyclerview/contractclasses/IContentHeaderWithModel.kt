package tech.mujtaba.genericrecyclerview.recyclerview.contractclasses

/**
 * Convenience interface that works with a model object and behaves like a parent
 */
interface IContentHeaderWithModel<T> : IContentHeader, IModel<T>{
}