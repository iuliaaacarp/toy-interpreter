package interpreter.model.stmt;

import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileStmtTest {
    @TempDir
    Path dir;
    private PrgState state;

    @BeforeEach
    void setUp() {
        state = PrgState.of(new NopStmt());
        try { state.getExeStack().pop(); } catch (Exception ignored) {}
    }


    @Test
    void openRFileAddsToFileTable() throws Exception {
        Path file = dir.resolve("test.in");
        Files.writeString(file, "10\n");

        new OpenRFileStmt(new ValueExp(new StringValue(file.toString()))).execute(state);
        assertTrue(state.getFileTable().isDefined(new StringValue(file.toString())));
    }

    @Test
    void openRFileNonStringThrows() {
        assertThrows(StatementException.class, () ->
                new OpenRFileStmt(new ValueExp(new IntValue(5))).execute(state));
    }

    @Test
    void openRFileMissingFileThrows() {
        assertThrows(StatementException.class, () ->
                new OpenRFileStmt(new ValueExp(new StringValue("missing_file_xyz.txt"))).execute(state));
    }

    @Test
    void readFileReadsSequentialNumbers() throws Exception {
        Path file = dir.resolve("test.in");
        Files.writeString(file, "15\n50\n");

        new VarDeclStmt("x", new IntType()).execute(state);
        new OpenRFileStmt(new ValueExp(new StringValue(file.toString()))).execute(state);

        IStmt read = new ReadFileStmt(new ValueExp(new StringValue(file.toString())), "x");
        read.execute(state);
        assertEquals(new IntValue(15), state.getSymTable().lookup("x"));

        read.execute(state);
        assertEquals(new IntValue(50), state.getSymTable().lookup("x"));
    }

    @Test
    void readFileUndeclaredVariableThrows() {
        assertThrows(StatementException.class, () ->
                new ReadFileStmt(new ValueExp(new StringValue("test.txt")), "x").execute(state));
    }

    @Test
    void readFileNonIntVariableThrows() throws Exception {
        new VarDeclStmt("b", new BoolType()).execute(state);
        assertThrows(StatementException.class, () ->
                new ReadFileStmt(new ValueExp(new StringValue("test.txt")), "b").execute(state));
    }

    @Test
    void readFileNotOpenThrows() throws Exception {
        new VarDeclStmt("x", new IntType()).execute(state);
        assertThrows(StatementException.class, () ->
                new ReadFileStmt(new ValueExp(new StringValue("closed.txt")), "x").execute(state));
    }

    @Test
    void readFileNonIntegerLineThrows() throws Exception {
        Path file = dir.resolve("bad.in");
        Files.writeString(file, "hello\n");

        new VarDeclStmt("x", new IntType()).execute(state);
        new OpenRFileStmt(new ValueExp(new StringValue(file.toString()))).execute(state);

        assertThrows(StatementException.class, () ->
                new ReadFileStmt(new ValueExp(new StringValue(file.toString())), "x").execute(state));
    }


    @Test
    void closeRFileRemovesFromFileTable() throws Exception {
        Path file = dir.resolve("test.in");
        Files.writeString(file, "10\n");
        StringValue fileName = new StringValue(file.toString());

        new OpenRFileStmt(new ValueExp(fileName)).execute(state);
        assertTrue(state.getFileTable().isDefined(fileName));

        new CloseRFileStmt(new ValueExp(fileName)).execute(state);
        assertFalse(state.getFileTable().isDefined(fileName));
        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void closeRFileNotOpenThrows() {
        assertThrows(StatementException.class, () ->
                new CloseRFileStmt(new ValueExp(new StringValue("never_opened.txt"))).execute(state));
    }
}