import org.junit.jupiter.api.Assertions.*


import java.util.*
import kotlin.test.*

abstract class AbstractGenericSortedSetTest {
    private lateinit var tree: SortedSet<Int>
    private lateinit var randomTree: SortedSet<Int>
    private val randomTreeSize = 1000
    private val randomValues = mutableListOf<Int>()

    protected fun fillTree(create: () -> SortedSet<Int>) {
        this.tree = create()
        //В произвольном порядке добавим числа от 1 до 10
        tree.add(5)
        tree.add(1)
        tree.add(2)
        tree.add(7)
        tree.add(9)
        tree.add(10)
        tree.add(8)
        tree.add(4)
        tree.add(3)
        tree.add(6)

        this.randomTree = create()
        val random = Random()
        for (i in 0 until randomTreeSize) {
            val randomValue = random.nextInt(randomTreeSize) + 1
            if (randomTree.add(randomValue)) {
                randomValues.add(randomValue)
            }
        }
    }


    protected fun doHeadSetTest() {
        var set: SortedSet<Int> = tree.headSet(5)
        kotlin.test.assertEquals(true, set.contains(1))
        kotlin.test.assertEquals(true, set.contains(2))
        kotlin.test.assertEquals(true, set.contains(3))
        kotlin.test.assertEquals(true, set.contains(4))
        kotlin.test.assertEquals(false, set.contains(5))
        kotlin.test.assertEquals(false, set.contains(6))
        kotlin.test.assertEquals(false, set.contains(7))
        kotlin.test.assertEquals(false, set.contains(8))
        kotlin.test.assertEquals(false, set.contains(9))
        kotlin.test.assertEquals(false, set.contains(10))


        set = tree.headSet(127)
        for (i in 1..10)
            kotlin.test.assertEquals(true, set.contains(i))

    }

    protected fun doTailSetTest() {
        var set: SortedSet<Int> = tree.tailSet(5)
        kotlin.test.assertEquals(false, set.contains(1))
        kotlin.test.assertEquals(false, set.contains(2))
        kotlin.test.assertEquals(false, set.contains(3))
        kotlin.test.assertEquals(false, set.contains(4))
        kotlin.test.assertEquals(true, set.contains(5))
        kotlin.test.assertEquals(true, set.contains(6))
        kotlin.test.assertEquals(true, set.contains(7))
        kotlin.test.assertEquals(true, set.contains(8))
        kotlin.test.assertEquals(true, set.contains(9))
        kotlin.test.assertEquals(true, set.contains(10))

        set = tree.tailSet(-128)
        for (i in 1..10)
            kotlin.test.assertEquals(true, set.contains(i))

    }

    protected fun doHeadSetRelationTest() {
        val set: SortedSet<Int> = tree.headSet(7)
        kotlin.test.assertEquals(6, set.size)
        kotlin.test.assertEquals(10, tree.size)
        tree.add(0)
        kotlin.test.assertTrue(set.contains(0))
        set.add(-2)
        kotlin.test.assertTrue(tree.contains(-2))
        tree.add(12)
        kotlin.test.assertFalse(set.contains(12))
        assertFailsWith<IllegalArgumentException> { set.add(8) }
        kotlin.test.assertEquals(8, set.size)
        kotlin.test.assertEquals(13, tree.size)
    }

    protected fun doTailSetRelationTest() {
        val set: SortedSet<Int> = tree.tailSet(4)
        kotlin.test.assertEquals(7, set.size)
        kotlin.test.assertEquals(10, tree.size)
        tree.add(12)
        kotlin.test.assertTrue(set.contains(12))
        set.add(42)
        kotlin.test.assertTrue(tree.contains(42))
        tree.add(0)
        kotlin.test.assertFalse(set.contains(0))
        assertFailsWith<IllegalArgumentException> { set.add(-2) }
        kotlin.test.assertEquals(9, set.size)
        kotlin.test.assertEquals(13, tree.size)
    }

