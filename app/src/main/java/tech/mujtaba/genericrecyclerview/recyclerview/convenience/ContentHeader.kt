package tech.mujtaba.genericrecyclerview.recyclerview.convenience

import tech.mujtaba.genericrecyclerview.recyclerview.contractclasses.IContentChild
import tech.mujtaba.genericrecyclerview.recyclerview.contractclasses.IContentHeader


/**
 * Use this class instead of the IContentHeader to make sure whenever you
 * set a list of children, they automatically get assigned a parent
 */

@Deprecated("This is not needed anymore since all IContentChild classes" +
        " will have a parent anyway since it's not nullable",ReplaceWith("IContentHeader"))
abstract class ContentHeader : IContentHeader {

    override var children: MutableList<IContentChild> = mutableListOf()
        get() {
            field.forEach {
                it.parent = this
            }
          return field
        }
        set(value) {
            if (value != null) {
                field = value
                value.forEach {
                    it.parent = this
                }
            }
        }
}