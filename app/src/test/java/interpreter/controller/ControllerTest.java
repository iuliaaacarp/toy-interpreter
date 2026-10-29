package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.stmt.AssignStmt;
import interpreter.model.stmt.CompStmt;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.repository.IRepository;
import interpreter.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    private IStmt validProgram;
    private IStmt invalidProgram;

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