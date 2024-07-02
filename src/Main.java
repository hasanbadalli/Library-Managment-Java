import concrets.Book;
import concrets.Transaction;
import concrets.User;
import enums.TransactionType;
import enums.UserRole;
import exceptions.CustomAuthException;

import java.time.LocalDate;
import java.util.Scanner;

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
                    8 --> Update password
                    9 --> Delete Account
                    0 --> Exit
                    """);
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
                    Book book1 = Book.findUserBookById(id);
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
                        Transaction transaction = new Transaction(userMember.getUserID(), id, LocalDate.now(), TransactionType.RETURN);
                        transaction.record(userMember, transaction);
                    }else{
                        System.out.println("There is no book for this id");
                    }
                }
                case 6 -> {
                    System.out.println();
                    Transaction transaction = new Transaction();
                    transaction.displayForUser(userMember);
                }
                case 8 -> {
                    System.out.println("Write your new password");
                    String newPassword = scan.next();
                    if(!newPassword.isEmpty()){
                        userMember.updateUser(userMember, newPassword);
                    }else {
                        System.out.println("Password cannot be empty");
                    }
                }
                case 9 -> {
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

                }

                case 0 -> {
                    run = false;
                    System.out.println("Exited");
                }

            }
        }
    }



}