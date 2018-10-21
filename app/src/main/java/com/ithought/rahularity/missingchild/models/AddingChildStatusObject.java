package com.ithought.rahularity.missingchild.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddingChildStatusObject {

    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AddingChildStatusObject{" +
                "status='" + status + '\'' +
                '}';
    }
}
