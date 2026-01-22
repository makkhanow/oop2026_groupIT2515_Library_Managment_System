package edu.aitu.oop3.entities;

public class Member {
    private long id;
    private String fullName;
    private String phone;

    public Member() {}

    public Member(long id, String fullName, String phone) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
    }

    public Member(String fullName, String phone) {
        this(0, fullName, phone);
    }

    public long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }

    public void setId(long id) { this.id = id; }

    @Override
    public String toString() {
        return id + " | " + fullName + " | phone=" + phone;
    }
}
