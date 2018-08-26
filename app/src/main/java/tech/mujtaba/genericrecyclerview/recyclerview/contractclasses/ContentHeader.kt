package tech.mujtaba.genericrecyclerview.recyclerview.contractclasses


/**
 * Use this class instead of the IContentHeader to make sure whenever you
 * set a list of children, they automatically get assigned a parent
 */
abstract class ContentHeader : IContentHeader {

    override var children: MutableList<IContentChild> = mutableListOf()
        set(value) {
            if (value != null) {
                field.clear()
                for (c in value) {
                    field.add(c)
                    c.parent = this
                }
            }
        }
}