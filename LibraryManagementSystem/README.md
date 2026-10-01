# Library Management System — MIS Console Application

A console-based Library Management System written in Java, built to satisfy
the MIS Console Application Assignment requirements (topic #2 from the sheet).

## What it does

Manage books and members with book issue and return operations, including
availability checking, due dates, fine calculation, searching, and
issue/return history.

## Features (mapped to the assignment's Common Minimum Requirements)

| Requirement                 | How it's covered |
|------------------------------|-------------------|
| Console Application          | Pure text menu system in `Main.java`, no GUI. |
| Minimum Code Size             | ~1,580 lines of real, meaningful code across 9 files (no padding/blank-line inflation). |
| File Handling                 | Plain `.txt` files (`books.txt`, `members.txt`, `transactions.txt`) via `FileReader`/`BufferedReader` and `FileWriter`/`BufferedWriter` in `FileStorage.java`. No database. |
| Object-Oriented Programming   | `Book`, `Member`, `Transaction` are model classes with constructors and encapsulated fields; `BookManager`, `MemberManager`, `TransactionManager` encapsulate logic. |
| Record Management             | Full Add / View / Update / Delete for both Books and Members. |
| Search and Organization       | Search books by title, author, or category; search members by name. |
| Project-Specific Operations   | Issue book, return book, automatic fine calculation for overdue returns. |
| Validation                    | `Validator.java` rejects empty input, non-numeric input, duplicate IDs, invalid menu choices, and blocks unsafe deletes (e.g. deleting a book still on loan). |
| Reports / Summaries           | Library summary, overdue report, fine collection report, per-member borrowing history. |
| Program Structure              | Split into 9 classes by responsibility, not one giant `main()`. |

## Project structure

```
LibraryManagementSystem/
├── src/
│   └── library/
│       ├── Book.java              (model)
│       ├── Member.java            (model)
│       ├── Transaction.java       (model)
│       ├── Validator.java         (console input validation)
│       ├── FileStorage.java       (generic file read/write helpers)
│       ├── BookManager.java       (book CRUD + search)
│       ├── MemberManager.java     (member CRUD + search)
│       ├── TransactionManager.java (issue/return + fines + reports)
│       └── Main.java              (menu-driven UI, entry point)
└── data/                          (created automatically the first time you run it)
    ├── books.txt
    ├── members.txt
    └── transactions.txt
```

## How to compile and run

From inside the `LibraryManagementSystem` folder:

```bash
# Compile
javac -d bin src/library/*.java

# Run
java -cp bin library.Main
```

The program automatically creates a `data/` folder with the three text
files the first time it runs, so nothing needs to be set up manually.

## File formats (pipe-delimited `|`)

- `books.txt` — `bookId|title|author|category|totalCopies|availableCopies`
- `members.txt` — `memberId|name|contact|address`
- `transactions.txt` — `transactionId|bookId|memberId|issueDate|dueDate|returnDate|fineAmount|status`
  (`returnDate` is stored as `NA` while a book is still out)

## Business rules

- Loan period: **14 days** from the issue date.
- Fine: **Rs. 5 per day** late (configurable in `TransactionManager.java`
  via the `LOAN_PERIOD_DAYS` and `FINE_PER_DAY` constants).
- A book can't be deleted while any copy is still on loan.
- A member can't be deleted while they still have a book on loan.
- Reducing a book's total copies below the number currently on loan is blocked.

## Sample workflow to try

1. Book Management → Add New Book (e.g. ID `B001`, title, author, category, 3 copies).
2. Member Management → Add New Member (e.g. ID `M001`).
3. Issue / Return Book → Issue Book → enter `B001` and `M001`.
4. Reports → Library Summary, to see the issued count go up.
5. Issue / Return Book → Return Book → enter `B001` to see it returned
   (try changing your system date forward, or just re-run after the due
   date, to see the fine calculation in action).

## Notes

This was written and reviewed carefully for correctness (brace/paren
balance and logic verified by hand), but a JDK compiler wasn't available
in the environment it was written in to do a live `javac` build — please
compile it once on your machine before submitting, in case anything needs
a small tweak.
