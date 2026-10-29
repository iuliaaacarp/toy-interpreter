package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.stmt.*;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.repository.IRepository;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    private IStmt validProgram;
    private IStmt invalidProgram;

    private static PrgState run(IStmt program) throws MyException {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), program);
        new Controller(new Repository(state), false).allStep();
        return state;
    }

    @Test
    void example1PrintsTwo() throws MyException {
        PrgState state = run(Examples.EX1);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void example2PrintsEighteen() throws MyException {
        PrgState state = run(Examples.EX2);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(new IntValue(18), state.getOut().get(0));
    }

    @Test
    void example3PrintsTwo() throws MyException {
        PrgState state = run(Examples.EX3);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void oneStepOnEmptyStackThrows() {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new NopStmt());
        try { state.getExeStack().pop(); } catch (Exception ignored) {}
        Controller controller = new Controller(new Repository(state), false);

        assertThrows(StatementException.class, () -> controller.oneStep(state));
    }

    @BeforeEach
    void setUp() {
        validProgram = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new AssignStmt("v", new ValueExp(new IntValue(5)))
        );

        invalidProgram = new AssignStmt("v", new ValueExp(new IntValue(5)));
    }

    @Test
    void allStepExecutesValidProgramCorrectly() throws MyException {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), validProgram);
        IRepository repo = new Repository(state);

        Controller controller = new Controller(repo, false);

        controller.allStep();

        assertTrue(state.getExeStack().isEmpty(), "Execution stack should be empty after execution");
        assertTrue(state.getSymTable().isDefined("v"), "Variable 'v' should be in the symbol table");
        assertEquals(new IntValue(5), state.getSymTable().lookup("v"), "Variable 'v' should equal 5");
    }

    @Test
    void allStepThrowsExceptionForInvalidProgram() {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), invalidProgram);
        IRepository repo = new Repository(state);
        Controller controller = new Controller(repo, false);

        var ex = assertThrows(MyException.class, controller::allStep);
        assertTrue(ex.getMessage().contains("not declared"), "Exception message should mention undeclared variable");
    }
}