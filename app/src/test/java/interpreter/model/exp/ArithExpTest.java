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

class ArithExpTest {
    private IDictionary<String, IValue> symTable;

    private static IExp num(int n) {
        return new ValueExp(new IntValue(n));
    }

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
    }

    @Test
    void respectsOperatorPrecedence() throws MyException, ADTException {
        IExp e = new ArithExp(ArithOp.ADD, num(2),
                new ArithExp(ArithOp.MUL, num(3), num(5)));
        assertEquals(new IntValue(17), e.eval(symTable));
    }

    @Test
    void testArithmeticOperatorsCorrectly() throws MyException, ADTException {
        assertEquals(new IntValue(5), new ArithExp(ArithOp.ADD, num(2), num(3)).eval(symTable));
        assertEquals(new IntValue(1), new ArithExp(ArithOp.SUB, num(4), num(3)).eval(symTable));
        assertEquals(new IntValue(12), new ArithExp(ArithOp.MUL, num(4), num(3)).eval(symTable));
        assertEquals(new IntValue(2), new ArithExp(ArithOp.DIV, num(7), num(3)).eval(symTable));
    }

    @Test
    void divisionByZeroThrows() {
        IExp e = new ArithExp(ArithOp.DIV, num(1), num(0));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("division by zero", ex.getMessage());
    }

    @Test
    void nonIntegerOperandThrows() {
        IExp boolExp = new ValueExp(new BoolValue(true));
        IExp e = new ArithExp(ArithOp.ADD, num(1), boolExp);
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("second operand is not an integer", ex.getMessage());
    }
}