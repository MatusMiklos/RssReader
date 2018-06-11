package com.example.pc.zadanie_cloudy.RestAPI;

import com.example.pc.zadanie_cloudy.Pojo.Article;
import com.example.pc.zadanie_cloudy.Pojo.User;
import com.example.pc.zadanie_cloudy.Pojo.Channels;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by pc on 30.11.2017.
 */

public interface RestApi {
    //login pouzivatela
    @GET("/api/users/")
    Call<User> authenticate(@Header("username") String username,
                            @Header("password") String password);

    //registracia pouzivatela
    @POST("/api/users/")
    Call<User> registration(@Body Map<String, String> body);

    //vsetky clanky daneho kanalu
    @GET("/api/websites/{id}/articles")
    Call<List<Article>> all(@Header("userId") int userId,
                            @Path("id") int id);

    //odbery prihlaseneho pouzivatela
    @GET("/api/websites/")
    Call<List<Channels>> getUserSubscripted(@Header("userId") int userId);

    //channely prihlaseneho pouzivatela, ktore este neodobera
    @GET("/api/websites/")
    Call<List<Channels>> getUserNotSubscripted(@Header("userId") int userId, @Header("limitSubscribed") int limitSubscribed);

    //unsubcribe
    @DELETE ("/api/users/websites/{websiteId}")
    Call<Channels> unsubscribe(@Header("userId") int userId,
                               @Path("websiteId") int websiteId);

    //subscribe
    @POST("/api/users/websites/{websiteId}")
    Call<Channels> subscribe(@Header("userId") int userId,
                            @Path("websiteId") int websiteId);

    //vytvorenie noveho channelu
    @POST("/api/websites/")
    Call<Channels> createNewChannel(@Body Map<String, String> body);
}
