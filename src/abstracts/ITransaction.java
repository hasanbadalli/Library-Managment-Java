package abstracts;

import concrets.Transaction;
import concrets.User;

public interface ITransaction {
    void record(User user, Transaction transaction);
    void display();
    void displayForUser(User user);
}
