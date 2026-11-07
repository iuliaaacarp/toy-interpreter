package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public record CloseRFileStmt(IExp exp) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        IDictionary<String, IValue> symTable = state.getSymTable();
        IDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();
        IValue evaluatedExp = exp().eval(symTable);

        if (!(evaluatedExp instanceof StringValue fileName)) {
            throw new StatementException("Expression " + exp + " does not evaluate to a string.");
        }
        if (!fileTable.isDefined(fileName)) {
            throw new StatementException("Expression " + exp + " does not define a file.");
        }

        BufferedReader bufferedReader = fileTable.lookup(fileName);
        try{
            bufferedReader.close();
            fileTable.remove(fileName);
        } catch (IOException e) {
            throw new StatementException("Cannot close file " + fileName.val() + ": " + e.getMessage());
        }
        return state;
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp + ")";
    }
}
