package abstracts;

import concrets.Book;
import concrets.User;

public interface IUser {
    void addUser(User user);
    void deleteUser(User user);
    void updateUser(User user, String newPassword);
    void borrowBook(Book book);
    void returnBook(Book book);

    void seeBorrowedBooks();
}
