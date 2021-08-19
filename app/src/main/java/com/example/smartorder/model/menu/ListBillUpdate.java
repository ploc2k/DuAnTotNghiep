package com.example.smartorder.model.menu;

import com.example.smartorder.model.bill.BillOne;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListBillUpdate {
    @SerializedName("list_bill")
    @Expose
    private List<BillOne> billOneList;

    public List<BillOne> getBillOneList() {
        return billOneList;
    }

    public void setBillOneList(List<BillOne> billOneList) {
        this.billOneList = billOneList;
    }
}
