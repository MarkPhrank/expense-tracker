import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

public class Main {
    private static List<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== УЧЁТ РАСХОДОВ v3 ===");
            System.out.println("1. Добавить расход");
            System.out.println("2. Показать все");
            System.out.println("3. Сохранить в CSV");
            System.out.println("4. Фильтр по категории");
            System.out.println("5. Статистика (сумма, среднее, мин/макс)");
            System.out.println("6. Топ-3 категории по расходам");
            System.out.println("0. Выход");
            System.out.print("Выбери действие: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addExpense(scanner);
                case "2" -> showAll();
                case "3" -> saveToFile();
                case "4" -> filterByCategory(scanner);
                case "5" -> showStatistics();
                case "6" -> showTopCategories();
                case "0" -> { System.out.println("👋 Пока!"); return; }
                default -> System.out.println("❌ Неверный ввод.");
            }
        }
    }

    private static void addExpense(Scanner scanner) {
        System.out.print("📂 Категория: ");
        String category = scanner.nextLine().trim();
        System.out.print("💰 Сумма: ");
        double amount;
        try { amount = Double.parseDouble(scanner.nextLine()); }
        catch (NumberFormatException e) { System.out.println("❌ Ошибка числа!"); return; }

        System.out.print("📅 Дата (YYYY-MM-DD) или ENTER: ");
        String dateInput = scanner.nextLine().trim();
        LocalDate date = dateInput.isEmpty() ? LocalDate.now() : LocalDate.parse(dateInput);

        transactions.add(new Transaction(category, amount, date));
        System.out.println("✅ Добавлено!");
    }

    private static void showAll() {
        if (transactions.isEmpty()) { System.out.println("📭 Нет расходов."); return; }
        System.out.println("\n📊 Все расходы:");
        transactions.forEach(System.out::println);
    }

    private static void saveToFile() {
        try (PrintWriter w = new PrintWriter(new FileWriter("expenses.csv"))) {
            w.println("date,category,amount");
            transactions.forEach(t -> w.println(t.toCsv()));
            System.out.println("💾 Сохранено в expenses.csv");
        } catch (IOException e) { System.out.println("❌ Ошибка записи: " + e.getMessage()); }
    }

    // 🔥 ДЕНЬ 3: Stream API
    private static void filterByCategory(Scanner scanner) {
        System.out.print("📂 Введи категорию: ");
        String cat = scanner.nextLine().trim();
        var filtered = transactions.stream()
                .filter(t -> t.category.equalsIgnoreCase(cat))
                .toList();

        if (filtered.isEmpty()) System.out.println("📭 Ничего не найдено.");
        else {
            System.out.println("\n💸 Категория '" + cat + "':");
            filtered.forEach(System.out::println);
        }
    }

    private static void showStatistics() {
        if (transactions.isEmpty()) { System.out.println("📭 Нет данных."); return; }
        
        var stats = transactions.stream()
                .mapToDouble(t -> t.amount)
                .summaryStatistics(); // За 1 проход считает сумму, среднее, мин, макс

        System.out.println("\n📊 Статистика:");
        System.out.printf("💰 Всего: %.2f ₽\n", stats.getSum());
        System.out.printf("📈 Средний чек: %.2f ₽\n", stats.getAverage());
        System.out.printf("💵 Мин: %.2f ₽ | Макс: %.2f ₽\n", stats.getMin(), stats.getMax());
    }

    private static void showTopCategories() {
        if (transactions.isEmpty()) { System.out.println("📭 Нет данных."); return; }

        Map<String, Double> sums = transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.category,
                        Collectors.summingDouble(t -> t.amount)
                ));

        System.out.println("\n🏆 Топ-3 категории по расходам:");
        sums.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(3)
                .forEach(e -> System.out.printf("%s: %.2f ₽\n", e.getKey(), e.getValue()));
    }
}