package library;

/**
 * Member.java
 * Represents a library member who can borrow books.
 */
public class Member {

    private String memberId;
    private String name;
    private String contact;
    private String address;

    public Member(String memberId, String name, String contact, String address) {
        this.memberId = memberId;
        this.name = name;
        this.contact = contact;
        this.address = address;
    }

    // ---------- Getters ----------
    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }

    // ---------- Setters ----------
    public void setName(String name) {
        this.name = name;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Converts this Member into a delimited line for file storage.
     * Format: memberId|name|contact|address
     */
    public String toFileString() {
        return memberId + "|" + name + "|" + contact + "|" + address;
    }

    /**
     * Recreates a Member object from a delimited line read from the data file.
     */
    public static Member fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 4) {
            return null;
        }
        return new Member(parts[0], parts[1], parts[2], parts[3]);
    }

    @Override
    public String toString() {
        return String.format("%-8s %-22s %-15s %-25s", memberId, name, contact, address);
    }
}
