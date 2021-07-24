package com.slowcoders.damds;

public class AppointmentItem {
    String name;
    String age;
    String gender;
    String bloodgroup;
    String symptoms;
    String phone;
    String date;
    String time;
    String doctorname;
    String doctorid;

    public AppointmentItem() {
    }

    public AppointmentItem(String name, String age, String gender, String bloodgroup, String symptoms, String phone, String date, String time, String doctorname, String doctorid) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bloodgroup = bloodgroup;
        this.symptoms = symptoms;
        this.phone = phone;
        this.date = date;
        this.time = time;
        this.doctorname = doctorname;
        this.doctorid = doctorid;
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

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDoctorname() {
        return doctorname;
    }

    public void setDoctorname(String doctorname) {
        this.doctorname = doctorname;
    }

    public String getDoctorid() {
        return doctorid;
    }

    public void setDoctorid(String doctorid) {
        this.doctorid = doctorid;
    }
}
