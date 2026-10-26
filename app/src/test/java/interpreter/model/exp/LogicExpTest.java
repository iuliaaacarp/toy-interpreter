package interpreter.model.exp;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.IValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LogicExpTest {
    private IDictionary<String, IValue> symTable;

    private static IExp bool(boolean b) {
        return new ValueExp(new BoolValue(b));
    }

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
    }

    @Test
    void evaluatesAndCorrectly() throws MyException, ADTException {
        IExp e1 = new LogicExp(LogicOp.AND, bool(true), bool(true));
        assertEquals(new BoolValue(true), e1.eval(symTable));

        IExp e2 = new LogicExp(LogicOp.AND, bool(true), bool(false));
        assertEquals(new BoolValue(false), e2.eval(symTable));
    }

    @Test
    void evaluatesOrCorrectly() throws MyException, ADTException {
        IExp e1 = new LogicExp(LogicOp.OR, bool(false), bool(true));
        assertEquals(new BoolValue(true), e1.eval(symTable));

        IExp e2 = new LogicExp(LogicOp.OR, bool(false), bool(false));
        assertEquals(new BoolValue(false), e2.eval(symTable));
    }

    @Test
    void nonBooleanOperandThrows() {
        IExp intExp = new ValueExp(new IntValue(5));
        IExp e = new LogicExp(LogicOp.AND, bool(true), intExp);
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("second operand is not a boolean", ex.getMessage());
    }
}