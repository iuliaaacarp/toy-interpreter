package interpreter;

import interpreter.controller.Controller;
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
static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            String option = scanner.nextLine().trim();

            if (option.equals("0")) {
                System.out.println("Exiting interpreter. Goodbye!");
                break;
            }

            IStmt program = switch (option) {
                case "1" -> Examples.EX1;
                case "2" -> Examples.EX2;
                case "3" -> Examples.EX3;
                case "4" -> Examples.EX4;
                default -> null;
            };

            if (program == null) {
                System.out.println("Invalid option. Please choose 1, 2, 3, 4, or 0.\n");
                continue;
            }

            runProgram(program);
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("==================== MENU ====================");
        System.out.println("1. Run Example 1: " + Examples.EX1);
        System.out.println("2. Run Example 2: " + Examples.EX2);
        System.out.println("3. Run Example 3: " + Examples.EX3);
        System.out.println("4. Run Example 4: " + Examples.EX4);
        System.out.println("0. Exit");
        System.out.println("==============================================");
        System.out.print("Choose an option: ");
    }

    private static void runProgram(IStmt program) {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), program);
        IRepository repo = new Repository(state);
        Controller controller = new Controller(repo, true);

        System.out.println("\n--- Starting Execution ---");
        try {
            controller.allStep();
            System.out.println(">>> Execution finished successfully! Final Out: " + state.getOut() + "\n");
        } catch (MyException e) {
            System.out.println(">>> ERROR: " + e.getMessage() + "\n");
        }
    }
}