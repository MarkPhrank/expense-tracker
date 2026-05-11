import java.time.LocalDate;

public class Transaction {
    String category;
    double amount;
    LocalDate date;

    Transaction(String category, double amount, LocalDate date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }
    // Преобразуем объект в строку для CSV
    public String toCsv() {
        return date + "," + category + "," + amount;
    }

    @Override
    public String toString() {
        return String.format("[%s] %.2f ₽ — %s", date, amount, category);
    }
}