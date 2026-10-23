package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MyDictionaryTest {
    private IDictionary<String, Integer> dictionary;

    @BeforeEach
    void setUp() {
        dictionary = new MyDictionary<>();
    }

    @Test
    void putAndLookupWorkCorrectly() throws ADTException {
        dictionary.put("a", 10);
        assertTrue(dictionary.isDefined("a"));
        assertEquals(10, dictionary.lookup("a"));
    }

    @Test
    void updateChangesExistingValue() throws ADTException {
        dictionary.put("a", 10);
        dictionary.update("a", 20);
        assertEquals(20, dictionary.lookup("a"));
    }

    @Test
    void lookupMissingKeyThrows() {
        var ex = assertThrows(ADTException.class, () -> dictionary.lookup("missing"));
        assertEquals("Key missing is not defined", ex.getMessage());
    }

    @Test
    void updateMissingKeyThrows() {
        assertThrows(ADTException.class, () -> dictionary.update("missing", 5));
    }
}