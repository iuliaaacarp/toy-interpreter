package interpreter.model.stmt;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.exp.IExp;
import interpreter.model.type.IType;
import interpreter.model.value.IValue;

public record AssignStmt(String id, IExp exp) implements IStmt{
    @Override
    public PrgState execute(PrgState state) throws MyException, ADTException {
        IDictionary<String, IValue> symTable = state.getSymTable();
        if (!symTable.isDefined(id)){
            throw new StatementException("Variable " + id + " is not declared");
        }

        IValue val = exp.eval(symTable);
        IType declaredType = symTable.lookup(id).getType();

        if (!val.getType().equals(declaredType)){
            throw new StatementException("Declared type of variable " + id +
                    " and type of the assigned expression do not match");
        }

        symTable.update(id, val);
        return state;
    }

    @Override
    public String toString() {
        return id + " = " + exp;
    }
}
