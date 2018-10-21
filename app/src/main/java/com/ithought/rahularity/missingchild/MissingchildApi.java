package com.ithought.rahularity.missingchild;



import com.ithought.rahularity.missingchild.models.AddingChildStatusObject;
import com.ithought.rahularity.missingchild.models.UserObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MissingchildApi {

    @FormUrlEncoded
    @POST("compare/")
    Call<UserObject> compare(
            @Field("image") String image

    );

    @FormUrlEncoded
    @POST("getall/")
    Call<UserObject> getall();

    @FormUrlEncoded
    @POST("add/")
    Call<AddingChildStatusObject> add(

            @Field("image") String image,
            @Field("name") String name,
            @Field("age") String age,
            @Field("father_name") String father_name,
            @Field("nearest_police_station") String nearest_police_station,
            @Field("complexion") String complexion,
            @Field("contact") String contact,
            @Field("address") String address,
            @Field("date_of_missing") String date_of_missing,
            @Field("place_of_missing") String place_of_missing,
            @Field("height") String height,
            @Field("weight") String weight

    );

}
