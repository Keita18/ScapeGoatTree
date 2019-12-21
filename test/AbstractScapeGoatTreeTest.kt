import org.junit.jupiter.api.Assertions.*
import java.util.*

abstract class AbstractScapeGoatTreeTest {
    private lateinit var scapeGoatTree: CheckableSortedSet<Int>
    private val list = mutableListOf<Int>()

    protected fun fillTree(create: () -> CheckableSortedSet<Int>) {
        this.scapeGoatTree = create()

        val random = Random()
        for (iteration in 1..100) {
            for (i in 1..20) {
                list.add(random.nextInt(100))
            }
        }

        for (element in list) {
            scapeGoatTree.add(element)
        }
    }

    protected fun testAdd(create: () -> CheckableSortedSet<Int>) {
        val tree = create()
        val treeSet = TreeSet<Int>()
        assertEquals(0, tree.size)
        assertFalse(tree.contains(5))
        tree.add(10)
        tree.add(5)
        tree.add(7)
        tree.add(10)
        treeSet.addAll(tree)
        assertEquals(treeSet.size, tree.size)
        assertTrue(tree.contains(5))
        tree.add(3)
        tree.add(1)
        tree.add(3)
        tree.add(4)
        treeSet.addAll(tree)
        assertEquals(treeSet.size, tree.size)
        assertFalse(tree.contains(8))
        tree.add(8)
        tree.add(15)
        tree.add(15)
        tree.add(20)
        treeSet.addAll(tree)
        assertEquals(treeSet.size, tree.size)
        assertTrue(tree.contains(8))
        assertTrue(tree.checkInvariant())
        assertEquals(treeSet.first(), tree.first())
        assertEquals(treeSet.last(), tree.last())
    }

    protected fun <T : Comparable<T>> createKotlinTree(): CheckableSortedSet<T> = ScapeGoatTree()

    protected fun testRemove() {
        val random = Random()
        val treeSet = TreeSet<Int>()
        treeSet.addAll(list)
        val scapeGoatSet = scapeGoatTree
        val originalHeight = scapeGoatSet.height()
        val toRemove = list[random.nextInt(list.size)]
        val oldSize = treeSet.size

        assertEquals(treeSet.size, scapeGoatSet.size)
        assertTrue(scapeGoatSet.remove(toRemove))
        assertEquals(oldSize - 1, scapeGoatSet.size)
        println("Removing $toRemove from $list")
        for (element in treeSet) {
            val inn = element != toRemove
            assertEquals(
                    inn, element in scapeGoatSet,
                    "$element should be ${if (inn) "in" else "not in"} tree"
            )
        }
        treeSet.remove(toRemove)

        assertTrue(scapeGoatSet.checkInvariant(), "Binary tree invariant is false after tree.remove()")
        assertTrue(
                scapeGoatSet.height() <= originalHeight,
                "After removal of $toRemove from $list binary tree height increased"
        )
    }

    protected fun testRemove2() {
        val scapeGoatSet = scapeGoatTree
        val treeSet = TreeSet<Int>()
        treeSet.addAll(list)
        val last = treeSet.last()
        val first = treeSet.first()
        assertEquals(last, scapeGoatSet.last())
        assertEquals(first, scapeGoatSet.first())
        scapeGoatSet.remove(last)
        assertFalse(scapeGoatSet.contains(last))
        assertTrue(scapeGoatSet.contains(first))

        var i = 0
        val oldSize = scapeGoatSet.size
        val myRandom = Random().nextInt(last!! / 2)
        for (j in 0 until scapeGoatSet.last() step myRandom)
            if (scapeGoatSet.contains(j)) {
                scapeGoatSet.remove(j)
                i++
                assertTrue(treeSet.contains(j))
            }
        assertEquals(oldSize - i, scapeGoatSet.size)
    }

    protected fun testIterator() {
        val treeSet = TreeSet<Int>()
        treeSet.addAll(list)
        val scapeGoatSet = scapeGoatTree
        val treeIt = treeSet.iterator()
        val binaryIt = scapeGoatSet.iterator()

        println("Traversing $list")
        while (treeIt.hasNext()) {
            assertEquals(treeIt.next(), binaryIt.next(), "Incorrect iterator state while iterating $treeSet")
        }
        val iterator1 = scapeGoatSet.iterator()
        val iterator2 = scapeGoatSet.iterator()
        println("Consistency check for hasNext $list")
        // hasNext call should not affect iterator position
        while (iterator1.hasNext()) {
            assertEquals(
                    iterator2.next(), iterator1.next(),
                    "Call of iterator.hasNext() changes its state while iterating $treeSet"
            )
        }
    }

    protected fun testIteratorRemove() {
        val random = Random()
        val treeSet = TreeSet<Int>()
        treeSet.addAll(list)
        val scapeGoatSet = scapeGoatTree
        val toRemove = list[random.nextInt(list.size)]

        treeSet.remove(toRemove)
        println("Removing $toRemove from $list")

        val iterator = scapeGoatSet.iterator()
        var counter = scapeGoatSet.size
        while (iterator.hasNext()) {
            val element = iterator.next()
            counter--
            print("$element ")
            if (element == toRemove) {
                iterator.remove()
            }
        }
        assertEquals(
                0, counter,
                "Iterator.remove() of $toRemove from $list changed iterator position: " +
                        "we've traversed a total of ${scapeGoatSet.size - counter} elements instead of ${scapeGoatSet.size}"
        )
        println()
        assertEquals(treeSet.size, scapeGoatSet.size, "Size is incorrect after removal of $toRemove from $list")
        for (element in list) {
            val inn = element != toRemove
            assertEquals(
                    inn, element in scapeGoatSet,
                    "$element should be ${if (inn) "in" else "not in"} tree"
            )
        }
        assertTrue(scapeGoatSet.checkInvariant(), "Binary tree invariant is false after tree.iterator().remove()")
    }
}