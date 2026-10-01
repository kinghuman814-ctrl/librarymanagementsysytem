package library;

/**
 * Book.java
 * Represents a single book record in the library.
 * Stores identity, catalogue and stock information for a book.
 */
public class Book {

    private String bookId;
    private String title;
    private String author;
    private String category;
    private int totalCopies;
    private int availableCopies;

    public Book(String bookId, String title, String author, String category,
                int totalCopies, int availableCopies) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    // ---------- Getters ----------
    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    // ---------- Setters ----------
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    // ---------- Stock helper operations ----------

    /** Reduces available copies by one when a book is issued. Returns true if successful. */
    public boolean decreaseAvailableCopy() {
        if (availableCopies > 0) {
            availableCopies--;
            return true;
        }
        return false;
    }

    /** Increases available copies by one when a book is returned. */
    public void increaseAvailableCopy() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }

    public boolean isAvailable() {
        return availableCopies > 0;
    }

    /**
     * Converts this Book object into a single delimited line for file storage.
     * Format: bookId|title|author|category|totalCopies|availableCopies
     */
    public String toFileString() {
        return bookId + "|" + title + "|" + author + "|" + category + "|"
                + totalCopies + "|" + availableCopies;
    }

    /**
     * Recreates a Book object from a delimited line read from the data file.
     */
    public static Book fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 6) {
            return null;
        }
        String id = parts[0];
        String title = parts[1];
        String author = parts[2];
        String category = parts[3];
        int total = Integer.parseInt(parts[4]);
        int available = Integer.parseInt(parts[5]);
        return new Book(id, title, author, category, total, available);
    }

    @Override
    public String toString() {
        return String.format("%-8s %-28s %-20s %-15s %-8d %-8d",
                bookId, title, author, category, totalCopies, availableCopies);
    }
}
