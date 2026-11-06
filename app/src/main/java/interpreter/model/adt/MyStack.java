package interpreter.model.adt;

import interpreter.exceptions.ADTException;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class MyStack<T> implements IStack<T> {
    private final Deque<T> stack = new ArrayDeque<>();

    @Override
    public void push(T item) {
        stack.push(item);
    }

    @Override
    public T pop() throws ADTException {
        if (stack.isEmpty()) {
            throw new ADTException("Stack is empty");
        }
        return stack.pop();
    }

    @Override
    public List<T> toList() {
        List<T> list = new java.util.ArrayList<>(stack);
        java.util.Collections.reverse(list);
        return list;
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public String toString() {
        return stack.toString();
    }
}
