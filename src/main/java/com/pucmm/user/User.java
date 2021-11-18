package com.pucmm.user;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.Objects;

@DatabaseTable(tableName = "user")
public class User {

    enum ROL {SELLER, CUSTOMER}

    @DatabaseField(generatedId = true, columnName = "uid")
    private int uid;
    @DatabaseField(columnName = "firstName", canBeNull = false)
    private String firstName;
    @DatabaseField(columnName = "lastName", canBeNull = false)
    private String lastName;
    @DatabaseField(columnName = "email", canBeNull = false)
    private String email;
    @DatabaseField(columnName = "password", canBeNull = false)
    private String password;
    @DatabaseField(columnName = "rol",canBeNull  = false)
    private ROL rol;
    @DatabaseField(columnName = "contact")
    private String contact;
    @DatabaseField(columnName = "birthday", dataType = DataType.DATE_STRING, format = "yyyy-MM-dd")
    private Date birthday;
    @DatabaseField(columnName = "photo")
    private String photo;

    User() {
        // all persisted classes must define a no-arg constructor with at least package visibility
    }

    public int getUid() {
        return uid;
    }

    public User setUid(int uid) {
        this.uid = uid;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public ROL getRol() {
        return rol;
    }

    public User setRol(ROL rol) {
        this.rol = rol;
        return this;
    }

    public String getContact() {
        return contact;
    }

    public User setContact(String contact) {
        this.contact = contact;
        return this;
    }

    public Date getBirthday() {
        return birthday;
    }

    public User setBirthday(Date birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public User setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getUid() == user.getUid();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUid());
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", rol=" + rol +
                ", contact='" + contact + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
