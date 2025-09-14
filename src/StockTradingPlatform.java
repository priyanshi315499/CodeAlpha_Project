import java.io.*;
import java.util.*;

// ---------- STOCK CLASS ----------
class Stock {
    private String symbol;
    private double price;

    public Stock(String symbol, double initPrice) {
        this.symbol = symbol;
        this.price = initPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    // simulate random price updates
    public void updatePrice() {
        double pct = (Math.random() * 0.10) - 0.05; // -5% to +5%
        price = price * (1 + pct);
        price = Math.round(price * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return symbol + ": $" + String.format("%.2f", price);
    }
}

// ---------- TRANSACTION CLASS ----------
class Transaction {
    private String symbol;
    private String type; // BUY / SELL
    private int quantity;
    private double price;

    public Transaction(String symbol, String type, int quantity, double price) {
        this.symbol = symbol;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public String toString() {
        return type + " " + quantity + " " + symbol + " @ $" + price;
    }
}

// ---------- PORTFOLIO CLASS ----------
class Portfolio {
    private double cash;
    private Map<String, Integer> holdings;
    private List<Transaction> history;

    public Portfolio(double initialCash) {
        this.cash = initialCash;
        this.holdings = new HashMap<>();
        this.history = new ArrayList<>();
    }

    public void buy(Stock stock, int qty) {
        double cost = stock.getPrice() * qty;
        if (cost > cash) {
            System.out.println("❌ Not enough cash to buy " + qty + " of " + stock.getSymbol());
            return;
        }
        cash -= cost;
        holdings.put(stock.getSymbol(), holdings.getOrDefault(stock.getSymbol(), 0) + qty);
        history.add(new Transaction(stock.getSymbol(), "BUY", qty, stock.getPrice()));
        System.out.println("✅ Bought " + qty + " of " + stock.getSymbol());
    }

    public void sell(Stock stock, int qty) {
        int have = holdings.getOrDefault(stock.getSymbol(), 0);
        if (qty > have) {
            System.out.println("❌ Not enough shares to sell.");
            return;
        }
        holdings.put(stock.getSymbol(), have - qty);
        double revenue = stock.getPrice() * qty;
        cash += revenue;
        history.add(new Transaction(stock.getSymbol(), "SELL", qty, stock.getPrice()));
        System.out.println("✅ Sold " + qty + " of " + stock.getSymbol());
    }

    public double getNetWorth(Map<String, Stock> market) {
        double total = cash;
        for (var entry : holdings.entrySet()) {
            String sym = entry.getKey();
            int qty = entry.getValue();
            Stock s = market.get(sym);
            if (s != null) {
                total += s.getPrice() * qty;
            }
        }
        return total;
    }

    public void showPortfolio(Map<String, Stock> market) {
        System.out.println("\n📊 Portfolio:");
        System.out.printf("Cash: $%.2f%n", cash);
        for (var entry : holdings.entrySet()) {
            String sym = entry.getKey();
            int qty = entry.getValue();
            Stock s = market.get(sym);
            double price = (s == null) ? 0.0 : s.getPrice();
            System.out.printf("%s: %d shares @ $%.2f%n", sym, qty, price);
        }
        System.out.printf("Net Worth: $%.2f%n", getNetWorth(market));
    }

    public void showHistory() {
        System.out.println("\n📜 Transaction History:");
        for (Transaction t : history) {
            System.out.println(t);
        }
    }
}

// ---------- MAIN APP CLASS ----------
public class StockTradingPlatform {
    private Map<String, Stock> market;
    private Portfolio portfolio;
    private Scanner scanner;

    public StockTradingPlatform() {
        market = new HashMap<>();
        market.put("AAPL", new Stock("AAPL", 150.0));
        market.put("GOOG", new Stock("GOOG", 2800.0));
        market.put("TSLA", new Stock("TSLA", 700.0));
        market.put("AMZN", new Stock("AMZN", 3300.0));

        portfolio = new Portfolio(10000.0); // starting cash
        scanner = new Scanner(System.in);
    }

    private void showMenu() {
        System.out.println("\n=== Stock Trading Platform ===");
        System.out.println("1. View Market");
        System.out.println("2. Buy Stock");
        System.out.println("3. Sell Stock");
        System.out.println("4. View Portfolio");
        System.out.println("5. View History");
        System.out.println("6. Next Day (update prices)");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    private void showMarket() {
        System.out.println("\n📈 Market Prices:");
        for (Stock s : market.values()) {
            System.out.println(s);
        }
    }

    private void buyStock() {
        System.out.print("Enter symbol: ");
        String sym = scanner.nextLine().toUpperCase();
        Stock s = market.get(sym);
        if (s == null) {
            System.out.println("❌ Stock not found.");
            return;
        }
        System.out.print("Enter quantity: ");
        int qty = Integer.parseInt(scanner.nextLine());
        portfolio.buy(s, qty);
    }

    private void sellStock() {
        System.out.print("Enter symbol: ");
        String sym = scanner.nextLine().toUpperCase();
        Stock s = market.get(sym);
        if (s == null) {
            System.out.println("❌ Stock not found.");
            return;
        }
        System.out.print("Enter quantity: ");
        int qty = Integer.parseInt(scanner.nextLine());
        portfolio.sell(s, qty);
    }

    private void updatePrices() {
        for (Stock s : market.values()) {
            s.updatePrice();
        }
        System.out.println("✅ Prices updated.");
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            showMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> showMarket();
                case "2" -> buyStock();
                case "3" -> sellStock();
                case "4" -> portfolio.showPortfolio(market);
                case "5" -> portfolio.showHistory();
                case "6" -> updatePrices();
                case "0" -> exit = true;
                default -> System.out.println("❌ Invalid choice.");
            }
        }
        System.out.println("👋 Exiting platform.");
    }

    public static void main(String[] args) {
        StockTradingPlatform app = new StockTradingPlatform();
        app.run();
    }
}
