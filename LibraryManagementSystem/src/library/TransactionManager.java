package library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * TransactionManager.java
 * Handles issuing books to members, returning them, calculating
 * overdue fines, and producing transaction-related reports.
 * Transactions are kept in memory and persisted to
 * "transactions.txt" through FileStorage.
 */
public class TransactionManager {

    private static final String FILE_NAME = "transactions.txt";
    private static final int LOAN_PERIOD_DAYS = 14;   // books are due 14 days after issue
    private static final double FINE_PER_DAY = 5.0;   // fine charged per day overdue

    private FileStorage fileStorage;
    private List<Transaction> transactions;
    private int nextTransactionNumber;

    public TransactionManager(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
        this.transactions = new ArrayList<>();
        loadTransactions();
        this.nextTransactionNumber = computeNextTransactionNumber();
    }

    private void loadTransactions() {
        transactions.clear();
        List<String> lines = fileStorage.readAllLines(FILE_NAME);
        for (String line : lines) {
            Transaction t = Transaction.fromFileString(line);
            if (t != null) {
                transactions.add(t);
            }
        }
    }

    private void saveTransactions() {
        List<String> lines = new ArrayList<>();
        for (Transaction t : transactions) {
            lines.add(t.toFileString());
        }
        fileStorage.writeAllLines(FILE_NAME, lines);
    }

    /** Works out the next free running number for generated transaction IDs (T0001, T0002, ...). */
    private int computeNextTransactionNumber() {
        int max = 0;
        for (Transaction t : transactions) {
            String id = t.getTransactionId();
            if (id.startsWith("T")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > max) {
                        max = num;
                    }
                } catch (NumberFormatException ignored) {
                    // Non-standard IDs are ignored when computing the next number.
                }
            }
        }
        return max + 1;
    }

    private String generateTransactionId() {
        String id = String.format("T%04d", nextTransactionNumber);
        nextTransactionNumber++;
        return id;
    }

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    /**
     * Issues a book to a member. Assumes the caller has already confirmed
     * the book has an available copy and the member exists.
     * Returns the created Transaction.
     */
    public Transaction issueBook(Book book, Member member) {
        book.decreaseAvailableCopy();

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(LOAN_PERIOD_DAYS);
        String transactionId = generateTransactionId();

        Transaction transaction = new Transaction(transactionId, book.getBookId(),
                member.getMemberId(), issueDate, dueDate, null, 0.0, "ISSUED");

        transactions.add(transaction);
        saveTransactions();
        return transaction;
    }

    /**
     * Returns a book for the given active transaction, calculates any
     * overdue fine and updates the book's available copy count.
     * Returns the fine amount charged (0.0 if returned on time).
     */
    public double returnBook(Transaction transaction, Book book) {
        LocalDate returnDate = LocalDate.now();
        double fine = calculateFine(transaction.getDueDate(), returnDate);

        transaction.setReturnDate(returnDate);
        transaction.setFineAmount(fine);
        transaction.setStatus("RETURNED");

        book.increaseAvailableCopy();
        saveTransactions();
        return fine;
    }

    /** Calculates the fine owed based on how many days late the return is. */
    public double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
        if (daysLate <= 0) {
            return 0.0;
        }
        return daysLate * FINE_PER_DAY;
    }

    /** Finds the active (still issued, not yet returned) transaction for a given book ID. */
    public Transaction findActiveTransactionByBookId(String bookId) {
        for (Transaction t : transactions) {
            if (t.getBookId().equalsIgnoreCase(bookId) && !t.isReturned()) {
                return t;
            }
        }
        return null;
    }

    public Transaction findTransactionById(String transactionId) {
        for (Transaction t : transactions) {
            if (t.getTransactionId().equalsIgnoreCase(transactionId)) {
                return t;
            }
        }
        return null;
    }

    /** Returns every transaction currently in the "ISSUED" state. */
    public List<Transaction> getCurrentlyIssued() {
        List<Transaction> results = new ArrayList<>();
        for (Transaction t : transactions) {
            if (!t.isReturned()) {
                results.add(t);
            }
        }
        return results;
    }

    /** Returns every issued transaction whose due date has already passed. */
    public List<Transaction> getOverdueTransactions() {
        List<Transaction> results = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Transaction t : transactions) {
            if (!t.isReturned() && t.getDueDate().isBefore(today)) {
                results.add(t);
            }
        }
        return results;
    }

    /** Returns the full borrowing history for one member, most recent first is not required. */
    public List<Transaction> getHistoryForMember(String memberId) {
        List<Transaction> results = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getMemberId().equalsIgnoreCase(memberId)) {
                results.add(t);
            }
        }
        return results;
    }

    /** Sums up the fine amount collected across all returned transactions. */
    public double getTotalFinesCollected() {
        double total = 0.0;
        for (Transaction t : transactions) {
            if (t.isReturned()) {
                total += t.getFineAmount();
            }
        }
        return total;
    }

    public int getTotalIssuedCount() {
        return getCurrentlyIssued().size();
    }

    public int getTotalOverdueCount() {
        return getOverdueTransactions().size();
    }
}
