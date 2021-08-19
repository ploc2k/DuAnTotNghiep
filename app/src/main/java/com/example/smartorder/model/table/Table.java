
package com.example.smartorder.model.table;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Table {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("tableCode")
    @Expose
    private Integer tableCode;
    @SerializedName("tableSeats")
    @Expose
    private Integer tableSeats;
    @SerializedName("status")
    @Expose
    private boolean status;

    public Table(String id, Integer tableCode, Integer tableSeats, boolean status) {
        this.id = id;
        this.tableCode = tableCode;
        this.tableSeats = tableSeats;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTableCode() {
        return tableCode;
    }

    public void setTableCode(Integer tableCode) {
        this.tableCode = tableCode;
    }

    public Integer getTableSeats() {
        return tableSeats;
    }

    public void setTableSeats(Integer tableSeats) {
        this.tableSeats = tableSeats;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
