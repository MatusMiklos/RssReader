package com.example.pc.zadanie_cloudy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pc.zadanie_cloudy.Pojo.User;
import com.example.pc.zadanie_cloudy.RestAPI.RestApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    public static final String ENDPOINT_URL = "http://act7.azurewebsites.net/";
    private RestApi restApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Registrácia");
        setContentView(R.layout.activity_register);
        onClickButtonRegister();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ENDPOINT_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        restApi = retrofit.create(RestApi.class);
    }

    public void onClickButtonRegister(){
        Button backButton = (Button)findViewById(R.id.registerButton);
        final EditText usernameEdit = (EditText) findViewById(R.id.usernameEdit);
        final EditText passEdit = (EditText) findViewById(R.id.passEdit);
        final EditText passRepeatEdit = (EditText) findViewById(R.id.passRepeatEdit);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEditViews(usernameEdit, passEdit, passRepeatEdit)){
                     //zmenit na mupltiple parameters v body h
                    registerRetrofit(usernameEdit.getText().toString(), passEdit.getText().toString());
                }
            }
        });
    }

    //pre kontrolu formulara, ci su pozadovane polia spravne naplnene
    public boolean checkEditViews(EditText username, EditText password, EditText passwordRepeat){
        if (!password.getText().toString().equals(passwordRepeat.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Heslá sa nezhodujú!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (username.getText().toString().trim().length() > 0 &&
                password.getText().toString().trim().length() > 0)
            if (password.getText().toString().trim().length() > 4)
                return true;
            else {
                Toast.makeText(getApplicationContext(), "Dĺžka hesla musí byť minimálne 5 znakov!", Toast.LENGTH_SHORT).show();
                return false;
            }

        else {
            Toast.makeText(getApplicationContext(), "Všetky polia musia byt vyplnené!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //funkcia na registraciu prostrednictvom post requestu z api
    //dava stale NULL!!!!
    public void registerRetrofit(String username, String password){
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", username);
        requestBody.put("password", password);
        Call<User> call = restApi.registration(requestBody);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(getApplicationContext(), "Registrácia úspešná! Môžeš sa prihlásiť.", Toast.LENGTH_LONG).show();
                Intent loginScreen = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginScreen);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Chyba, skotroluj pripojenie na internet!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
