
package com.example.smartorder.model.bill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillOne {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("billCode")
    @Expose
    private String billCode;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("sl")
    @Expose
    private Integer sl;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("totalMoney")
    @Expose
    private Integer totalMoney;

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getSl() {
        return sl;
    }

    public void setSl(Integer sl) {
        this.sl = sl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Integer totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BillOne(String id, String billCode, String image, Integer sl, String name, Integer price, String type, Integer totalMoney) {
        this.id = id;
        this.billCode = billCode;
        this.image = image;
        this.sl = sl;
        this.name = name;
        this.price = price;
        this.type = type;
        this.totalMoney = totalMoney;
    }
}
