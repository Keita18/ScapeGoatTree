import java.util.*

abstract class AbstractBinarySTree<T : Comparable<T>> : SortedSet<T> {
    var root: Node<T>? = null

    override fun clear() {
        root = null
    }

    override var size: Int = 0

    /**
     * Return true if tree isEmpty or false
     */
    override fun isEmpty(): Boolean {
        return root == null
    }

    /**
     * Return true if tree contain element or false
     */
    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    fun find(value: T): Node<T>? =
            root?.let { find(it, value) }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        if (elements.isEmpty()) return false
        for (element in elements) {
            if (!contains(element)) return false
        }
        return true
    }

    /**
     * fun to remove element from tree
     */
    override fun remove(element: T): Boolean {
        val toDelete: Node<T> = find(element) ?: return false
        if (toDelete.value != element)
            return false
        if (toDelete.left == null || toDelete.right == null)
            splice(toDelete)
        else {
            var minRight = toDelete.right

            while (minRight!!.left != null)
                minRight = minRight.left

            toDelete.value = minRight.value
            splice(minRight)
        }
        return true
    }

    private fun splice(toDelete: Node<T>) {
        val parent: Node<T>?
        var change: Node<T>? = null
        when {
            toDelete.left != null -> change = toDelete.left!!
            toDelete.right != null -> change = toDelete.right!!
        }
        if (toDelete == root) {
            root = change
            parent = null
        } else {
            parent = toDelete.parent
            when (toDelete) {
                parent?.left -> parent.left = change
                parent?.right -> parent.right = change
            }
        }
        if (change != null)
            change.parent = parent
        size--
    }

    /**
     * Returns the first (lowest) element currently in this set.
     *
     * @return the first (lowest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    override fun first(): T {
        var node = root ?: throw NoSuchElementException()
        while (node.left != null)
            node = node.left!!
        return node.value
    }

    /**
     * Returns the last (highest) element currently in this set.
     *
     * @return the last (highest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    override fun last(): T {
        var node = root ?: throw NoSuchElementException()
        while (node.right != null)
            node = node.right!!
        return node.value
    }

    /**
     * Returns a view of the portion of this set whose elements range
     * from fromElement, inclusive, to toElement, exclusive
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        return GenericSortedSet(fromElement, toElement, this)
    }

    /**
     * Returns a view of the portion of this set whose elemenSare strictly less than toElement
     */
    override fun headSet(toElement: T): SortedSet<T> {
        return GenericSortedSet(null, toElement, this)
    }

    /**
     * Returns a view of the portion of this set whose elements are greater than or equal to fromElement
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        return GenericSortedSet(fromElement, null, this)
    }

    /**
     * Returns the comparator used to order the elements in this set,
     * or <tt>null</tt> if this set uses the [ natural ordering][Comparable] of its elements.
     *
     * @return the comparator used to order the elements in this set,
     * or <tt>null</tt> if this set uses the natural ordering
     * of its elements
     */
    override fun comparator(): Comparator<in T>? {
        return null
    }

    override fun iterator(): MutableIterator<T> {
        return BinaryTreeIterator(root)
    }

    inner class BinaryTreeIterator internal constructor(root: Node<T>?) : MutableIterator<T> {

        private var stack: Stack<Node<T>>

        init {
            var node = root
            stack = Stack()
            while (node != null) {
                stack.push(node)
                node = node.left
            }
        }

        /**
         * Check for the following element
         */
        override fun hasNext(): Boolean {
            return stack.isNotEmpty()
        }

        /**
         * Search for the next element
         */
        private var result: T? = null

        override fun next(): T {
            var node = stack.pop()
            result = node.value
            if (node.right != null) {
                node = node.right
                while (node != null) {
                    stack.push(node)
                    node = node.left
                }
            }
            return result!!
        }

        /**
         * Delete the next element
         */
        override fun remove() {
            this@AbstractBinarySTree.remove(result)
        }
    }
}