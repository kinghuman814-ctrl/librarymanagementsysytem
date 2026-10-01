package library;

import java.util.ArrayList;
import java.util.List;

/**
 * BookManager.java
 * Handles all operations related to Book records: add, view,
 * update, delete and search. Books are kept in memory in a list
 * and persisted to "books.txt" through FileStorage.
 */
public class BookManager {

    private static final String FILE_NAME = "books.txt";
    private FileStorage fileStorage;
    private List<Book> books;

    public BookManager(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
        this.books = new ArrayList<>();
        loadBooks();
    }

    /** Loads all books from the data file into memory. */
    private void loadBooks() {
        books.clear();
        List<String> lines = fileStorage.readAllLines(FILE_NAME);
        for (String line : lines) {
            Book book = Book.fromFileString(line);
            if (book != null) {
                books.add(book);
            }
        }
    }

    /** Saves the current in-memory list of books back to the data file. */
    private void saveBooks() {
        List<String> lines = new ArrayList<>();
        for (Book b : books) {
            lines.add(b.toFileString());
        }
        fileStorage.writeAllLines(FILE_NAME, lines);
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public boolean bookIdExists(String bookId) {
        for (Book b : books) {
            if (b.getBookId().equalsIgnoreCase(bookId)) {
                return true;
            }
        }
        return false;
    }

    public Book findBookById(String bookId) {
        for (Book b : books) {
            if (b.getBookId().equalsIgnoreCase(bookId)) {
                return b;
            }
        }
        return null;
    }

    /** Adds a new book. Returns false if the book ID already exists. */
    public boolean addBook(Book book) {
        if (bookIdExists(book.getBookId())) {
            return false;
        }
        books.add(book);
        saveBooks();
        return true;
    }

    /** Updates an existing book's details. Returns false if the book was not found. */
    public boolean updateBook(String bookId, String title, String author,
                               String category, int totalCopies) {
        Book book = findBookById(bookId);
        if (book == null) {
            return false;
        }
        int issuedCopies = book.getTotalCopies() - book.getAvailableCopies();
        if (totalCopies < issuedCopies) {
            // Cannot reduce total copies below the number currently issued out.
            return false;
        }
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        book.setTotalCopies(totalCopies);
        book.setAvailableCopies(totalCopies - issuedCopies);
        saveBooks();
        return true;
    }

    /** Deletes a book by ID. Returns false if the book was not found. */
    public boolean deleteBook(String bookId) {
        Book book = findBookById(bookId);
        if (book == null) {
            return false;
        }
        books.remove(book);
        saveBooks();
        return true;
    }

    /** Searches books whose title contains the given keyword (case-insensitive). */
    public List<Book> searchByTitle(String keyword) {
        List<Book> results = new ArrayList<>();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(b);
            }
        }
        return results;
    }

    /** Searches books whose author contains the given keyword (case-insensitive). */
    public List<Book> searchByAuthor(String keyword) {
        List<Book> results = new ArrayList<>();
        for (Book b : books) {
            if (b.getAuthor().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(b);
            }
        }
        return results;
    }

    /** Searches books belonging to the given category (case-insensitive). */
    public List<Book> searchByCategory(String keyword) {
        List<Book> results = new ArrayList<>();
        for (Book b : books) {
            if (b.getCategory().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(b);
            }
        }
        return results;
    }

    /** Returns only the books that currently have at least one available copy. */
    public List<Book> getAvailableBooks() {
        List<Book> results = new ArrayList<>();
        for (Book b : books) {
            if (b.isAvailable()) {
                results.add(b);
            }
        }
        return results;
    }

    public int getTotalBookTitles() {
        return books.size();
    }

    public int getTotalCopiesInLibrary() {
        int total = 0;
        for (Book b : books) {
            total += b.getTotalCopies();
        }
        return total;
    }

    public int getTotalAvailableCopies() {
        int total = 0;
        for (Book b : books) {
            total += b.getAvailableCopies();
        }
        return total;
    }

    /** Called by TransactionManager after a successful issue/return to persist stock changes. */
    public void persist() {
        saveBooks();
    }
}
