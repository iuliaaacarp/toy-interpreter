package interpreter.model.stmt;

import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        try { state.getExeStack().pop(); } catch (Exception ignored) {}
    }

    @Test
    void varDeclAddsVariableWithDefaultValue() throws Exception {
        new VarDeclStmt("v", new IntType()).execute(state);
        assertTrue(state.getSymTable().isDefined("v"));
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
    }

    @Test
    void redeclaredVariableThrows() throws Exception {
        new VarDeclStmt("v", new IntType()).execute(state);
        var ex = assertThrows(StatementException.class, () -> new VarDeclStmt("v", new BoolType()).execute(state));
        assertTrue(ex.getMessage().contains("already declared"));
    }

    @Test
    void assignUpdatesVariableValue() throws Exception {
        state.getSymTable().put("v", new IntValue(0));
        new AssignStmt("v", new ValueExp(new IntValue(5))).execute(state);
        assertEquals(new IntValue(5), state.getSymTable().lookup("v"));
    }

    @Test
    void assignmentToUndeclaredVariableThrows() {
        var ex = assertThrows(StatementException.class, () -> new AssignStmt("v", new ValueExp(new IntValue(5))).execute(state));
        assertTrue(ex.getMessage().contains("not declared"));
    }

    @Test
    void typeMismatchInAssignmentThrows() {
        state.getSymTable().put("v", new IntValue(0)); // Declared as int
        var ex = assertThrows(StatementException.class, () -> new AssignStmt("v", new ValueExp(new BoolValue(true))).execute(state));
        assertTrue(ex.getMessage().contains("do not match"));
    }

    @Test
    void ifStmtPushesCorrectBranch() throws Exception {
        IStmt thenS = new PrintStmt(new ValueExp(new IntValue(1)));
        IStmt elseS = new PrintStmt(new ValueExp(new IntValue(2)));

        new IfStmt(new ValueExp(new BoolValue(true)), thenS, elseS).execute(state);
        assertEquals(thenS, state.getExeStack().pop());
    }

    @Test
    void nonBooleanIfConditionThrows() {
        var ex = assertThrows(StatementException.class, () ->
                new IfStmt(new ValueExp(new IntValue(1)), new NopStmt(), new NopStmt()).execute(state));
        assertTrue(ex.getMessage().contains("not a boolean"));
    }
}