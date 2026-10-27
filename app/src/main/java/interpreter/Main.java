package interpreter;

import interpreter.controller.Controller;
import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.repository.IRepository;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Run Example 1: " + Examples.EX1);
            System.out.println("2. Run Example 2: " + Examples.EX2);
            System.out.println("3. Run Example 3: " + Examples.EX3);
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String option = scanner.nextLine();
            if (option.equals("0")) break;

            IStmt program;
            switch (option) {
                case "1": program = Examples.EX1; break;
                case "2": program = Examples.EX2; break;
                case "3": program = Examples.EX3; break;
                default:
                    System.out.println("Invalid option");
                    continue;
            }

            PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), program);
            IRepository repo = new Repository(state);
            Controller controller = new Controller(repo, true);

            try {
                controller.allStep();
            } catch (MyException | ADTException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}