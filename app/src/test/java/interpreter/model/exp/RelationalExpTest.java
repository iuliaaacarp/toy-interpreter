package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.IValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RelationalExpTest {
    private MyDictionary<String, IValue> symTable;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
    }

    @Test
    void operatorsEvaluateToTrue() throws Exception {
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.LT, new ValueExp(new IntValue(2)), new ValueExp(new IntValue(5))).eval(symTable));
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.LE, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(5))).eval(symTable));
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.EQ, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(5))).eval(symTable));
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.NE, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(3))).eval(symTable));
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.GT, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(2))).eval(symTable));
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.GE, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(5))).eval(symTable));
    }

    @Test
    void operatorsEvaluateToFalse() throws Exception {
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.LT, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(2))).eval(symTable));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.LE, new ValueExp(new IntValue(6)), new ValueExp(new IntValue(5))).eval(symTable));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.EQ, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(3))).eval(symTable));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.NE, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(5))).eval(symTable));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.GT, new ValueExp(new IntValue(2)), new ValueExp(new IntValue(5))).eval(symTable));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.GE, new ValueExp(new IntValue(2)), new ValueExp(new IntValue(5))).eval(symTable));
    }

    @Test
    void nonIntegerLeftOperandThrowsException() {
        IExp exp = new RelationalExp(RelOp.LT, new ValueExp(new BoolValue(true)), new ValueExp(new IntValue(5)));
        var ex = assertThrows(ExpressionException.class, () -> exp.eval(symTable));
        assertTrue(ex.getMessage().contains("first operand is not an integer"));
    }

    @Test
    void nonIntegerRightOperandThrowsException() {
        IExp exp = new RelationalExp(RelOp.LT, new ValueExp(new IntValue(5)), new ValueExp(new BoolValue(true)));
        var ex = assertThrows(ExpressionException.class, () -> exp.eval(symTable));
        assertTrue(ex.getMessage().contains("second operand is not an integer"));
    }
}