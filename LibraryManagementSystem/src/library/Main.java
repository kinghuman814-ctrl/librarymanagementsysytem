package library;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Main.java
 * Entry point of the Library Management System.
 * Displays the menu-driven console interface and delegates all
 * real work to BookManager, MemberManager and TransactionManager.
 */
public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static Validator validator = new Validator(scanner);
    private static FileStorage fileStorage = new FileStorage();
    private static BookManager bookManager = new BookManager(fileStorage);
    private static MemberManager memberManager = new MemberManager(fileStorage);
    private static TransactionManager transactionManager = new TransactionManager(fileStorage);

    public static void main(String[] args) {
        printWelcome();
        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = validator.readMenuChoice("Enter your choice: ", 1, 6);

            switch (choice) {
                case 1:
                    bookManagementMenu();
                    break;
                case 2:
                    memberManagementMenu();
                    break;
                case 3:
                    issueReturnMenu();
                    break;
                case 4:
                    searchMenu();
                    break;
                case 5:
                    reportsMenu();
                    break;
                case 6:
                    running = false;
                    System.out.println("\nThank you for using the Library Management System. Goodbye!");
                    break;
            }
        }
        scanner.close();
    }

    private static void printWelcome() {
        System.out.println("=================================================");
        System.out.println("      LIBRARY MANAGEMENT SYSTEM - MIS PROJECT   ");
        System.out.println("=================================================");
    }

    private static void printMainMenu() {
        System.out.println("\n----------------- MAIN MENU -----------------");
        System.out.println("1. Book Management");
        System.out.println("2. Member Management");
        System.out.println("3. Issue / Return Book");
        System.out.println("4. Search");
        System.out.println("5. Reports");
        System.out.println("6. Exit");
        System.out.println("-----------------------------------------------");
    }

    // =========================================================
    // BOOK MANAGEMENT
    // =========================================================

    private static void bookManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n------------- BOOK MANAGEMENT -------------");
            System.out.println("1. Add New Book");
            System.out.println("2. View All Books");
            System.out.println("3. Update Book");
            System.out.println("4. Delete Book");
            System.out.println("5. Back to Main Menu");
            System.out.println("--------------------------------------------");

            int choice = validator.readMenuChoice("Enter your choice: ", 1, 5);
            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    viewAllBooks();
                    break;
                case 3:
                    updateBook();
                    break;
                case 4:
                    deleteBook();
                    break;
                case 5:
                    back = true;
                    break;
            }
        }
    }

    private static void addBook() {
        System.out.println("\n-- Add New Book --");
        String bookId = validator.readNonEmptyString("Enter Book ID: ");

        if (bookManager.bookIdExists(bookId)) {
            System.out.println("A book with ID '" + bookId + "' already exists. Please use a different ID.");
            return;
        }

        String title = validator.readNonEmptyString("Enter Title: ");
        String author = validator.readNonEmptyString("Enter Author: ");
        String category = validator.readNonEmptyString("Enter Category: ");
        int totalCopies = validator.readPositiveInt("Enter Total Copies: ");

        Book book = new Book(bookId, title, author, category, totalCopies, totalCopies);
        boolean added = bookManager.addBook(book);

        if (added) {
            System.out.println("Book added successfully.");
        } else {
            System.out.println("Could not add book. The Book ID may already exist.");
        }
    }

    private static void viewAllBooks() {
        List<Book> books = bookManager.getAllBooks();
        System.out.println("\n-- All Books --");
        if (books.isEmpty()) {
            System.out.println("No books found in the library.");
            return;
        }
        printBookTableHeader();
        for (Book b : books) {
            System.out.println(b);
        }
        System.out.println("Total titles: " + bookManager.getTotalBookTitles());
    }

    private static void printBookTableHeader() {
        System.out.printf("%-8s %-28s %-20s %-15s %-8s %-8s%n",
                "ID", "Title", "Author", "Category", "Total", "Avail.");
        System.out.println("--------------------------------------------------------------------------------------");
    }

    private static void updateBook() {
        System.out.println("\n-- Update Book --");
        String bookId = validator.readNonEmptyString("Enter Book ID to update: ");
        Book book = bookManager.findBookById(bookId);

        if (book == null) {
            System.out.println("No book found with ID '" + bookId + "'.");
            return;
        }

        System.out.println("Current details: " + book);
        String title = validator.readNonEmptyString("Enter new Title: ");
        String author = validator.readNonEmptyString("Enter new Author: ");
        String category = validator.readNonEmptyString("Enter new Category: ");
        int totalCopies = validator.readPositiveInt("Enter new Total Copies: ");

        boolean updated = bookManager.updateBook(bookId, title, author, category, totalCopies);
        if (updated) {
            System.out.println("Book updated successfully.");
        } else {
            System.out.println("Update failed. Total copies cannot be less than copies currently issued out.");
        }
    }

    private static void deleteBook() {
        System.out.println("\n-- Delete Book --");
        String bookId = validator.readNonEmptyString("Enter Book ID to delete: ");
        Book book = bookManager.findBookById(bookId);

        if (book == null) {
            System.out.println("No book found with ID '" + bookId + "'.");
            return;
        }

        if (book.getAvailableCopies() < book.getTotalCopies()) {
            System.out.println("Cannot delete this book: one or more copies are still issued out.");
            return;
        }

        boolean confirm = validator.readYesNo("Are you sure you want to delete '" + book.getTitle() + "'?");
        if (confirm) {
            bookManager.deleteBook(bookId);
            System.out.println("Book deleted successfully.");
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    // =========================================================
    // MEMBER MANAGEMENT
    // =========================================================

    private static void memberManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n------------ MEMBER MANAGEMENT ------------");
            System.out.println("1. Add New Member");
            System.out.println("2. View All Members");
            System.out.println("3. Update Member");
            System.out.println("4. Delete Member");
            System.out.println("5. Back to Main Menu");
            System.out.println("--------------------------------------------");

            int choice = validator.readMenuChoice("Enter your choice: ", 1, 5);
            switch (choice) {
                case 1:
                    addMember();
                    break;
                case 2:
                    viewAllMembers();
                    break;
                case 3:
                    updateMember();
                    break;
                case 4:
                    deleteMember();
                    break;
                case 5:
                    back = true;
                    break;
            }
        }
    }

    private static void addMember() {
        System.out.println("\n-- Add New Member --");
        String memberId = validator.readNonEmptyString("Enter Member ID: ");

        if (memberManager.memberIdExists(memberId)) {
            System.out.println("A member with ID '" + memberId + "' already exists. Please use a different ID.");
            return;
        }

        String name = validator.readNonEmptyString("Enter Name: ");
        String contact = validator.readNonEmptyString("Enter Contact Number: ");
        String address = validator.readNonEmptyString("Enter Address: ");

        Member member = new Member(memberId, name, contact, address);
        boolean added = memberManager.addMember(member);

        if (added) {
            System.out.println("Member added successfully.");
        } else {
            System.out.println("Could not add member. The Member ID may already exist.");
        }
    }

    private static void viewAllMembers() {
        List<Member> members = memberManager.getAllMembers();
        System.out.println("\n-- All Members --");
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }
        printMemberTableHeader();
        for (Member m : members) {
            System.out.println(m);
        }
        System.out.println("Total members: " + memberManager.getTotalMembers());
    }

    private static void printMemberTableHeader() {
        System.out.printf("%-8s %-22s %-15s %-25s%n", "ID", "Name", "Contact", "Address");
        System.out.println("--------------------------------------------------------------------------");
    }

    private static void updateMember() {
        System.out.println("\n-- Update Member --");
        String memberId = validator.readNonEmptyString("Enter Member ID to update: ");
        Member member = memberManager.findMemberById(memberId);

        if (member == null) {
            System.out.println("No member found with ID '" + memberId + "'.");
            return;
        }

        System.out.println("Current details: " + member);
        String name = validator.readNonEmptyString("Enter new Name: ");
        String contact = validator.readNonEmptyString("Enter new Contact Number: ");
        String address = validator.readNonEmptyString("Enter new Address: ");

        memberManager.updateMember(memberId, name, contact, address);
        System.out.println("Member updated successfully.");
    }

    private static void deleteMember() {
        System.out.println("\n-- Delete Member --");
        String memberId = validator.readNonEmptyString("Enter Member ID to delete: ");
        Member member = memberManager.findMemberById(memberId);

        if (member == null) {
            System.out.println("No member found with ID '" + memberId + "'.");
            return;
        }

        List<Transaction> active = transactionManager.getCurrentlyIssued();
        for (Transaction t : active) {
            if (t.getMemberId().equalsIgnoreCase(memberId)) {
                System.out.println("Cannot delete this member: they still have one or more books issued out.");
                return;
            }
        }

        boolean confirm = validator.readYesNo("Are you sure you want to delete '" + member.getName() + "'?");
        if (confirm) {
            memberManager.deleteMember(memberId);
            System.out.println("Member deleted successfully.");
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    // =========================================================
    // ISSUE / RETURN
    // =========================================================

    private static void issueReturnMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n------------ ISSUE / RETURN BOOK ------------");
            System.out.println("1. Issue Book");
            System.out.println("2. Return Book");
            System.out.println("3. View Currently Issued Books");
            System.out.println("4. Back to Main Menu");
            System.out.println("----------------------------------------------");

            int choice = validator.readMenuChoice("Enter your choice: ", 1, 4);
            switch (choice) {
                case 1:
                    issueBook();
                    break;
                case 2:
                    returnBook();
                    break;
                case 3:
                    viewCurrentlyIssued();
                    break;
                case 4:
                    back = true;
                    break;
            }
        }
    }

    private static void issueBook() {
        System.out.println("\n-- Issue Book --");
        String bookId = validator.readNonEmptyString("Enter Book ID: ");
        Book book = bookManager.findBookById(bookId);
        if (book == null) {
            System.out.println("No book found with ID '" + bookId + "'.");
            return;
        }
        if (!book.isAvailable()) {
            System.out.println("Sorry, no copies of '" + book.getTitle() + "' are currently available.");
            return;
        }

        String memberId = validator.readNonEmptyString("Enter Member ID: ");
        Member member = memberManager.findMemberById(memberId);
        if (member == null) {
            System.out.println("No member found with ID '" + memberId + "'.");
            return;
        }

        Transaction transaction = transactionManager.issueBook(book, member);
        bookManager.persist();

        System.out.println("Book issued successfully.");
        System.out.println("Transaction ID : " + transaction.getTransactionId());
        System.out.println("Issue Date     : " + transaction.getIssueDate());
        System.out.println("Due Date       : " + transaction.getDueDate());
    }

    private static void returnBook() {
        System.out.println("\n-- Return Book --");
        String bookId = validator.readNonEmptyString("Enter Book ID being returned: ");

        Book book = bookManager.findBookById(bookId);
        if (book == null) {
            System.out.println("No book found with ID '" + bookId + "'.");
            return;
        }

        Transaction transaction = transactionManager.findActiveTransactionByBookId(bookId);
        if (transaction == null) {
            System.out.println("No active issue record found for this book. It may not currently be issued.");
            return;
        }

        double fine = transactionManager.returnBook(transaction, book);
        bookManager.persist();

        System.out.println("Book returned successfully.");
        System.out.println("Return Date : " + transaction.getReturnDate());
        if (fine > 0) {
            System.out.printf("This return was late. Fine charged: %.2f%n", fine);
        } else {
            System.out.println("Returned on time. No fine charged.");
        }
    }

    private static void viewCurrentlyIssued() {
        List<Transaction> issued = transactionManager.getCurrentlyIssued();
        System.out.println("\n-- Currently Issued Books --");
        if (issued.isEmpty()) {
            System.out.println("No books are currently issued out.");
            return;
        }
        printTransactionTableHeader();
        for (Transaction t : issued) {
            System.out.println(t);
        }
    }

    private static void printTransactionTableHeader() {
        System.out.printf("%-8s %-8s %-8s %-12s %-12s %-12s %-8s %-10s%n",
                "TxnID", "BookID", "MemID", "IssueDate", "DueDate", "ReturnDate", "Fine", "Status");
        System.out.println("--------------------------------------------------------------------------------------");
    }

    // =========================================================
    // SEARCH
    // =========================================================

    private static void searchMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----------------- SEARCH -----------------");
            System.out.println("1. Search Book by Title");
            System.out.println("2. Search Book by Author");
            System.out.println("3. Search Book by Category");
            System.out.println("4. Search Member by Name");
            System.out.println("5. Back to Main Menu");
            System.out.println("--------------------------------------------");

            int choice = validator.readMenuChoice("Enter your choice: ", 1, 5);
            switch (choice) {
                case 1: {
                    String keyword = validator.readNonEmptyString("Enter title keyword: ");
                    printBookResults(bookManager.searchByTitle(keyword));
                    break;
                }
                case 2: {
                    String keyword = validator.readNonEmptyString("Enter author keyword: ");
                    printBookResults(bookManager.searchByAuthor(keyword));
                    break;
                }
                case 3: {
                    String keyword = validator.readNonEmptyString("Enter category keyword: ");
                    printBookResults(bookManager.searchByCategory(keyword));
                    break;
                }
                case 4: {
                    String keyword = validator.readNonEmptyString("Enter member name keyword: ");
                    List<Member> results = memberManager.searchByName(keyword);
                    if (results.isEmpty()) {
                        System.out.println("No matching members found.");
                    } else {
                        printMemberTableHeader();
                        for (Member m : results) {
                            System.out.println(m);
                        }
                    }
                    break;
                }
                case 5:
                    back = true;
                    break;
            }
        }
    }

    private static void printBookResults(List<Book> results) {
        if (results.isEmpty()) {
            System.out.println("No matching books found.");
            return;
        }
        printBookTableHeader();
        for (Book b : results) {
            System.out.println(b);
        }
    }

    // =========================================================
    // REPORTS
    // =========================================================

    private static void reportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----------------- REPORTS -----------------");
            System.out.println("1. Library Summary");
            System.out.println("2. Overdue Books Report");
            System.out.println("3. Fine Collection Report");
            System.out.println("4. Member Borrowing History");
            System.out.println("5. Back to Main Menu");
            System.out.println("---------------------------------------------");

            int choice = validator.readMenuChoice("Enter your choice: ", 1, 5);
            switch (choice) {
                case 1:
                    librarySummaryReport();
                    break;
                case 2:
                    overdueReport();
                    break;
                case 3:
                    fineCollectionReport();
                    break;
                case 4:
                    memberHistoryReport();
                    break;
                case 5:
                    back = true;
                    break;
            }
        }
    }

    private static void librarySummaryReport() {
        System.out.println("\n-- Library Summary Report --");
        System.out.println("Total distinct book titles : " + bookManager.getTotalBookTitles());
        System.out.println("Total copies in library     : " + bookManager.getTotalCopiesInLibrary());
        System.out.println("Total copies available      : " + bookManager.getTotalAvailableCopies());
        System.out.println("Total registered members    : " + memberManager.getTotalMembers());
        System.out.println("Books currently issued out  : " + transactionManager.getTotalIssuedCount());
        System.out.println("Books currently overdue     : " + transactionManager.getTotalOverdueCount());
    }

    private static void overdueReport() {
        List<Transaction> overdue = transactionManager.getOverdueTransactions();
        System.out.println("\n-- Overdue Books Report (as of " + LocalDate.now() + ") --");
        if (overdue.isEmpty()) {
            System.out.println("No overdue books at this time.");
            return;
        }
        printTransactionTableHeader();
        for (Transaction t : overdue) {
            long daysLate = java.time.temporal.ChronoUnit.DAYS.between(t.getDueDate(), LocalDate.now());
            double projectedFine = transactionManager.calculateFine(t.getDueDate(), LocalDate.now());
            System.out.println(t + "   (" + daysLate + " days late, projected fine: "
                    + String.format("%.2f", projectedFine) + ")");
        }
    }

    private static void fineCollectionReport() {
        System.out.println("\n-- Fine Collection Report --");
        double total = transactionManager.getTotalFinesCollected();
        int count = 0;
        for (Transaction t : transactionManager.getAllTransactions()) {
            if (t.isReturned() && t.getFineAmount() > 0) {
                count++;
                System.out.println(t);
            }
        }
        if (count == 0) {
            System.out.println("No fines have been collected yet.");
        }
        System.out.printf("Total fines collected so far: %.2f%n", total);
    }

    private static void memberHistoryReport() {
        System.out.println("\n-- Member Borrowing History --");
        String memberId = validator.readNonEmptyString("Enter Member ID: ");
        Member member = memberManager.findMemberById(memberId);
        if (member == null) {
            System.out.println("No member found with ID '" + memberId + "'.");
            return;
        }

        List<Transaction> history = transactionManager.getHistoryForMember(memberId);
        System.out.println("History for: " + member.getName() + " (" + memberId + ")");
        if (history.isEmpty()) {
            System.out.println("This member has no borrowing history yet.");
            return;
        }
        printTransactionTableHeader();
        for (Transaction t : history) {
            System.out.println(t);
        }
    }
}
