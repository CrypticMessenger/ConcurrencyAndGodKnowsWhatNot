import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class LeadersAndFollowers {

    public static void main(String[] args) {
        final Semaphore leaderArrived = new Semaphore(0);
        final Semaphore followerArrived = new Semaphore(0);

        // The main loop to simulate dancers arriving.
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n1: Add a Leader");
                System.out.println("2: Add a Follower");
                System.out.println("3: Exit");
                System.out.print("Choose an option: ");

                if (scanner.hasNextInt()) {
                    int choice = scanner.nextInt();
                    switch (choice) {
                        case 1:
                            leaderArrived.release();
                            new Thread(new Leader(followerArrived)).start();
                            break;
                        case 2:
                            followerArrived.release();
                            new Thread(new Follower(leaderArrived)).start();
                            break;
                        case 3:
                            System.out.println("Exiting...");
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // Clear the invalid input
                }
            }
        }
    }
}