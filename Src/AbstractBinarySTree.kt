import java.util.*

abstract class AbstractBinarySTree<T: Comparable<T>> : SortedSet<T> {
    open var root: Node<T>? = null

    override fun clear() {
        root = null
    }

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
        return search(element, root)
    }

    private fun search(t: T, root: Node<T>?): Boolean {
        return when {
            root == null -> false // element is not found
            t < root.value -> search(t, root.left) // Search left subtree
            t > root.value -> search(t, root.right) // Search right subtree
            else -> true
        } // element is found
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        if (elements.isEmpty()) return false
        for (`object` in elements) {
            if (!contains(`object`)) return false
        }
        return true
    }

    override fun remove(element: T): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Returns the first (lowest) element currently in this set.
     *
     * @return the first (lowest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    override fun first(): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Returns the last (highest) element currently in this set.
     *
     * @return the last (highest) element currently in this set
     * @throws NoSuchElementException if this set is empty
     */
    override fun last(): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Returns a view of the portion of this set whose elements range
     * from fromElement, inclusive, to toElement, exclusive
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Returns a view of the portion of this set whose elemenSare strictly less than toElement
     */
    override fun headSet(toElement: T): SortedSet<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Returns a view of the portion of this set whose elements are greater than or equal to fromElement
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun iterator(): MutableIterator<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}