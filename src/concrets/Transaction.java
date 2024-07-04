package concrets;

import abstracts.ITransaction;
import enums.TransactionType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Transaction implements ITransaction {
    private int transactionID;
    private static int nextID = 1;
    private int userID;
    private int bookID;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private TransactionType transactionType;

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public Transaction(int userID, int bookID, TransactionType transactionType, LocalDate borrowDate) {
        this.transactionID = nextID++;
        this.userID = userID;
        this.bookID = bookID;
        this.transactionType = transactionType;
        this.borrowDate = borrowDate;
    }

    public Transaction(int userID, int bookID, TransactionType transactionType, LocalDate borrowDate, LocalDate returnDate) {
        this.transactionID = nextID++;
        this.userID = userID;
        this.bookID = bookID;
        this.transactionType = transactionType;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public Transaction() {

    }


    private static HashMap<User, ArrayList<Transaction>> userTransactions = new HashMap<>();

    public static LocalDate getBorrowDate(User user, int bookID) {
        ArrayList<Transaction> transactions = userTransactions.get(user);
        if (transactions != null) {
            for (Transaction transaction : transactions) {
                if (transaction.bookID == bookID) {
                    return transaction.getBorrowDate();
                }
            }
        }
        return null;
    }

    @Override
    public void record(User user,Transaction transaction) {
        userTransactions.putIfAbsent(user, new ArrayList<>());
        userTransactions.get(user).add(transaction);
    }

    @Override
    public void display() {
        userTransactions.forEach((user, transactions) -> {
            System.out.println("User: " + user.getEmail());
            transactions.forEach(System.out::println);
        });
    }

    @Override
    public void displayForUser(User user) {
        ArrayList<Transaction> transactions = userTransactions.get(user);
        if (transactions != null) {
            System.out.println("Transactions for user: " + user.getName());
            transactions.forEach(System.out::println);
        } else {
            System.out.println("No transactions found ");
        }
    }

    @Override
    public String toString() {
        return "{" +
                "transactionID=" + transactionID +
                ", userID=" + userID +
                ", bookID=" + bookID +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                ", transactionType=" + transactionType +
                '}';
    }
}
