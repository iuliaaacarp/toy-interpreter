package interpreter.model.adt;

import interpreter.exceptions.ADTException;

public interface IStack<T> {
    void push(T item);
    T pop() throws ADTException;
    boolean isEmpty();
}
