
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class KotlinHeadSetTest : AbstractGenericSortedSetTest() {

    @BeforeEach
    fun fillTree() {
        fillTree { ScapeGoatTree() }
    }

    @Test
    fun headSetTest() {
        doHeadSetTest()
    }

    @Test
    fun headSetRelationTest() {
        doHeadSetRelationTest()
    }

    @Test
    fun tailSetTest() {
        doTailSetTest()
    }

    @Test
    fun tailSetRelationTest() {
        doTailSetRelationTest()
    }

    @Test
    fun subSetTest() {
        doSubSetTest()
        doSubSetRelationTest()
    }

    @Test
    fun genericSortedSetTest() {
        doTestGenericSortedSetIterator()
    }
}