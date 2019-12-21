import java.util.*
import kotlin.math.ceil
import kotlin.math.ln
import kotlin.math.max


open class ScapeGoatTree<T : Comparable<T>> : AbstractBinarySTree<T>(), CheckableSortedSet<T> {
    private var q = 0     //counter, q, that maintains an upper-bound on the number of nodes.

    override fun height(): Int = height(root)

    private fun height(node: Node<T>?): Int {
        if (node == null) return 0
        return 1 + max(height(node.left), height(node.right))
    }

    override fun checkInvariant(): Boolean =
            root?.let { checkInvariant(it) } ?: true

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

    /**
     * Adds the specified element to the set.
     *
     * @return `true` if the element has been added, `false` if the element is already contained in the set.
     */
    override fun add(element: T): Boolean {
        val newNode = Node(element)
        val depth = addWithDepth(newNode)
        val check = ln(q.toDouble()) / ln(3.0 / 2.0)

        if (depth > check) {

            /** depth exceeded, find scapegoat */
            var scapeG = newNode.parent
            while (3 * subTreeSize(scapeG) <= 2 * subTreeSize(scapeG!!.parent))
                scapeG = scapeG.parent
            scapeG = scapeG.parent

            val scapeGParent = scapeG?.parent
            val scapegoatOnParentsLeft = scapeGParent != null && scapeGParent.left == scapeG
            val rebuildSub = rebuildTree(subTreeSize(scapeG), scapeG!!)
            rebuildSub?.parent = scapeGParent

            if (scapeGParent != null) {
                if (scapegoatOnParentsLeft) {
                    scapeGParent.left = rebuildSub
                } else {
                    scapeGParent.right = rebuildSub
                }
            }
            if (scapeG == root) {
                root = rebuildSub
            }

        }
        return depth >= 0
    }

    private fun addWithDepth(newNode: Node<T>): Int {
        var current = root
        if (current == null) {
            root = newNode
            size++
            q++
            return 0
        }

        var done = false
        var depth = 0
        do {
            if (newNode.value < current!!.value) {
                if (current.left == null) {
                    current.left = newNode
                    newNode.parent = current
                    done = true
                } else {
                    current = current.left
                }
            } else if (newNode.value > current.value) {
                if (current.right == null) {
                    current.right = newNode
                    newNode.parent = current
                    done = true
                }
                current = current.right
            } else {
                return -1
            }
            depth++
        } while (!done)
        size++
        q++
        return depth
    }

    override fun addAll(elements: Collection<T>): Boolean {

        if (elements.isEmpty()) return false

        var modified = false
        for (element in elements) if (add(element)) modified = true
        return modified
    }

    override fun remove(element: T): Boolean {
        val test = super.remove(element)
        if (root == null)
            return false
        if (2 * size < q) {
            rebuildTree(size, root!!)
            q = size
        }
        return test
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        if (elements.isEmpty())
            return false
        var modified = false
        for (element in elements) {
            if (contains(element)) {
                super.remove(element)
                modified = true
            }
        }
        if (root != null)
            rebuildTree(size, root!!)

        return modified
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        if (elements.isEmpty())
            return false
        var modified = false
        for (element in this) {
            if (!elements.contains(element)) {
                super.remove(element)
                modified = true
            }
        }
        if (root != null)
            rebuildTree(size, root!!)
        return modified
    }

    /** Function to count number of nodes */
    private fun subTreeSize(node: Node<T>?): Int {
        return if (node == null) 0
        else 1 + subTreeSize(node.left) + subTreeSize(node.right)
    }

    private fun rebuildTree(size: Int, scapegoat: Node<T>): Node<T>? {
        val nodes: MutableList<Node<T>> = ArrayList()
        var currentNode: Node<T>? = scapegoat
        var done = false
        val stack = Stack<Node<T>>()

        while (!done) {
            if (currentNode != null) {
                stack.push(currentNode)
                currentNode = currentNode.left
            } else {
                if (!stack.isEmpty()) {
                    currentNode = stack.pop()
                    nodes.add(currentNode)
                    currentNode = currentNode.right
                } else {
                    done = true
                }
            }
        }
        // build tree from flattened list of nodes
        return buildTree(nodes, 0, size - 1)
    }

    /**
     * Build balanced tree from flattened tree.
     */
    private fun buildTree(nodes: List<Node<T>>, start: Int, end: Int): Node<T>? {
        val middle = ceil((start + end).toDouble() / 2.0).toInt()
        if (start > end) {
            return null
        }
        // middle becomes root of subtree instead of scapegoat
        val node: Node<T>? = nodes[middle]
        // recursively get left and right nodes
        val leftNode = buildTree(nodes, start, middle - 1)
        node!!.left = leftNode
        if (leftNode != null) {
            leftNode.parent = node
        }
        val rightNode = buildTree(nodes, middle + 1, end)
        node.right = rightNode
        if (rightNode != null) {
            rightNode.parent = node
        }
        return node
    }


    /** Function for InOrder traversal */
    fun inorderIterator(value: (T) -> Any) {
        inorderTraverse(value, root)
    }

    private fun inorderTraverse(value: (T) -> Any, node: Node<T>?) {
        if (node != null) {
            inorderTraverse(value, node.left)
            value.invoke(node.value)
            inorderTraverse(value, node.right)
        }
    }


    /** Function for PreOrder traversal */
    fun preOrderIterator(value: (T) -> Any) {
        preOrderTraverse(value, root)
    }

    private fun preOrderTraverse(value: (T) -> Any, node: Node<T>?) {
        if (node != null) {
            value.invoke(node.value)
            preOrderTraverse(value, node.left)
            preOrderTraverse(value, node.right)
        }
    }


    /** Function for PostOrder traversal */
    fun postOrderIterator(value: (T) -> Any) {
        postOrderTraverse(value, root)
    }

    private fun postOrderTraverse(value: (T) -> Any, node: Node<T>?) {
        if (node != null) {
            postOrderTraverse(value, node.left)
            postOrderTraverse(value, node.right)
            value.invoke(node.value)
        }
    }
}