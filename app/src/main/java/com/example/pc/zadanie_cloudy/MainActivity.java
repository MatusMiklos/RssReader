package com.example.pc.zadanie_cloudy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.zadanie_cloudy.Adapters.ChannelsAdapter;
import com.example.pc.zadanie_cloudy.Pojo.Channels;
import com.example.pc.zadanie_cloudy.RestAPI.RestApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String ENDPOINT_URL = "http://act7.azurewebsites.net/";
    RestApi restApi;
    int userId;
    ImageView subscribeChannel;
    AlertDialog dialog;
    View dialoglayout;
    LayoutInflater inflater;
    RecyclerView recyclerView;
    ChannelsAdapter channelsAdapter;
    EditText rss_url;
    ImageView subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Odbery");
        userId = (int) getIntent().getSerializableExtra("userId");
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder(). baseUrl(ENDPOINT_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        restApi = retrofit.create(RestApi.class);
        loadSubscripted(userId);

        subscribeChannel = (ImageView) findViewById(R.id.subcribeChannel);
        subscribeChannel.setOnClickListener(loadNotSubscribed(userId));
    }

    //poslanie dopytu pre zistenie, ktore channely pouzivatel neodobera
    private View.OnClickListener loadNotSubscribed(final int userId){
        return new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Call<List<Channels>> call = restApi.getUserNotSubscripted(userId, 1);
                call.enqueue(new Callback<List<Channels>>() {
                    @Override
                    public void onResponse(Call<List<Channels>> call, Response<List<Channels>> response) {
                        displayPopup(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Channels>> call, Throwable t) {

                    }
                });
            }
        };
    }

    //zobrazi popup channelov, ktore pouzivatel este neodbera
    private void displayPopup(final List<Channels> channels){
        inflater=getLayoutInflater();
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                loadSubscripted(userId);
            }
        });
        dialog = alertDialogBuilder.create();
        dialoglayout = null;
        dialoglayout = inflater.inflate(R.layout.subscribe_popup, null);
        recyclerView = (RecyclerView) dialoglayout.findViewById(R.id.subscribeRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        channelsAdapter = new ChannelsAdapter(channels, userId, restApi, 1);
        recyclerView.setAdapter(channelsAdapter);
        subscribe = (ImageView) dialoglayout.findViewById(R.id.subscribeImgView);
        rss_url = (EditText) dialoglayout.findViewById(R.id.rssUrlEdit);
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rss_url.getText().toString().trim().length() == 0){
                    Toast.makeText(getApplicationContext(), "Zadaj URL adresu.", Toast.LENGTH_SHORT).show();

                }else{
                    final Map<String, String> requestBody = new HashMap<>();
                    requestBody.put("rssUrl", rss_url.getText().toString());
                    Call<Channels> call = restApi.createNewChannel(requestBody);
                    call.enqueue(new Callback<Channels>() {
                        @Override
                        public void onResponse(Call<Channels> call, Response<Channels> response) {
                            Channels newChannel = response.body();
                            if (response.code() == 201){
                                Toast.makeText(getApplicationContext(), "Kanál vytvorený, odber prihlásený..", Toast.LENGTH_SHORT).show();
                                rss_url.setText("");
                                subscribeChannel(newChannel.getId());
                            }else{
                                Toast.makeText(getApplicationContext(), "Neplatná RSS url..", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Channels> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Neplatná RSS url..", Toast.LENGTH_SHORT).show();
                        }
                    });
                    }
            }
        });
        alertDialogBuilder.setView(dialoglayout);
        alertDialogBuilder.show();
    }

    //dopyt pre prihlasenie odberu
    public void subscribeChannel(int websiteId) {

        Call <Channels> call = restApi.subscribe(userId, websiteId);
        call.enqueue(new Callback<Channels>() {
            @Override
            public void onResponse(Call<Channels> call, Response<Channels> response) {
                Log.d("subs", "done");
            }

            @Override
            public void onFailure(Call<Channels> call, Throwable t) {

            }
        });
    }

    //zavolanie requestu na ziskanie zoznamu odberov pouzivatela
    private void loadSubscripted(int userId){
        Call<List<Channels>> call = restApi.getUserSubscripted(userId);
        call.enqueue(new Callback<List<Channels>>() {
            @Override
            public void onResponse(Call<List<Channels>> call, Response<List<Channels>> response) {
                displaySubsriptions(response.body());
            }

            @Override
            public void onFailure(Call<List<Channels>> call, Throwable t) {

            }
        });
    }

    //funkcia berie ako parameter list odberov, nasledne ich zobrazi
    private void displaySubsriptions(List<Channels> subsriptions){
        recyclerView = (RecyclerView) findViewById(R.id.mainRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        channelsAdapter = new ChannelsAdapter(subsriptions, userId, restApi, 0);
        recyclerView.setAdapter(channelsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.menuLogout){
            Toast.makeText(this, "Odhlasovanie..", Toast.LENGTH_SHORT).show();
            Intent loginScreen = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginScreen);
        }
        return super.onOptionsItemSelected(item);
    }
}
