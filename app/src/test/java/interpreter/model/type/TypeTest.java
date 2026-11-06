package interpreter.model.type;

import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TypeTest {

    @Test
    void intTypeProvidesCorrectDefaultValue() {
        IType intType = new IntType();
        assertEquals(new IntValue(0), intType.defaultValue());
    }

    @Test
    void boolTypeProvidesCorrectDefaultValue() {
        IType boolType = new BoolType();
        assertEquals(new BoolValue(false), boolType.defaultValue());
    }

    @Test
    void stringTypeProvidesCorrectDefaultValue() {
        assertEquals(new StringValue(""), new StringType().defaultValue());
    }

    @Test
    void typesAreEqualByValue() {
        assertEquals(new IntType(), new IntType());
        assertEquals(new BoolType(), new BoolType());
        assertEquals(new StringType(), new StringType());
        assertNotEquals(new IntType(), new BoolType());
        assertNotEquals(new BoolType(), new StringType());
        assertNotEquals(new StringType(), new IntType());
    }
}