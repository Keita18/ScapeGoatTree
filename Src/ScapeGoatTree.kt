import kotlin.math.ln
import kotlin.math.max

class ScapeGoatTree<T : Comparable<T>> : AbstractBinarySTree<T>(), CheckableSortedSet<T>{
    override var root: Node<T>? = super.root
    override var nodesNumber = super.nodesNumber
    private var q = 0

    init {
        root = null
        nodesNumber = 0
    }



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
            nodesNumber++
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
        nodesNumber++
        q++
        return depth
    }



    override fun addAll(elements: Collection<T>): Boolean {
        if (elements.isEmpty())
            return false

        for (element in elements) {
            val test = add(element)
            if (!test)
                return false
        }
        return true
    }


    /** Function to count number of nodes */
    private fun subTreeSize(node: Node<T>?, checkHeight: Boolean = false): Int {
        if (node == null) return 0
        val leftNodeCount = subTreeSize(node.left)
        val rightNodeCount = subTreeSize(node.right)

        return 1 + if (!checkHeight)  leftNodeCount + rightNodeCount // subTreeSize
        else  max(leftNodeCount, rightNodeCount)    // height
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
    private fun buildBalanced(array: Array<Node<T>?>, index: Int, nodeSize: Int): Node<T>?{
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

}