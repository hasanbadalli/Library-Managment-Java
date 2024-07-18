package customFuncInterface;

import concrets.Book;
import concrets.User;

public interface FindUserBookById {
    Book find(int id, User user);
}
