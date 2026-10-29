package interpreter.model.exp;

import interpreter.exceptions.MyException;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.IValue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VarExpTest {
    @Test
    void undefinedVariableThrowsException() {
        VarExp exp = new VarExp("missingVar");
        MyDictionary<String, IValue> symTable = new MyDictionary<>();

        var ex = assertThrows(MyException.class, () -> exp.eval(symTable));
        assertTrue(ex.getMessage().contains("is not defined"));
    }
}