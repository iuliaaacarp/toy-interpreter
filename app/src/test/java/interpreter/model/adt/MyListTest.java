package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MyListTest {
    private IList<String> list;

    @BeforeEach
    void setUp() {
        list = new MyList<>();
    }

    @Test
    void addAndGetWorkCorrectly() throws ADTException {
        list.add("test1");
        list.add("test2");

        assertEquals("test1", list.get(0));
        assertEquals("test2", list.get(1));
    }

    @Test
    void sizeIncreasesWhenAddingElements() {
        assertEquals(0, list.size());
        list.add("test1");
        assertEquals(1, list.size());
    }

    @Test
    void indexOutOfBoundsThrows() {
        var ex = assertThrows(ADTException.class, () -> list.get(0));
        assertEquals("Index out of bounds", ex.getMessage());
    }
}
