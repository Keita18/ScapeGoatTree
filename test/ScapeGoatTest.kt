import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ScapeGoatTest : AbstractScapeGoatTreeTest() {

    @BeforeEach
    fun fillTree() {
        fillTree { ScapeGoatTree() }
    }

    @Test
    fun testAddSGT() {
        testAdd { createKotlinTree() }
    }

    @Test
    fun testRemoveSGT() {
        testRemove()
        testRemove2()
    }

    @Test
    fun testIteratorSGT() {
        testIterator()
    }

    @Test
    fun testIteratorRemoveSGT() {
        testIteratorRemove()
    }

}