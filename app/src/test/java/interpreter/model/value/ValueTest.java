package interpreter.model.value;

import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValueTest {

    @Test
    void intValueReturnsCorrectType() {
        IValue intVal = new IntValue(5);
        assertEquals(new IntType(), intVal.getType());
    }

    @Test
    void boolValueReturnsCorrectType() {
        IValue boolVal = new BoolValue(true);
        assertEquals(new BoolType(), boolVal.getType());
    }

    @Test
    void valuesAreEqualByContent() {
        assertEquals(new IntValue(10), new IntValue(10));
        assertNotEquals(new IntValue(10), new IntValue(5));

        assertEquals(new BoolValue(true), new BoolValue(true));
        assertNotEquals(new BoolValue(true), new BoolValue(false));
    }
}