
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class KotlinHeadSetTest : AbstractGenericSortedSetTest() {

    @BeforeEach
    fun fillTree() {
        fillTree { ScapeGoatTree() }
    }

    @Test
    @Tag("Normal")
    fun headSetTest() {
        doHeadSetTest()
    }

    @Test
    @Tag("Hard")
    fun headSetRelationTest() {
        doHeadSetRelationTest()
    }

    @Test
    @Tag("Normal")
    fun tailSetTest() {
        doTailSetTest()
    }

    @Test
    @Tag("Hard")
    fun tailSetRelationTest() {
        doTailSetRelationTest()
    }

    @Test
    @Tag("Impossible")
    fun subSetTest() {
        doSubSetTest()
        doSubSetRelationTest()
    }

    @Test
    @Tag("Impossible")
    fun genericSortedSetTest() {
        doTestGenericSortedSetIterator()
    }
}