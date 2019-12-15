import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import java.util.*

class ScapeGoatTreeTesT {
    private fun testAdd(create: () -> CheckableSortedSet<Int>) {
        val tree = create()
        Assertions.assertEquals(0, tree.size)
        Assertions.assertFalse(tree.contains(5))
        tree.add(10)
        tree.add(5)
        tree.add(7)
        tree.add(10)
        Assertions.assertEquals(3, tree.size)
        Assertions.assertTrue(tree.contains(5))
        tree.add(3)
        tree.add(1)
        tree.add(3)
        tree.add(4)
        Assertions.assertEquals(6, tree.size)
        Assertions.assertFalse(tree.contains(8))
        tree.add(8)
        tree.add(15)
        tree.add(15)
        tree.add(20)
        Assertions.assertEquals(9, tree.size)
        Assertions.assertTrue(tree.contains(8))
        Assertions.assertTrue(tree.checkInvariant())
        Assertions.assertEquals(1, tree.first())
        Assertions.assertEquals(20, tree.last())

        val random = Random().nextInt (100)
        tree.add(random)
        Assertions.assertEquals(10, tree.size)
        Assertions.assertTrue(tree.contains(random))
        Assertions.assertEquals(random, tree.last())
    }

    @Test
    fun testAddSGT() {
        testAdd { createKotlinTree() }
    }

    private fun <T : Comparable<T>> createKotlinTree(): CheckableSortedSet<T> = ScapeGoatTree()

    private fun testRemove(create: () -> CheckableSortedSet<Int>) {
        val random = Random()
        for (iteration in 1..100) {
            val list = mutableListOf<Int>()
            for (i in 1..20) {
                list.add(random.nextInt(100))
            }
            val binarySet = create()
            Assertions.assertFalse(binarySet.remove(42))
            for (element in list) {
                binarySet += element
            }
            val originalHeight = binarySet.height()
            val toRemove = list[random.nextInt(list.size)]
            val oldSize = binarySet.size

            Assertions.assertTrue(binarySet.remove(toRemove))
            Assertions.assertEquals(oldSize - 1, binarySet.size)
            println("Removing $toRemove from $list")
            for (element in list) {
                val inn = element != toRemove
                Assertions.assertEquals(
                        inn, element in binarySet,
                        "$element should be ${if (inn) "in" else "not in"} tree"
                )
            }
            list.remove(toRemove)

            Assertions.assertTrue(binarySet.checkInvariant(), "Binary tree invariant is false after tree.remove()")
            Assertions.assertTrue(
                    binarySet.height() <= originalHeight,
                    "After removal of $toRemove from $list binary tree height increased"
            )
        }
    }

    private fun testRemove2(create: () -> CheckableSortedSet<Int>) {
        val random = Random()
        val binarySet = create()
        val list = mutableSetOf<Int>()

        for (iteration in 1..100) {
            for (i in 1..20) {
                list.add(random.nextInt(100))
                binarySet.add(random.nextInt(100))
            }
        }

        val last = list.max()
        val first = list.min()
        Assertions.assertEquals(last, binarySet.last())
        Assertions.assertEquals(first, binarySet.first())

        binarySet.remove(last)
        Assertions.assertFalse(binarySet.contains(last))
        Assertions.assertTrue(binarySet.contains(first))

        var i = 0
        val oldSize = binarySet.size
        val myRandom = Random().nextInt(last!! / 2)
        for (j in 0 until binarySet.last() step myRandom)
            if (binarySet.contains(j)) {
                binarySet.remove(j)
                i++
                Assertions.assertTrue(list.contains(j))
            }
        Assertions.assertEquals(oldSize - i, binarySet.size)
    }

    @Test
    fun testRemoveSGT() {
        testRemove { createKotlinTree() }
        testRemove2 { createKotlinTree() }
    }

    private fun testIterator(create: () -> CheckableSortedSet<Int>) {
        val random = Random()
        for (iteration in 1..100) {
            val list = mutableListOf<Int>()
            for (i in 1..20) {
                list.add(random.nextInt(100))
            }
            val treeSet = TreeSet<Int>()
            val binarySet = create()
            Assertions.assertFalse(binarySet.iterator().hasNext(), "Iterator of empty set should not have next element")
            for (element in list) {
                treeSet += element
                binarySet += element
            }
            val treeIt = treeSet.iterator()
            val binaryIt = binarySet.iterator()
            println("Traversing $list")
            while (treeIt.hasNext()) {
                Assertions.assertEquals(treeIt.next(), binaryIt.next(), "Incorrect iterator state while iterating $treeSet")
            }
            val iterator1 = binarySet.iterator()
            val iterator2 = binarySet.iterator()
            println("Consistency check for hasNext $list")
            // hasNext call should not affect iterator position
            while (iterator1.hasNext()) {
                Assertions.assertEquals(
                        iterator2.next(), iterator1.next(),
                        "Call of iterator.hasNext() changes its state while iterating $treeSet"
                )
            }
        }
    }

    @Test
    fun testIteratorSGT() {
        testIterator { createKotlinTree() }
    }

    private fun testIteratorRemove(create: () -> CheckableSortedSet<Int>) {
        val random = Random()
        for (iteration in 1..100) {
            val list = mutableListOf<Int>()
            for (i in 1..20) {
                list.add(random.nextInt(100))
            }
            val treeSet = TreeSet<Int>()
            val binarySet = create()
            for (element in list) {
                treeSet += element
                binarySet += element
            }
            val toRemove = list[random.nextInt(list.size)]
            treeSet.remove(toRemove)
            println("Removing $toRemove from $list")
            val iterator = binarySet.iterator()
            var counter = binarySet.size
            while (iterator.hasNext()) {
                val element = iterator.next()
                counter--
                print("$element ")
                if (element == toRemove) {
                    iterator.remove()
                }
            }
            Assertions.assertEquals(
                    0, counter,
                    "Iterator.remove() of $toRemove from $list changed iterator position: " +
                            "we've traversed a total of ${binarySet.size - counter} elements instead of ${binarySet.size}"
            )
            println()
            Assertions.assertEquals(treeSet.size, binarySet.size, "Size is incorrect after removal of $toRemove from $list")
            for (element in list) {
                val inn = element != toRemove
                Assertions.assertEquals(
                        inn, element in binarySet,
                        "$element should be ${if (inn) "in" else "not in"} tree"
                )
            }
            Assertions.assertTrue(binarySet.checkInvariant(), "Binary tree invariant is false after tree.iterator().remove()")
        }
    }

    @Test
    fun testIteratorRemoveSGT() {
        testIteratorRemove { createKotlinTree() }
    }

}