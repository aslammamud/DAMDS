package com.slowcoders.damds;

public class UserItem {
    String name;
    String age;
    String gender;
    String bloodgroup;
    String phone;
    String password;
    String address;

    public UserItem() {
    }

    public UserItem(String name, String age, String gender, String bloodgroup, String phone, String password, String address) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bloodgroup = bloodgroup;
        this.phone = phone;
        this.password = password;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
