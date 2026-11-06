package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.exp.IExp;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import interpreter.model.value.IValue;
import java.io.BufferedReader;
import java.io.IOException;

public record ReadFileStmt(IExp exp, String varName) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        IDictionary<String, IValue> symTable = state.getSymTable();
        IDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();

        if (!symTable.isDefined(varName)) {
            throw new StatementException("Variable " + varName + " is not defined in the symbol table.");
        }
        if (!symTable.lookup(varName).getType().equals(new IntType())) {
            throw new StatementException("Variable " + varName + " is not of type int.");
        }

        IValue evaluatedExp = exp.eval(symTable);
        if (!(evaluatedExp instanceof StringValue fileName)) {
            throw new StatementException("Expression " + exp + " does not evaluate to a string.");
        }

        if (!fileTable.isDefined(fileName)) {
            throw new StatementException("File " + fileName.val() + " is not open in the FileTable.");
        }

        BufferedReader reader = fileTable.lookup(fileName);
        try {
            String line = reader.readLine();
            int value = 0;

            if (line != null) {
                value = Integer.parseInt(line.strip());
            }

            symTable.update(varName, new IntValue(value));

        } catch (IOException e) {
            throw new StatementException("Cannot read from file " + fileName.val() + ": " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new StatementException("The value read from file is not an integer.");
        }

        return state;
    }

    @Override
    public String toString() {
        return "readFile(" + exp + ", " + varName + ")";
    }
}