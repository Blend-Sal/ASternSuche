import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose a task:");
        System.out.println("1 -> Task 2");
        System.out.println("2 -> Task 3");
        System.out.println("3 -> Task 4 (Manhattan Distance)");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> Helper.task2(args);
            case 2 -> Helper.task3And4(false);
            case 3 -> Helper.task3And4(true);
            default -> System.out.println("Invalid choice");
        }

        scanner.close();
    }
}