package library;

import java.time.LocalDate;

/**
 * Transaction.java
 * Represents a single book issue/return transaction between
 * a Member and a Book. Also tracks fine information.
 */
public class Transaction {

    private String transactionId;
    private String bookId;
    private String memberId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;   // null while the book is still out
    private double fineAmount;
    private String status;          // "ISSUED" or "RETURNED"

    public Transaction(String transactionId, String bookId, String memberId,
                        LocalDate issueDate, LocalDate dueDate, LocalDate returnDate,
                        double fineAmount, String status) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fineAmount = fineAmount;
        this.status = status;
    }

    // ---------- Getters ----------
    public String getTransactionId() {
        return transactionId;
    }

    public String getBookId() {
        return bookId;
    }

    public String getMemberId() {
        return memberId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public String getStatus() {
        return status;
    }

    // ---------- Setters ----------
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isReturned() {
        return "RETURNED".equalsIgnoreCase(status);
    }

    /**
     * Converts this Transaction into a delimited line for file storage.
     * A missing return date is stored as the literal text "NA".
     * Format: transactionId|bookId|memberId|issueDate|dueDate|returnDate|fineAmount|status
     */
    public String toFileString() {
        String returnDateText = (returnDate == null) ? "NA" : returnDate.toString();
        return transactionId + "|" + bookId + "|" + memberId + "|" + issueDate + "|"
                + dueDate + "|" + returnDateText + "|" + fineAmount + "|" + status;
    }

    /**
     * Recreates a Transaction object from a delimited line read from the data file.
     */
    public static Transaction fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 8) {
            return null;
        }
        String id = parts[0];
        String bookId = parts[1];
        String memberId = parts[2];
        LocalDate issueDate = LocalDate.parse(parts[3]);
        LocalDate dueDate = LocalDate.parse(parts[4]);
        LocalDate returnDate = parts[5].equals("NA") ? null : LocalDate.parse(parts[5]);
        double fine = Double.parseDouble(parts[6]);
        String status = parts[7];
        return new Transaction(id, bookId, memberId, issueDate, dueDate, returnDate, fine, status);
    }

    @Override
    public String toString() {
        String returnText = (returnDate == null) ? "-" : returnDate.toString();
        return String.format("%-8s %-8s %-8s %-12s %-12s %-12s %-8.2f %-10s",
                transactionId, bookId, memberId, issueDate, dueDate, returnText, fineAmount, status);
    }
}
