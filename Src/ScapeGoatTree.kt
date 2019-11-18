class ScapeGoatTree<T : Comparable<T>> : AbstractBinarySTree<T>(){
    override var root: Node<T>? = super.root
    private var nodesNumber = 0
    private var q = 0

    init {
        root = null
        nodesNumber = 0
    }

    override val size: Int
        get() = nodesNumber


    /**
     * Adds the specified element to the set.
     *
     * @return `true` if the element has been added, `false` if the element is already contained in the set.
     */
    override fun add(element: T): Boolean {
        val closest = super.find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                closest.left = newNode
                newNode.parent = closest
            }
            else -> {
                assert(closest.right == null)
                closest.right = newNode
                newNode.parent = closest
            }
        }
        nodesNumber++
        return true
    }

    override fun addAll(elements: Collection<T>): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}