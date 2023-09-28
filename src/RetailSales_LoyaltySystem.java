import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

class Product {
    private String name;  // name of the product
    private double price;  // price of the product
    private boolean isLuxury;  // indicates whether the product is a luxury item

    public Product(String name, double price, boolean isLuxury) {
        this.name = name;
        this.price = price;
        this.isLuxury = isLuxury;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isLuxury() {
        return isLuxury;
    }

    public void setLuxury(boolean luxury) {
        isLuxury = luxury;
    }
}

abstract class Customer {
    private String name;  // name of the customer
    private int ID;  // customer ID

    public Customer(String name, int ID) {
        this.name = name;
        this.ID = ID;
    }

    public abstract double transactionPromotion(double transactionValue);

    public abstract void gainPoints(double transactionValue);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public abstract double getTotalPoints();
}

class RegularCustomer extends Customer {
    private double zR;  // promotion rate for regular customers
    private double pointLimit;  // transaction amount limit for promotions
    private double zPR;  // points accumulation rate for regular customers
    private double totalPoints;  // total points of the customer

    public RegularCustomer(String name, int ID, double zR, double pointLimit, double zPR) {
        super(name, ID);
        this.zR = zR;
        this.pointLimit = pointLimit;
        this.zPR = zPR;
        this.totalPoints = 0.0;
    }

    public double transactionPromotion(double transactionValue) {
        if (transactionValue > pointLimit) {
            return (transactionValue - pointLimit) * zR;
        }
        return 0.0;
    }

    public void gainPoints(double transactionValue) {
        if (transactionValue > pointLimit) {
            double points = (transactionValue - pointLimit) * zPR;
            totalPoints += points;
        }
    }

    public double getzR() {
        return zR;
    }

    public void setzR(double zR) {
        this.zR = zR;
    }

    public double getPointLimit() {
        return pointLimit;
    }

    public void setPointLimit(double pointLimit) {
        this.pointLimit = pointLimit;
    }

    public double getzPR() {
        return zPR;
    }

    public void setzPR(double zPR) {
        this.zPR = zPR;
    }

    public double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(double totalPoints) {
        this.totalPoints = totalPoints;
    }
}

class GoldCustomer extends Customer {
    private double zP;  // points accumulation rate for gold customers
    private double totalPoints;  // total points of the customer

    public GoldCustomer(String name, int ID, double zP) {
        super(name, ID);
        this.zP = zP;
        this.totalPoints = 0.0;
    }

    public double transactionPromotion(double transactionValue) {
        return transactionValue * zP;
    }

    public void gainPoints(double transactionValue) {
        double points = transactionValue * zP;
        totalPoints += points;
    }

    public double getzP() {
        return zP;
    }

    public void setzP(double zP) {
        this.zP = zP;
    }

    public double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(double totalPoints) {
        this.totalPoints = totalPoints;
    }
}

class Transaction {
    private Customer customer;
    private ArrayList<ProductAmountPair> productAmountPairs;

    public Transaction(Customer customer) {
        this.customer = customer;
        this.productAmountPairs = new ArrayList<>();
    }

    public void addBasket(Product product, int amount) {
        productAmountPairs.add(new ProductAmountPair(product, amount));
    }

    public double invoice() {
        double totalPayment = 0.0;

        for (ProductAmountPair pair : productAmountPairs) {
            Product product = pair.getProduct();
            int amount = pair.getAmount();
            double price = product.getPrice() * amount;

            if (product.isLuxury() && amount >= 3) {
                price -= product.getPrice() * 0.1;
            }

            totalPayment += price;
        }

        totalPayment -= customer.transactionPromotion(totalPayment);
        customer.gainPoints(totalPayment);

        return totalPayment;
    }
}

class ProductAmountPair {
    private Product product;
    private int amount;

    public ProductAmountPair(Product product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

public class RetailSales_LoyaltySystem {
    private static ArrayList<Product> products = new ArrayList<>();
    private static ArrayList<Customer> customers = new ArrayList<>();
    private static ArrayList<Transaction> transactions = new ArrayList<>();

    private static void createInitialData() {
        Product product1 = new Product("Product 1", 10.0, false);
        Product product2 = new Product("Product 2", 20.0, true);
        Product product3 = new Product("Product 3", 30.0, true);
        Product product4 = new Product("Product 4", 40.0, false);

        products.add(product1);
        products.add(product2);
        products.add(product3);
        products.add(product4);

        Customer regular1 = new RegularCustomer("Regular Customer 1", 1, 0.1, 50.0, 0.02);
        Customer regular2 = new RegularCustomer("Regular Customer 2", 2, 0.05, 60.0, 0.03);
        Customer gold1 = new GoldCustomer("Gold Customer 1", 3, 0.1);
        Customer gold2 = new GoldCustomer("Gold Customer 2", 4, 0.2);

        customers.add(regular1);
        customers.add(regular2);
        customers.add(gold1);
        customers.add(gold2);
    }

    private static void displayProducts() {
        System.out.println("Product List:");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.println((i + 1) + ". " + product.getName() + " - $" + product.getPrice());
        }
        System.out.println();
    }

    private static void displayCustomers() {
        System.out.println("Customer List:");
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            System.out.println((i + 1) + ". " + customer.getName());
        }
        System.out.println();
    }

    private static void processTransaction() {
        displayProducts();
        displayCustomers();

        Scanner scanner = new Scanner(System.in);

        System.out.print("Select a customer (enter the corresponding number): ");
        int customerChoice = scanner.nextInt();
        if (customerChoice <= 0 || customerChoice > customers.size()) {
            System.out.println("Invalid customer choice!");
            return;
        }
        Customer customer = customers.get(customerChoice - 1);

        System.out.print("Enter the product number: ");
        int productChoice = scanner.nextInt();
        if (productChoice <= 0 || productChoice > products.size()) {
            System.out.println("Invalid product choice!");
            return;
        }
        Product product = products.get(productChoice - 1);

        System.out.print("Enter the product amount: ");
        int amount = scanner.nextInt();
        if (amount <= 0) {
            System.out.println("Invalid product amount!");
            return;
        }

        Transaction transaction = new Transaction(customer);
        transaction.addBasket(product, amount);
        transactions.add(transaction);

        double totalPayment = transaction.invoice();
        System.out.println("Total Payment: $" + totalPayment);
        System.out.println();
    }

    private static void displayCustomerPoints() {
        System.out.println("Customer Points:");
        for (Customer customer : customers) {
            System.out.println(customer.getName() + " - " + customer.getTotalPoints() + " points");
        }
        System.out.println();
    }

    private static void showMenu() {
        System.out.println("Retail Company Menu:");
        System.out.println("1. Process Transaction");
        System.out.println("2. Display Customer Points");
        System.out.println("3. Exit");
        System.out.println();
    }

    public static void main(String[] args) {
        createInitialData();

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            showMenu();
            System.out.print("Enter your choice: ");
            try {
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        processTransaction();
                        break;
                    case 2:
                        displayCustomerPoints();
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.next();
                choice = 0;
            }
        } while (choice != 3);
    }
}