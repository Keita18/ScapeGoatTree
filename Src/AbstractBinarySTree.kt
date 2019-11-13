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
            root == null -> false                         // element is not found
            t < root.value -> search(t, root.left)       // Search left subtree
            t > root.value -> search(t, root.right)     // Search right subtree
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
    /**
     * fun to remove element from tree
     */
    override fun remove(element: T): Boolean {
        if (contains(element)) {
            root = delete(root, element)
            return true
        }
        return false
    }

    private fun delete(currentRoot: Node<T>?, toDelete: T): Node<T> ?{
        when {
            currentRoot == null -> throw RuntimeException("cannot delete.")
            toDelete < currentRoot.value -> currentRoot.left = delete(currentRoot.left, toDelete)
            toDelete > currentRoot.value -> currentRoot.right = delete(currentRoot.right, toDelete)
            else -> when {
                currentRoot.left == null -> return currentRoot.right
                currentRoot.right == null -> return currentRoot.left
                else -> {
                    // get data from the rightmost node in the left subtree
                    currentRoot.value = retrieveData(currentRoot.left!!)
                    // delete the rightmost node in the left subtree
                    currentRoot.left = delete(currentRoot.left, currentRoot.value)
                }
            }
        }
        return currentRoot
    }
    private fun retrieveData(currentRoot: Node<T>): T {
        var p = currentRoot
        while (p.right != null) p = p.right!!

        return p.value
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        if (elements.isEmpty()) return false
        for (t in elements) {
            if (contains(t)) {
                remove(t)
            } else
                return false
        }
        return true
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        if (elements.isEmpty()) return false
        val list = ArrayList<T>()
        for (element in elements) {
            if (contains(element)) {
                list.add(element)
            }
        }
        return addAll(list)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun iterator(): MutableIterator<T> {
        return BinaryTreeIterator()
    }

    inner class BinaryTreeIterator internal constructor() : MutableIterator<T> {

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