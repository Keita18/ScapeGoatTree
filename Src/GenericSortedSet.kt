import java.util.*

class GenericSortedSet<T : Comparable<T>>(
        private val fromElement: T?,                 //up border
        private val toElement: T?,                  //bottom border
        private val delegate: AbstractBinarySTree<T>
) : AbstractMutableSet<T>(), SortedSet<T> {

    override fun comparator(): Comparator<in T>? = delegate.comparator()

    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        return GenericSortedSet(fromElement, toElement, delegate)
    }

    override fun headSet(toElement: T): SortedSet<T> {
        return GenericSortedSet(null, toElement, delegate)
    }

    override fun tailSet(fromElement: T): SortedSet<T> {
        return GenericSortedSet(fromElement, null, delegate)
    }

    override fun first(): T? {
        val iterator = delegate.iterator()
        var currentFirst: T? = null
        while (iterator.hasNext() && currentFirst == null) {
            val nextElement = iterator.next()
            if (checkBounds(nextElement) && this.contains(nextElement)) {
                currentFirst = nextElement
            }
        }
        return currentFirst
    }

    override fun last(): T? {
        val iterator = delegate.iterator()
        var currentLast: T? = null
        while (iterator.hasNext()) {
            val nextElement = iterator.next()
            if (checkBounds(nextElement) && this.contains(nextElement)) {
                currentLast = nextElement
            }
        }
        return currentLast
    }


    override operator fun contains(element: T): Boolean {
        if (!checkBounds(element))
            return false
        return delegate.contains(element)
    }

    override fun add(element: T): Boolean {
        if (checkBounds(element)) {
            delegate.add(element)
            return true
        } else throw IllegalArgumentException()
    }

    override fun remove(element: T): Boolean {
        if (contains(element))
            delegate.remove(element)
        else
            throw IllegalArgumentException()
        return true
    }

    override fun iterator(): MutableIterator<T> = object : MutableIterator<T> {
        private val delegate = this@GenericSortedSet.delegate.iterator()

        private var next: T? = null

        init {
            while (delegate.hasNext()) {
                val next = delegate.next()
                if (checkBounds(next)) {
                    this.next = next
                    break
                }
            }
        }

        override fun hasNext(): Boolean {
            return next != null
        }

        override fun next(): T {
            val result = next ?: throw NoSuchElementException()
            next = if (delegate.hasNext()) delegate.next() else null
            return result
        }

        override fun remove() {
            delegate.remove()
        }

    }

    private fun checkBounds(value: T): Boolean {
        return if (fromElement != null && toElement != null)
            toElement > value && fromElement <= value
        else if (fromElement == null)
            toElement!! > value
        else
            fromElement <= value
    }

    override val size: Int
        get() = delegate.count { checkBounds(it) }

}