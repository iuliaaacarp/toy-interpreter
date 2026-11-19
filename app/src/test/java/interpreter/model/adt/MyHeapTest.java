package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MyHeapTest {
    private IHeap heap;

    @BeforeEach
    void setUp() {
        heap = new MyHeap();
    }

    @Test
    void allocateReturnsIncrementalAddresses() {
        assertEquals(1, heap.allocate(new IntValue(10)));
        assertEquals(2, heap.allocate(new IntValue(20)));
        assertEquals(3, heap.allocate(new IntValue(30)));
    }

    @Test
    void isDefinedReturnsTrueForExistingAndFalseForMissing() {
        int addr = heap.allocate(new IntValue(10));
        assertTrue(heap.isDefined(addr));
        assertFalse(heap.isDefined(999));
    }

    @Test
    void updateModifiesExistingValue() throws ADTException {
        int addr = heap.allocate(new IntValue(10));
        heap.update(addr, new IntValue(20));
        assertEquals(new IntValue(20), heap.get(addr));
    }

    @Test
    void updateMissingAddressThrows() {
        assertThrows(ADTException.class, () -> heap.update(999, new IntValue(50)));
    }

    @Test
    void setContentAndGetContent() {
        heap.setContent(Map.of(1, new IntValue(100), 2, new IntValue(200)));
        assertEquals(new IntValue(100), heap.getContent().get(1));
        assertEquals(new IntValue(200), heap.getContent().get(2));
    }
}
