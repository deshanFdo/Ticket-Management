import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class w2053236_PlaneManagement {
    private static final int NUM_ROWS = 4;
    private static final int[] SEATS_PER_ROW = {14, 12, 12, 14};
    private static final boolean[][] seatStatus = new boolean[NUM_ROWS][];
    private static final Ticket[][] tickets = new Ticket[NUM_ROWS][];

    static {
        for (int i = 0; i < NUM_ROWS; i++) {
            seatStatus[i] = new boolean[SEATS_PER_ROW[i]];
            tickets[i] = new Ticket[SEATS_PER_ROW[i]];
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String goBackToMenu;

        do {
            System.out.println("Welcome to the Plane Management");
            System.out.println("********************************");
            System.out.println("       Menu Options             ");
            System.out.println("********************************");

            System.out.println("1) Buy a seat");
            System.out.println("2) Cancel a seat");
            System.out.println("3) Find available seat");
            System.out.println("4) Show seating plan");
            System.out.println("5) Print ticket information and total sales");
            System.out.println("6) Search ticket");
            System.out.println("0) Quit");
            System.out.println("**************************************");
            System.out.println("Please select an option (0-6):");

            int option;
            while (true) {
                if (input.hasNextInt()) { // Using hasNextInt() to check if input is an integer
                    option = input.nextInt();
                    if (option >= 0 && option <= 6) {
                        break;
                    } else {
                        System.out.println("Please input a number between 0 and 6.");
                    }
                } else {
                    System.out.println("Please input a number between 0 and 6.");
                    input.next(); // consume the invalid input
                }
            }

            switch (option) {
                case 1:
                    buySeat(input);
                    break;
                case 2:
                    cancelSeat(input);
                    break;
                case 3:
                    findFirstAvailable();
                    break;
                case 4:
                    showSeatingPlan();
                    break;
                case 5:
                    printTicketsInfo();
                    break;
                case 6:
                    searchTicket(input);
                    break;
                case 0:
                    System.out.println("Quitting the program...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please select a valid option.");
            }

            do {
                System.out.println("Do you want to go back to the menu? (y/n)");
                goBackToMenu = input.next().toLowerCase(); // Using equalsIgnoreCase() for case-insensitive comparison
            } while (!goBackToMenu.equals("y") && !goBackToMenu.equals("n"));

        } while (goBackToMenu.equals("y"));
        input.close();
    }

    private static void buySeat(Scanner scanner) {
        char rowLetter;
        int row;
        int seatNumber;

        do {
            System.out.print("Enter row letter (A-D): ");
            rowLetter = scanner.next().toUpperCase().charAt(0); // Using toUpperCase() for case normalization
            row = rowLetter - 'A';
        } while (row < 0 || row >= NUM_ROWS);

        do {
            System.out.print("Enter seat number (1-" + SEATS_PER_ROW[row] + "): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Please input a valid seat number.");
                System.out.print("Enter seat number (1-" + SEATS_PER_ROW[row] + "): ");
                scanner.next(); // consume the invalid input
            }
            seatNumber = scanner.nextInt() - 1;
        } while (seatNumber < 0 || seatNumber >= SEATS_PER_ROW[row]);

        if (seatStatus[row][seatNumber] == false) {
			seatStatus[row][seatNumber] = true;
			Person person = getPersonInfo(scanner);
			double price = calculatePrice(row, seatNumber);
			Ticket ticket = new Ticket(row, seatNumber, price, person);
			tickets[row][seatNumber] = ticket;
			ticket.save(); // Save ticket information to a file
			System.out.println("Seat " + rowLetter + "-" + (seatNumber + 1) + " purchased successfully.");
			}
		else {
			System.out.println("Seat " + rowLetter + "-" + (seatNumber + 1) + " is already sold.");
			}

    }

    private static Person getPersonInfo(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.next();
        System.out.print("Enter surname: ");
        String surname = scanner.next();
        System.out.print("Enter email: ");
        String email = scanner.next();
        return new Person(name, surname, email);
    }

    private static void cancelSeat(Scanner scanner) {
        char rowLetter;
        int row;
        int seatNumber;

        do {
            System.out.print("Enter row letter (A-D): ");
            rowLetter = scanner.next().toUpperCase().charAt(0);
            row = rowLetter - 'A';
        } while (row < 0 || row >= NUM_ROWS);

        do {
            System.out.print("Enter seat number (1-" + SEATS_PER_ROW[row] + "): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Please input a valid seat number.");
                System.out.print("Enter seat number (1-" + SEATS_PER_ROW[row] + "): ");
                scanner.next(); // consume the invalid input
            }
            seatNumber = scanner.nextInt() - 1;
        } while (seatNumber < 0 || seatNumber >= SEATS_PER_ROW[row]);

        if (seatStatus[row][seatNumber]) { // Using == for boolean comparison
            seatStatus[row][seatNumber] = false; // Mark the seat as unsold
            tickets[row][seatNumber] = null; // Remove ticket information
            System.out.println("Seat " + rowLetter + "-" + (seatNumber + 1) + " canceled successfully.");
        } else {
            System.out.println("Seat " + rowLetter + "-" + (seatNumber + 1) + " is not sold.");
        }
    }

    private static void findFirstAvailable() {
        boolean seatFound = false;
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int seatNumber = 0; seatNumber < SEATS_PER_ROW[row]; seatNumber++) {
                if (!seatStatus[row][seatNumber]) { // Using == for boolean comparison
                    char rowLetter = (char) ('A' + row);
                    System.out.println("First available seat is: " + rowLetter + "-" + (seatNumber + 1));
                    seatFound = true;
                    break;
                }
            }
            if (seatFound) {
                break;
            }
        }
        if (!seatFound) {
            System.out.println("No available seats found.");
        }
    }

    private static void showSeatingPlan() {
        System.out.println("\n" + "Seating Plan:");
        for (int row = 0; row < NUM_ROWS; row++) {
            char rowLetter = (char) ('A' + row);
            System.out.print(rowLetter + ": ");
            for (int seatNumber = 0; seatNumber < SEATS_PER_ROW[row]; seatNumber++) {
                if (seatStatus[row][seatNumber]) {
                    System.out.print("X ");
                } else {
                    System.out.print("O ");
                }
            }
            System.out.println();
        }
    }

    private static void printTicketsInfo() {
        double totalAmount = 0.0;
        System.out.println("\n" + "Tickets Information:");
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int seatNumber = 0; seatNumber < SEATS_PER_ROW[row]; seatNumber++) {
                if (seatStatus[row][seatNumber]) {
                    Ticket ticket = tickets[row][seatNumber];
                    System.out.println("\n" + "Row: " + (char) ('A' + row) + ", Seat: " + (seatNumber + 1));
                    System.out.println("\n" + "Price: £" + ticket.getPrice());
                    totalAmount += ticket.getPrice();
                }
            }
        }
        System.out.println("Total Amount: £" + totalAmount);
    }

    private static void searchTicket(Scanner scanner) {
        char rowLetter;
        int row;
        int seatNumber;

        do {
            System.out.print("\n" + "Enter row letter (A-D): ");
            rowLetter = scanner.next().toUpperCase().charAt(0);
            row = rowLetter - 'A';
        } while (row < 0 || row >= NUM_ROWS);

        do {
            System.out.print("\n" + "Enter seat number (1-" + SEATS_PER_ROW[row] + "): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Please input a valid seat number.");
                System.out.print("\n" + "Enter seat number (1-" + SEATS_PER_ROW[row] + "): ");
                scanner.next(); // consume the invalid input
            }
            seatNumber = scanner.nextInt() - 1;
        } while (seatNumber < 0 || seatNumber >= SEATS_PER_ROW[row]);

        if (seatStatus[row][seatNumber]) { // Using == for boolean comparison
            Ticket ticket = tickets[row][seatNumber];
            System.out.println("Ticket found:");
            System.out.println("Row: " + rowLetter + ", Seat: " + (seatNumber + 1));
            System.out.println("Price: £" + ticket.getPrice());
            System.out.println("Passenger Information:");
            ticket.getPerson().printInfo();
        } else {
            System.out.println("This seat is available.");
        }
    }

    private static double calculatePrice(int row, int seatNumber) {
        if (seatNumber < 5) {
            return 200.0;
        } else if (seatNumber < 9) {
            return 150.0;
        } else {
            return 180.0;
        }
    }

    private static boolean isValidSeat(int row, int seatNumber) {
        return row >= 0 && row < NUM_ROWS && seatNumber >= 0 && seatNumber < SEATS_PER_ROW[row];
    }

    private static class Person {
        private String name;
        private String surname;
        private String email;

        public Person(String name, String surname, String email) {
            this.name = name;
            this.surname = surname;
            this.email = email;
        }

        public void printInfo() {
            System.out.println("Name: " + name);
            System.out.println("Surname: " + surname);
            System.out.println("Email: " + email);
        }
    }

    private static class Ticket {
        private int row;
        private int seat;
        private double price;
        private Person person;

        public Ticket(int row, int seat, double price, Person person) {
            this.row = row;
            this.seat = seat;
            this.price = price;
            this.person = person;
        }

        public double getPrice() {
            return price;
        }

        public Person getPerson() {
            return person;
        }

        public void save() {
            String fileName = "tickets/" + (char) ('A' + row) + String.valueOf(seat + 1) + ".txt";
            try {
                FileWriter writer = new FileWriter(fileName);
                writer.write("Ticket Information : \n\n");
                writer.write("Row: " + (char) ('A' + row) + "		Seat: " + (seat + 1) +  "		Price: £" + price + "\n");
                writer.write("Passenger Information : \n\n");
                writer.write("Name: " + person.name + "\n");
                writer.write("Surname: " + person.surname + "\n");
                writer.write("Email: " + person.email + "\n");
                writer.close();
            } catch (IOException e) {
                System.out.println("Error saving ticket information.");
            }
        }
    }
}
