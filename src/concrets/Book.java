package concrets;

import abstracts.IBook;
import enums.BookGenre;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Book implements IBook {
    private int bookID;
    private static int nextID = 1;
    private String title, author;
    private BookGenre genre;
    private LocalDate publicationDate;
    private boolean isAvailable;
    public static List<Book> books = new ArrayList<>();


    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public static int getNextID() {
        return nextID;
    }

    public static void setNextID(int nextID) {
        Book.nextID = nextID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BookGenre getGenre() {
        return genre;
    }

    public void setGenre(BookGenre genre) {
        this.genre = genre;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }


    public Book(){

    }

    public Book(String title, String author, BookGenre genre, LocalDate publicationDate, boolean isAvailable) {
        this.bookID = nextID++;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationDate = publicationDate;
        this.isAvailable = isAvailable;
    }

    @Override
    public String toString() {
        return "{" +
                "bookID=" + bookID +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", publicationDate=" + publicationDate +
                ", isAvailable=" + isAvailable +
                '}';
    }

    static{
        books.add(new Book("Ismayilli", "Hasan Badalli", BookGenre.DRAM, LocalDate.of(2020, 1, 1), true));
        books.add(new Book("I am Civil", "Harry Jhon", BookGenre.DETECTIVE, LocalDate.of(2012, 1, 1), true));
        books.add(new Book("Advanced Java Programming", "Hasan Badalli", BookGenre.SCIENCE, LocalDate.of(2023, 1, 1), true));
        books.add(new Book("7 Color", "Heseny Bedellyy", BookGenre.SCIENCE, LocalDate.of(2024, 1, 1), false));
    }


    public static Book findBookById(int id){
        for(Book book : Book.books){
            if(book.getBookID() == id){
                return book;
            }
        }
        return null;
    }

    public static Book findUserBookById(int id){
        for(Book book : User.getUserBooks()){
            if(book.getBookID() == id){
                return book;
            }
        }
        return null;
    }


    @Override
    public void addBook(Book book) {
        books.add(book);
    }

    @Override
    public void deleteBook(Book book) {
        books.remove(book);
    }

    @Override
    public void updateBook(Book book) {
        book.setAvailable(!isAvailable);
    }

    @Override
    public void seeAllBooks() {
        for (int i = 0; i < books.size(); i++) {
            System.out.println(books.get(i).toString());
        }
    }
}
