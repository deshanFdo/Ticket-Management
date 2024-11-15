import java.io.FileWriter;
import java.io.IOException;

public class Ticket {
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
            writer.write("Ticket Information\n");
            writer.write("Row: " + (char) ('A' + row) + "\n");
            writer.write("Seat: " + (seat + 1) + "\n");
            writer.write("Price: £" + price + "\n");
            writer.write("Passenger Information\n");
            writer.write("Name: " + person.getName() + "\n");
            writer.write("Surname: " + person.getSurname() + "\n");
            writer.write("Email: " + person.getEmail() + "\n");
            writer.close();
            System.out.println("Ticket information saved to " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the ticket information.");
            // Log the error or handle it appropriately
        }
    }
}
