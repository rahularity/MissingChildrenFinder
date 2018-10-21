package com.ithought.rahularity.missingchild.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Children {

    @SerializedName("image_url")
    @Expose
    private String image_url;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("father_name")
    @Expose
    private String father;

    @SerializedName("contact")
    @Expose
    private String contact;

    @SerializedName("age")
    @Expose
    private String age;

    @SerializedName("nearest_police_station")
    @Expose
    private String nearest_police_station;

    @SerializedName("date_of_missing")
    @Expose
    private String date_of_missing;

    @SerializedName("place_of_missing")
    @Expose
    private String place_of_missing;

    @SerializedName("height")
    @Expose
    private String height;

    @SerializedName("weight")
    @Expose
    private String weight;

    @SerializedName("complexion")
    @Expose
    private String complexion;

    @SerializedName("address")
    @Expose
    private String address;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getNearest_police_station() {
        return nearest_police_station;
    }

    public void setNearest_police_station(String nearest_police_station) {
        this.nearest_police_station = nearest_police_station;
    }

    public String getDate_of_missing() {
        return date_of_missing;
    }

    public void setDate_of_missing(String date_of_missing) {
        this.date_of_missing = date_of_missing;
    }

    public String getPlace_of_missing() {
        return place_of_missing;
    }

    public void setPlace_of_missing(String place_of_missing) {
        this.place_of_missing = place_of_missing;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getComplexion() {
        return complexion;
    }

    public void setComplexion(String complexion) {
        this.complexion = complexion;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Children{" +
                "image_url='" + image_url + '\'' +
                ", name='" + name + '\'' +
                ", father='" + father + '\'' +
                ", contact='" + contact + '\'' +
                ", age='" + age + '\'' +
                ", nearest_police_station='" + nearest_police_station + '\'' +
                ", date_of_missing='" + date_of_missing + '\'' +
                ", place_of_missing='" + place_of_missing + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", complexion='" + complexion + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
