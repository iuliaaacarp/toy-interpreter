package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.stmt.*;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.repository.IRepository;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    @TempDir
    Path dir;

    private IStmt validProgram;
    private IStmt invalidProgram;

    private PrgState run(IStmt program) throws MyException {
        PrgState state = PrgState.of(program);
        var repo = new Repository(state, dir.resolve("log.txt").toString());
        new Controller(repo, false).allStep();
        return state;
    }

    @Test
    void fileExampleReadsBothNumbersAndClosesTheFile() throws Exception {
        Path in = dir.resolve("test.in");
        Files.writeString(in, "15\n50\n");
        PrgState s = run(Examples.fileExample(in.toString()));
        assertEquals(new IntValue(15), s.getOut().get(0));
        assertEquals(new IntValue(50), s.getOut().get(1));
        assertTrue(s.getFileTable().toMap().isEmpty());
    }

    @Test
    void logFileContainsTheFourSectionsInOrder() throws Exception {
        run(Examples.EX1);
        String log = Files.readString(dir.resolve("log.txt"));
        int a = log.indexOf("ExeStack:"), b = log.indexOf("SymTable:"),
                c = log.indexOf("Out:"), d = log.indexOf("FileTable:");
        assertTrue(0 <= a && a < b && b < c && c < d);
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
    void ex4WithEmptyFilePrintsTwoZeros() throws Exception {
        Path in = dir.resolve("empty.in");
        Files.writeString(in, "");

        PrgState s = run(Examples.fileExample(in.toString()));

        assertEquals(2, s.getOut().toList().size());
        assertEquals(new IntValue(0), s.getOut().get(0));
        assertEquals(new IntValue(0), s.getOut().get(1));
    }

    @Test
    void ex5PrintsFiveUsingRelationalOperator() throws Exception {
        PrgState s = run(Examples.EX5);
        assertEquals(1, s.getOut().toList().size());
        assertEquals(new IntValue(5), s.getOut().get(0));
    }

    @Test
    void oneStepOnEmptyStackThrows() {
        PrgState state = PrgState.of(new NopStmt());
        try { state.getExeStack().pop(); } catch (Exception ignored) {}

        Controller controller = new Controller(new Repository(state, dir.resolve("empty.txt").toString()), false);

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
        PrgState state = PrgState.of(validProgram);
        IRepository repo = new Repository(state, dir.resolve("valid_log.txt").toString());

        Controller controller = new Controller(repo, false);

        controller.allStep();

        assertTrue(state.getExeStack().isEmpty(), "Execution stack should be empty after execution");
        assertTrue(state.getSymTable().isDefined("v"), "Variable 'v' should be in the symbol table");
        assertEquals(new IntValue(5), state.getSymTable().lookup("v"), "Variable 'v' should equal 5");
    }

    @Test
    void allStepThrowsExceptionForInvalidProgram() {
        PrgState state = PrgState.of(invalidProgram);
        IRepository repo = new Repository(state, dir.resolve("invalid_log.txt").toString());
        Controller controller = new Controller(repo, false);

        var ex = assertThrows(MyException.class, controller::allStep);
        assertTrue(ex.getMessage().contains("not declared"), "Exception message should mention undeclared variable");
    }
}