package library;

import java.util.ArrayList;
import java.util.List;

/**
 * MemberManager.java
 * Handles all operations related to Member records: add, view,
 * update, delete and search. Members are kept in memory in a
 * list and persisted to "members.txt" through FileStorage.
 */
public class MemberManager {

    private static final String FILE_NAME = "members.txt";
    private FileStorage fileStorage;
    private List<Member> members;

    public MemberManager(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
        this.members = new ArrayList<>();
        loadMembers();
    }

    private void loadMembers() {
        members.clear();
        List<String> lines = fileStorage.readAllLines(FILE_NAME);
        for (String line : lines) {
            Member member = Member.fromFileString(line);
            if (member != null) {
                members.add(member);
            }
        }
    }

    private void saveMembers() {
        List<String> lines = new ArrayList<>();
        for (Member m : members) {
            lines.add(m.toFileString());
        }
        fileStorage.writeAllLines(FILE_NAME, lines);
    }

    public List<Member> getAllMembers() {
        return members;
    }

    public boolean memberIdExists(String memberId) {
        for (Member m : members) {
            if (m.getMemberId().equalsIgnoreCase(memberId)) {
                return true;
            }
        }
        return false;
    }

    public Member findMemberById(String memberId) {
        for (Member m : members) {
            if (m.getMemberId().equalsIgnoreCase(memberId)) {
                return m;
            }
        }
        return null;
    }

    /** Adds a new member. Returns false if the member ID already exists. */
    public boolean addMember(Member member) {
        if (memberIdExists(member.getMemberId())) {
            return false;
        }
        members.add(member);
        saveMembers();
        return true;
    }

    /** Updates an existing member's details. Returns false if the member was not found. */
    public boolean updateMember(String memberId, String name, String contact, String address) {
        Member member = findMemberById(memberId);
        if (member == null) {
            return false;
        }
        member.setName(name);
        member.setContact(contact);
        member.setAddress(address);
        saveMembers();
        return true;
    }

    /** Deletes a member by ID. Returns false if the member was not found. */
    public boolean deleteMember(String memberId) {
        Member member = findMemberById(memberId);
        if (member == null) {
            return false;
        }
        members.remove(member);
        saveMembers();
        return true;
    }

    /** Searches members whose name contains the given keyword (case-insensitive). */
    public List<Member> searchByName(String keyword) {
        List<Member> results = new ArrayList<>();
        for (Member m : members) {
            if (m.getName().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(m);
            }
        }
        return results;
    }

    public int getTotalMembers() {
        return members.size();
    }
}
