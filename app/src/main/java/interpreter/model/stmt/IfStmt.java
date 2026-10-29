package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IStack;
import interpreter.model.exp.IExp;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public record IfStmt(IExp cond, IStmt thenS, IStmt elseS) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue condVal = cond.eval(state.getSymTable());

        if (!(condVal instanceof BoolValue(boolean boolCond))) {
            throw new StatementException("Condition expression is not a boolean");
        }

        IStack<IStmt> stack = state.getExeStack();
        if (boolCond) {
            stack.push(thenS);
        } else {
            stack.push(elseS);
        }
        return state;
    }

    @Override
    public String toString() {
        return "(if " + cond + " then " + thenS + " else " + elseS + ")";
    }
}