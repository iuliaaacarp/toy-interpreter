package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.IValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RelationalExpTest {
    private MyDictionary<String, IValue> symTable;
    private IHeap heap;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
        heap = new MyHeap();
    }

    @Test
    void operatorsEvaluateToTrue() throws Exception {
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.LT, new ValueExp(new IntValue(2)), new ValueExp(new IntValue(5))).eval(symTable, heap));
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.LE, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(5))).eval(symTable, heap));
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.EQ, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(5))).eval(symTable, heap));
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.NE, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(3))).eval(symTable, heap));
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.GT, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(2))).eval(symTable, heap));
        assertEquals(new BoolValue(true), new RelationalExp(RelOp.GE, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(5))).eval(symTable, heap));
    }

    @Test
    void operatorsEvaluateToFalse() throws Exception {
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.LT, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(2))).eval(symTable, heap));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.LE, new ValueExp(new IntValue(6)), new ValueExp(new IntValue(5))).eval(symTable, heap));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.EQ, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(3))).eval(symTable, heap));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.NE, new ValueExp(new IntValue(5)), new ValueExp(new IntValue(5))).eval(symTable, heap));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.GT, new ValueExp(new IntValue(2)), new ValueExp(new IntValue(5))).eval(symTable, heap));
        assertEquals(new BoolValue(false), new RelationalExp(RelOp.GE, new ValueExp(new IntValue(2)), new ValueExp(new IntValue(5))).eval(symTable, heap));
    }

    @Test
    void nonIntegerLeftOperandThrowsException() {
        IExp exp = new RelationalExp(RelOp.LT, new ValueExp(new BoolValue(true)), new ValueExp(new IntValue(5)));
        var ex = assertThrows(ExpressionException.class, () -> exp.eval(symTable, heap));
        assertTrue(ex.getMessage().contains("first operand is not an integer"));
    }

    @Test
    void nonIntegerRightOperandThrowsException() {
        IExp exp = new RelationalExp(RelOp.LT, new ValueExp(new IntValue(5)), new ValueExp(new BoolValue(true)));
        var ex = assertThrows(ExpressionException.class, () -> exp.eval(symTable, heap));
        assertTrue(ex.getMessage().contains("second operand is not an integer"));
    }
}