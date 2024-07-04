import concrets.Book;
import concrets.Transaction;
import concrets.User;
import enums.BookGenre;
import enums.TransactionType;
import enums.UserRole;
import exceptions.CustomAuthException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("""
                ----------------------
                Welcome to our library
                ----------------------
                """);
        boolean run = true;
        while (run){

            System.out.println("""
                    Select one of them
                    1 --> Register
                    2 --> Login
                    0 --> Exit
                    """);
            int option = scan.nextInt();
            switch(option){
                case 1 -> {
                    register();
                }
                case 2 -> {
                    login();
                }
                case 0->{
                    run = false;
                    System.out.println("Exited");
                }
            }
        }

    }

    static void register(){
        System.out.println("Write your name");
        String name = scan.next();
        System.out.println("Write your email");
        String email = scan.next();
        System.out.println("Write your password");
        String password = scan.next();

        System.out.println("""
                Write your userRole:
                1.Librarian
                2.Member
                """);

        try {
            UserRole userRole = UserRole.valueOf(scan.next().toUpperCase());
            User newUser = new User(email);
            if (User.userMap.contains(newUser)) {
                System.out.println("An account with this email already exists.");
            } else {
                newUser = new User(name,email, password, userRole);
                newUser.addUser(newUser);
                System.out.println("User registered successfully.");
            }
        }catch (IllegalArgumentException e){
            System.out.println("""
                        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        Please write one of the given ones
                        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    """);

        }

        System.out.println(User.userMap.toString());

    }

    static void login(){
        System.out.println("Write your email");
        String email = scan.next();

        System.out.println("Write your password");
        String password = scan.next();

        try{
            boolean emailStatus = false;
            boolean passwordStatus = false;

            for (User user1 : User.userMap) {
                if(user1.getEmail().equals(email)){
                    emailStatus = true;
                    if(user1.getPassword().equals(password)){
                        passwordStatus = true;
                        System.out.println("Your login is successful");
                        if(user1.getUserRole() == UserRole.MEMBER){
                            member(user1);
                        }else if(user1.getUserRole() == UserRole.LIBRARIAN){
                            librarian(user1);
                        }

                        break;
                    }else{
                        passwordStatus = false;
                        break;
                    }
                }
            }
            if(!emailStatus){
                throw new CustomAuthException("""
                        !!!!!!!!!!!!!!!!!!!!!!!!!
                        This email does not exist
                        !!!!!!!!!!!!!!!!!!!!!!!!!
                        """);
            }
            else if(!passwordStatus){
                throw new CustomAuthException("""
                        !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        Your Password does not match
                        !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        """);
            }
        }catch (CustomAuthException e){
            System.out.println(e.getMessage());
        }

    }

    static void member(User userMember){
        System.out.printf("""
                ----------------------
                Welcome to Library, %s
                ----------------------
                \n
                """, userMember.getName());

        boolean run = true;
        while (run){


            System.out.println("""
                    Select one of them
                    1 --> See account info
                    2 --> See All Books
                    3 --> Borrow Books
                    4 --> See Borrowed Books
                    5 --> Return Book
                    6 --> See your Transactions History
                    7 --> Search Book by Name
                    8 --> Filter By Genre
                    9 --> Update password
                    10 --> Delete Account
                    0 --> Exit
                    """);
            borrowedBookChecker(userMember);
            int option = scan.nextInt();
            switch(option){
                case 1 -> {
                    System.out.println(userMember);
                }case 2 -> {
                    Book book = new Book();
                    book.seeAllBooks();
                    System.out.println();
                }case 3->{
                    Book book = new Book();
                    book.seeAllBooks();
                    System.out.println();
                    System.out.println("Please write book id which you want");
                    int id = scan.nextInt();
                    Book book1 = Book.findBookById(id);
                    if(book1 != null){
                        if(book1.isAvailable()){
                            userMember.borrowBook(book1);
                            Transaction transaction = new Transaction(userMember.getUserID(), id, TransactionType.BORROW, LocalDate.now());
                            transaction.record(userMember, transaction);
                        }else{
                            System.out.println("This book is not avialable");
                        }
                    }else{
                        System.out.println("There is no book for this id");
                    }
                }case 4->{
                    userMember.seeBorrowedBooks();
                }
                case 5->{
                    System.out.println();
                    userMember.seeBorrowedBooks();
                    System.out.println("Please write book id which you want to return");
                    int id = scan.nextInt();
                    Book book = Book.findUserBookById(id);
                    if(book != null){
                        userMember.returnBook(book);
                        LocalDate borrowDate = Transaction.getBorrowDate(userMember, book.getBookID());
                        Transaction transaction = new Transaction(userMember.getUserID(), id, TransactionType.RETURN, borrowDate, LocalDate.now());
                        transaction.record(userMember, transaction);
                        userMember.getTimeExpiredBooks().remove(book);
                    }else{
                        System.out.println("There is no book for this id");
                    }
                }
                case 6 -> {
                    System.out.println();
                    Transaction transaction = new Transaction();
                    transaction.displayForUser(userMember);
                }
                case 7 -> {
                    System.out.println("Write Book name for searching(when you write 'is' , Ism, Ismayilli,Ismayilli City etc. will show");
                    String bookName = scan.next();
                    boolean isBookFindded = false;
                    for (Book book : Book.books) {
                        if(book.getTitle().toLowerCase().contains(bookName.toLowerCase())){
                            System.out.println(book);
                            isBookFindded = true;
                        }
                    }
                    if(!isBookFindded){
                        System.out.println("There is no book like this name");
                    }
                }
                case 8 -> {
                    System.out.println("Write genre for sorting");
                    System.out.println("Genres: Fiction, Nonfiction, Science, Art, Dram, Dedective");
                    try{
                        BookGenre bookGenre = BookGenre.valueOf(scan.next().toUpperCase());
                        List<Book> sortedBook = Book.books.stream().filter(book -> book.getGenre() == bookGenre).toList();
                        for (Book book : sortedBook) {
                            System.out.println(book);
                        }
                        if (sortedBook.isEmpty()){
                            System.out.println("There is no book for this genre");
                        }
                    }catch (IllegalArgumentException e){
                        System.out.println("Please write genre correctly");
                    }

                }
                case 9 -> {
                    System.out.println("Write your new password");
                    String newPassword = scan.next();
                    if(!newPassword.isEmpty()){
                        userMember.updateUser(userMember, newPassword);
                    }else {
                        System.out.println("Password cannot be empty");
                    }
                }
                case 10 -> {
                    userMember.deleteUser(userMember);
                    run = false;
                    System.out.println("Your account deleted successfully");
                }
                case 0 -> {
                    run = false;
                    System.out.println("Exited");
                }

            }
        }
    }

    static void librarian(User targetUser){
        System.out.printf("""
                ----------------------
                Welcome to Library, %s
                ----------------------
                \n
                """, targetUser.getName());

        boolean run = true;
        while (run){

            System.out.println("""
                    Select one of them
                    1 --> See All Users
                    2 --> See All Users Transactions
                    3 --> Add Book
                    4 --> Delete Book
                    5 --> Update Book
                    6 --> See All Book
                    8 --> Update password
                    9 --> Delete Account
                    0 --> Exit
                    """);
            int option = scan.nextInt();
            switch(option){
                case 1 -> {
                    System.out.println(User.
                            userMap.toString());
                }
                case 2 -> {
                    Transaction transaction = new Transaction();
                    transaction.display();
                }

                case 3 -> {
                    System.out.println("Write Book Title");
                    String title = scan.next();
                    System.out.println("Write Author Name");
                    String author = scan.next();
                    System.out.println("Write Book Genre");
                    BookGenre bookGenre = BookGenre.valueOf(scan.next().toUpperCase());
                    System.out.println("Avialable or no avialable?(Wrire true or false)");
                    boolean isavialable = scan.nextBoolean();
                    Book book = new Book(title, author, bookGenre, LocalDate.now(), isavialable);
                    book.addBook(book);
                    System.out.println("Book is added successfully");
                }
                case 4 -> {
                    Book book = new Book();
                    book.seeAllBooks();
                    System.out.println("Write book id which you want remove");
                    int id = scan.nextInt();
                    Book book1= Book.findBookById(id);
                    if(book1 != null){
                        book.deleteBook(book1);

                        System.out.println("Book is removed successfully");
                    }else{
                        System.out.println("There is no book for this id");
                    }
                }
                case 5 -> {
                    Book book = new Book();
                    book.seeAllBooks();
                    System.out.println("Write Book id which you want update avialable status");
                    int id = scan.nextInt();
                    Book book1= Book.findBookById(id);
                    if(book1 != null){
                        book1.updateBook(book1);
                        System.out.println("Book status is updated successfully");
                    }else{
                        System.out.println("There is no book for this id");
                    }
                }case 6 -> {
                    Book book = new Book();
                    book.seeAllBooks();
                }
                case 8 -> {
                    System.out.println("Write your new password");
                    String newPassword = scan.next();
                    if(!newPassword.isEmpty()){
                        targetUser.updateUser(targetUser, newPassword);
                    }else {
                        System.out.println("Password cannot be empty");
                    }
                }
                case 9 -> {
                    targetUser.deleteUser(targetUser);
                    run = false;
                    System.out.println("Your account deleted successfully");
                }
                case 0 -> {
                    run = false;
                    System.out.println("Exited");
                }

            }
        }
    }

    static void borrowedBookChecker(User user){
        ArrayList<Transaction> transactions = Transaction.getUserTransactions().get(user);

        if (transactions != null) {
            int lateFee = 0;
            for (Transaction transaction : transactions) {
                long daysBorrowed = ChronoUnit.DAYS.between(transaction.getBorrowDate(), LocalDate.now());
                if (daysBorrowed > 14) {
                    user.getTimeExpiredBooks().add(Book.findUserBookById(transaction.getBookID()));
                    lateFee += 10;

                }else if(daysBorrowed > 7){
                    user.getTimeExpiredBooks().add(Book.findUserBookById(transaction.getBookID()));
                    lateFee += 5;
                }
            }
            if(lateFee != 0){
                System.out.println("The book you borrowed has expired, you must return it. And you must pay " + lateFee);
            }
            user.setLateFee(lateFee);
        }
    }


}