    protected fun doSubSetTest() {
        val smallSet: SortedSet<Int> = tree.subSet(3, 8)
        kotlin.test.assertEquals(false, smallSet.contains(1))
        kotlin.test.assertEquals(false, smallSet.contains(2))
        kotlin.test.assertEquals(true, smallSet.contains(3))
        kotlin.test.assertEquals(true, smallSet.contains(4))
        kotlin.test.assertEquals(true, smallSet.contains(5))
        kotlin.test.assertEquals(true, smallSet.contains(6))
        kotlin.test.assertEquals(true, smallSet.contains(7))
        kotlin.test.assertEquals(false, smallSet.contains(8))
        kotlin.test.assertEquals(false, smallSet.contains(9))
        kotlin.test.assertEquals(false, smallSet.contains(10))

        assertFailsWith<IllegalArgumentException> { smallSet.add(2) }
        assertFailsWith<IllegalArgumentException> { smallSet.add(9) }

        val allSet = tree.subSet(-128, 128)
        for (i in 1..10)
            kotlin.test.assertEquals(true, allSet.contains(i))

        val random = Random()
        val toElement = random.nextInt(randomTreeSize) + 1
        val fromElement = random.nextInt(toElement - 1) + 1

        val randomSubset = randomTree.subSet(fromElement, toElement)
        randomValues.forEach { element ->
            kotlin.test.assertEquals(element in fromElement until toElement, randomSubset.contains(element))
        }
    }

    protected fun doSubSetRelationTest() {
        val set: SortedSet<Int> = tree.subSet(2, 15)
        kotlin.test.assertEquals(9, set.size)
        kotlin.test.assertEquals(10, tree.size)
        tree.add(11)
        kotlin.test.assertTrue(set.contains(11))
        set.add(14)
        kotlin.test.assertTrue(tree.contains(14))
        tree.add(0)
        kotlin.test.assertFalse(set.contains(0))
        tree.add(15)
        kotlin.test.assertFalse(set.contains(15))
        assertFailsWith<IllegalArgumentException> { set.add(1) }
        assertFailsWith<IllegalArgumentException> { set.add(20) }
        kotlin.test.assertEquals(11, set.size)
        kotlin.test.assertEquals(14, tree.size)
    }

    protected fun doTestGenericSortedSetIterator() {
        val scapeGoatTree = ScapeGoatTree<Int>()
        val ktSortedSetTail = sortedSetOf<Int>()
        val ktSortedSetHead = sortedSetOf<Int>()
        val ktSortedSetSubset = sortedSetOf<Int>()

        val fromElement = (1..24).random()
        val toElement = (200..254).random()
        for (i in 0..255 step fromElement) {
            scapeGoatTree.add(i)
            if (i < toElement)
                ktSortedSetHead.add(i)
            if (i in fromElement until toElement)
                ktSortedSetSubset.add(i)
            if (i >= fromElement)
                ktSortedSetTail.add(i)
        }
        println("toEle $toElement and fromEl $fromElement")
        val ktBinaryTreeTail = scapeGoatTree.tailSet(toElement)
        val subsetKtBinaryTree = scapeGoatTree.subSet(fromElement, toElement)
        val headKtBinaryTree = scapeGoatTree.headSet(toElement)

        kotlin.test.assertEquals(ktSortedSetHead, headKtBinaryTree)
        kotlin.test.assertEquals(ktSortedSetSubset, subsetKtBinaryTree)
        assertEquals(ktBinaryTreeTail, ktBinaryTreeTail)
        kotlin.test.assertEquals(ktSortedSetHead.last(), headKtBinaryTree.last())
        kotlin.test.assertEquals(ktSortedSetTail.last(), ktBinaryTreeTail.last())
        kotlin.test.assertEquals(subsetKtBinaryTree.size, ktSortedSetSubset.size)
        assertTrue(
                subsetKtBinaryTree.contains(ktSortedSetSubset.max())
        )

        val random = Random()
        for (iteration in 1..100) {
            val list = mutableListOf<Int>()
            for (i in 1..20) {
                list.add(random.nextInt(100))
            }
            val treeSet = sortedSetOf<Int>()
            val sgtSubset = ScapeGoatTree<Int>().subSet(list.min()!!, list.max()!! + 1)
            val test = sgtSubset.iterator().hasNext()
            kotlin.test.assertFalse(test, "Iterator of empty set should not have next element")
            for (element in list) {
                treeSet += element
                sgtSubset += element
            }
            val treeIt = treeSet.iterator()
            val binaryIt = sgtSubset.iterator()
            println("Traversing $list")
            while (treeIt.hasNext()) {
                assertEquals(treeIt.next(), binaryIt.next(), "Incorrect iterator state while iterating $treeSet")
            }
            val iterator1 = sgtSubset.iterator()
            val iterator2 = sgtSubset.iterator()
            println("Consistency check for hasNext $list")
            // hasNext call should not affect iterator position
            while (iterator1.hasNext()) {
                assertEquals(
                        iterator2.next(), iterator1.next(),
                        "Call of iterator.hasNext() changes its state while iterating $treeSet"
                )
            }
        }
    }


}