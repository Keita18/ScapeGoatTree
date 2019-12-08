import java.util.*
import kotlin.math.ln
import kotlin.math.max

class ScapeGoatTree<T : Comparable<T>> : AbstractBinarySTree<T>(), CheckableSortedSet<T>{
    override var root: Node<T>? = super.root
    private var q = 0     //counter, q, that maintains an upper-bound on the number of nodes.

    override var size: Int
        get() = super.size
        set(value) {}


    override fun height(): Int = subTreeSize(root, true)

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
            rebuild(scapeG.parent)
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
        if (elements.isEmpty())
            return false
        else {
            for (element in elements) {
                add(element)
            }
        }
        return true
    }

    override fun remove(element: T): Boolean {
        val test = super.remove(element)
        if(!test)
            return false
        if (2*size < q) {
            rebuild(root)
            q = size
        }
        return true
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        if (elements.isEmpty()) return false
        for (element in elements) {
            if (contains(element)) {
                remove(element)
            } else
                return false
        }
        return true
    }


    /** Function to count number of nodes */
    private fun subTreeSize(node: Node<T>?, checkHeight: Boolean = false): Int {
        if (node == null) return 0
        val leftNodeCount = subTreeSize(node.left)
        val rightNodeCount = subTreeSize(node.right)

        return 1 + if (!checkHeight) leftNodeCount + rightNodeCount // subTreeSize
        else max(leftNodeCount, rightNodeCount)    // height
    }

    /**Function to rebuild tree from node u */
    private fun rebuild(node: Node<T>?) {
        val nodeSize = subTreeSize(node)
        val parent = node!!.parent
        val array = arrayOfNulls<Node<T>?>(nodeSize)
        packIntoArray(node, array, 0)
        when {
            parent == null -> {
                root = buildBalanced(array, 0, nodeSize)
                root!!.parent = null
            }
            parent.right == node -> {
                parent.right = buildBalanced(array, 0, nodeSize)
                parent.right!!.parent = parent
            }
            else -> {
                parent.left = buildBalanced(array, 0, nodeSize)
                parent.left!!.parent = parent
            }
        }
    }

    /** Function to packIntoArray */
    private fun packIntoArray(node: Node<T>?, array: Array<Node<T>?>, index: Int): Int {
        var i = index
        if (node == null) {
            return i
        }
        i = packIntoArray(node.left, array, i)
        array[i++] = node
        return packIntoArray(node.right, array, i)
    }

    /** Function to build balanced nodes */
    private fun buildBalanced(array: Array<Node<T>?>, index: Int, nodeSize: Int): Node<T>? {
        if (nodeSize == 0)
            return null
        val m = nodeSize / 2
        array[index + m]?.left = buildBalanced(array, index, m)
        if (array[index + m]?.left != null)
            array[index + m]?.left!!.parent = array[index + m]
        array[index + m]?.right = buildBalanced(array, index + m + 1, nodeSize - m - 1)
        if (array[index + m]?.right != null)
            array[index + m]?.right!!.parent = array[index + m]
        return array[index + m]
    }

    /** Function for InOrder traversal */
    fun inorderIterator(): Iterator<T>? {
        val queue: Queue<T> = LinkedList<T>()
        inorderTraverse(queue, root)
        return queue.iterator()
    }

    private fun inorderTraverse(queue: Queue<T>, node: Node<T>?) {
        if (node != null) {
            inorderTraverse(queue, node.left)
            queue.add(node.value)
            inorderTraverse(queue, node.right)
        }
    }

    /** Function for PreOrder traversal */
    fun preOrderIterator(): Iterator<T>? {
        val queue: Queue<T> = LinkedList<T>()
        preOrderTraverse(queue, root)
        return queue.iterator()
    }

    private fun preOrderTraverse(queue: Queue<T>, node: Node<T>?) {
        if (node != null) {
            preOrderTraverse(queue, node.left)
            queue.add(node.value)
            preOrderTraverse(queue, node.right)
        }
    }


    /** Function for PostOrder traversal */
    fun postOrderIterator(): Iterator<T>? {
        val queue: Queue<T> = LinkedList<T>()
        postOrderTraverse(queue, root)
        return queue.iterator()
    }

    private fun postOrderTraverse(queue: Queue<T>, node: Node<T>?) {
        if (node != null) {
            postOrderTraverse(queue, node.left)
            postOrderTraverse(queue, node.right)
            queue.add(node.value)
        }
    }
}