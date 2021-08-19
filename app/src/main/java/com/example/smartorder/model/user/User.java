
package com.example.smartorder.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("passWord")
    @Expose
    private String passWord;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("indentityCardNumber")
    @Expose
    private Integer indentityCardNumber;

    public User(String id, String passWord, String role, String fullName, String phone, String address, Integer age, String avatar, Integer indentityCardNumber) {
        this.id = id;
        this.passWord = passWord;
        this.role = role;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.age = age;
        this.avatar = avatar;
        this.indentityCardNumber = indentityCardNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getIndentityCardNumber() {
        return indentityCardNumber;
    }

    public void setIndentityCardNumber(Integer indentityCardNumber) {
        this.indentityCardNumber = indentityCardNumber;
    }


}
