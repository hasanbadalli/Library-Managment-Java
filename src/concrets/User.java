package concrets;

import abstracts.IUser;
import enums.UserRole;

import java.util.*;

public class User implements IUser {
    private int userID;
    private static int nextID=1;
    private String name, adress, phone, email, password;
    private UserRole userRole;
    private int lateFee;
    public static Set<User> userMap = new HashSet<>();
    private List<Book> userBooks = new ArrayList<>();
    private List<Book> timeExpiredBooks = new ArrayList<>();
    private List<Book> favouriteBooks = new ArrayList<>();

    public List<Book> getUserBooks() {
        return new ArrayList<>(userBooks);
    }
    public List<Book> getFavouriteBooks() {
        return favouriteBooks;
    }

    public int getLateFee() {
        return lateFee;
    }

    public void setLateFee(int lateFee) {
        this.lateFee = lateFee;
    }

    public List<Book> getTimeExpiredBooks() {
        return timeExpiredBooks;
    }

    public User(String name, String email, String password, UserRole userRole){
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.userID = nextID++;
    }


    public User(String name, String adress, String phone, String email, String password, UserRole userRole) {
        this.userID = nextID++;
        this.name = name;
        this.adress = adress;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }
    public User() {

    }


    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }



    public User(String email){
        this.email = email;
    }

    @Override
    public void addUser(User user) {
        userMap.add(user);
    }

    @Override
    public void deleteUser(User user) {
        userMap.remove(user);
    }

    @Override
    public void updateUser(User user, String newPassword) {
        userMap.stream()
                .filter(targetUser -> targetUser.equals(user))
                .findFirst()
                .ifPresent(targetUser -> targetUser.setPassword(newPassword));
    }
    @Override
    public void borrowBook(Book book){
        userBooks.add(book);
        System.out.println("Book is successfully added");
    }

    @Override
    public void returnBook(Book book){
        userBooks.remove(book);
        System.out.println("The Book is returned successfully");
    }


    public void seeBorrowedBooks(){
        for (Book userBook : userBooks) {
            System.out.println(userBook);
        }
        System.out.println();
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                ", adress='" + adress + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userRole=" + userRole + '\'' +
                ", lateFee=" + lateFee +
                '}';
    }
}
