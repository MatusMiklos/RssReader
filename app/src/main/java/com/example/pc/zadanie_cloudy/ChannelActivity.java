package com.example.pc.zadanie_cloudy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pc.zadanie_cloudy.Adapters.ArticleAdapter;
import com.example.pc.zadanie_cloudy.Pojo.Article;
import com.example.pc.zadanie_cloudy.RestAPI.RestApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChannelActivity extends AppCompatActivity {
    public static final String ENDPOINT_URL = "http://act7.azurewebsites.net/";
    RestApi restApi;
    int subscription;
    String title;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);

        Retrofit retrofit = new Retrofit.Builder(). baseUrl(ENDPOINT_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        //na zaklade informacie poslanej z MAIN obrazovky o aky odber sa jedna, zavolame funkciu
        //na nacitanie a zobrazenie clankov daneho odberu
        title = (String) getIntent().getSerializableExtra("title");
        setTitle(title);
        subscription = (int) getIntent().getSerializableExtra("subscription");
        userId = (int) getIntent().getSerializableExtra("userId");
        restApi = retrofit.create(RestApi.class);
        loadArticles(userId, subscription);
    }

    //funkcia pre nacitanie a nasledne zavolanie zobrazenia clankov za pomoci get requestu
    //parameter - id odberu
    private void loadArticles(int userId, int id){
        Call<List<Article>> call = restApi.all(userId, id);
        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                displayArticles(response.body());
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {

            }
        });
    }

    //funkcia pre konecne zobrazenie clankov zo spracovaneho tela requestu
    private void displayArticles(List<Article> articles){
        RecyclerView recyclerView;
        ArticleAdapter articleAdapter;
        recyclerView = (RecyclerView) findViewById(R.id.sportRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (articles != null){
            articleAdapter = new ArticleAdapter(articles);
            recyclerView.setAdapter(articleAdapter);
        }else{
            Toast.makeText(getApplicationContext(), "Problém pri načítaní článkov", Toast.LENGTH_SHORT).show();
        }
    }

    //vytyvorenie 3-bodkoveho menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //funkcia pre spracovanie kliknutia polozky v 3-bodkovom menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.menuLogout){
            Toast.makeText(this, "Odhlasovanie..", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChannelActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
