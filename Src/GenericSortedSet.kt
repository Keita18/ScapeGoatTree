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

    /**
     * Time Complexity O(n) in worst cage
     * Memory Complexity O(1)
     */
    override fun first(): T? {
        if (fromElement == null)
            return delegate.first()
        var root = delegate.root
        var currentFirst: T? = null
        while (root != null) {
            if (checkInRange(root.value)) {
                currentFirst = root.value
                break
            }


            root = if (root.value > fromElement)
                root.left
            else root.right

        }
        return currentFirst
    }

    /**
     * Time Complexity in worst case O(n-1) -> O(n) - n number of element in KtBinary
     * if toElement == KtBinary.last
     * Memory Complexity O(1)
     */
    override fun last(): T? {
        if (toElement == null)
            return delegate.last()
        var root = delegate.root
        var currantLast = root?.value

        while (root != null) {
            if (checkInRange(root.value)) {
                currantLast = root.value
            }

            root = if (root.value > toElement) root.left
            else root.right
        }
        return currantLast
    }

    /**
     * contains, add, remove
     * Time Complexity O(h) - h height of tree
     * Memory Complexity O(1)
     */
    override operator fun contains(element: T): Boolean {
        if (!checkInRange(element))
            return false
        return delegate.contains(element)
    }

    override fun add(element: T): Boolean {
        if (checkInRange(element)) {
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
        private val delegate = this@GenericSortedSet.delegate

        val commonParent = if (fromElement != null && toElement != null) lca(delegate.root, fromElement, toElement)
        else if (toElement == null) lca(delegate.root, fromElement!!, delegate.last())
        else lca(delegate.root, delegate.first(), toElement)

        /** Function to find LCA of node1 and node2 */
        fun lca(node: Node<T>?, fromElement: T, toElement: T): Node<T>? {
            if (node == null) return null

            // If (node1 and node2) < root -> we check left
            if (node.value > fromElement && node.value > toElement) return lca(node.left, fromElement, toElement)

            // If (node1 and node2) > root -> we check left
            return if (node.value < fromElement && node.value < toElement) lca(node.right, fromElement, toElement)
            else node
        }

        private val iterator = delegate.BinaryTreeIterator(commonParent)
        private var next: T? = null

        init {
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (checkInRange(next)) {
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
            next = if (iterator.hasNext()) {
                val nextElement = iterator.next()
                if (checkInRange(nextElement))
                    nextElement
                else null
            } else null
            return result
        }

        override fun remove() {
            iterator.remove()
        }
    }

    private fun checkInRange(value: T): Boolean {
        return if (fromElement != null && toElement != null)
            toElement > value && fromElement <= value
        else if (fromElement == null)
            toElement!! > value
        else
            fromElement <= value
    }

    /**
     * Time Complexity O(n) - n element in binary
     * Memory Complexity O(1)
     */
    override val size: Int
        get() = delegate.count { checkInRange(it) }

}