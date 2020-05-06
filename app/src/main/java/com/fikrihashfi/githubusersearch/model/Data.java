package com.fikrihashfi.githubusersearch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("items")
    @Expose
    private List<Users> users;

    @SerializedName("total_count")
    @Expose
    private int totalCount;

    public List<Users> getItems() {
        return users;
    }

    public void setItems(List<Users> users) {
        this.users = users;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getSize() {
        return users.size();
    }
}
