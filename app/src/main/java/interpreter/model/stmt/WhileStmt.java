package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.IStack;
import interpreter.model.exp.IExp;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public record WhileStmt(IExp cond, IStmt body) implements IStmt { // S13
    @Override
    public PrgState execute(PrgState state) throws MyException {
        IDictionary<String, IValue> symTable = state.getSymTable();
        IHeap heap = state.getHeap();
        IValue evaluatedCond = cond.eval(symTable, heap);

        if (!(evaluatedCond instanceof BoolValue(boolean boolCond))) {
            throw new StatementException("while: condition " + cond + " is not a boolean");
        }

        if (boolCond) {
            IStack<IStmt> stack = state.getExeStack();
            stack.push(this);
            stack.push(body);
        }
        return state;
    }

    @Override public String toString() {
        return "(while (" + cond + ") " + body + ")";
    }
}
