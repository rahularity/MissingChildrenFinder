package com.ithought.rahularity.missingchild.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserObject {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("children")
    @Expose
    private List<Children> children;

    public List<Children> getChildren() {
        return children;
    }

    public void setChildren(List<Children> children) {
        this.children = children;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserObject{" +
                "status='" + status + '\'' +
                ", children=" + children +
                '}';
    }
}
