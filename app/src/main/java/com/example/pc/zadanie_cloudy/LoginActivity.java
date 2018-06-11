package com.example.pc.zadanie_cloudy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pc.zadanie_cloudy.Pojo.User;
import com.example.pc.zadanie_cloudy.RestAPI.RestApi;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    public static final String ENDPOINT_URL = "http://act7.azurewebsites.net/";
    private RestApi restApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Prihlásenie");
        setContentView(R.layout.activity_login);
        OnClickRegisterTextView();
        OnClickLoginButton();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ENDPOINT_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        restApi = retrofit.create(RestApi.class);



        //notifikacie
        //nastavis cas kedy
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 24);
        calendar.set(Calendar.SECOND, 30);
        Intent intent = new Intent(this, Notification_reciever.class);

        //100 tam si mozes dat hocico len vsade kde je request code musi byt to iste
        //update current ze tento intent moze byt zmeneny v buducnosti napr menis cas abo content notifikacie
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //wake up ked je uzamknuty
        //interval dey ze kazdy den
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    //spusti obrazovku pre registraciu pouzivatela
    public void OnClickRegisterTextView(){
        TextView regTextView = (TextView) findViewById(R.id.registerText);
        regTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Registrácia...", Toast.LENGTH_SHORT).show();
                Intent registration = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registration);
            }
        });
    }

    //Stlacenie buttona na login, ak su obe edittexty naplnene, vola sa funkcia attemptToLogin
    public void OnClickLoginButton(){
        Button loginButton = (Button) findViewById(R.id.loginButton);
        final EditText loginEdit = (EditText) findViewById(R.id.loginEdit);
        final EditText passEdit = (EditText) findViewById(R.id.passEdit);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((loginEdit.getText().toString().trim().length() == 0) || (passEdit.getText().toString().trim().length() == 0))
                    Toast.makeText(getApplicationContext(), "Prihlásenie nebolo úspešné!", Toast.LENGTH_SHORT).show();
                else{
                    attemptToLogIn(loginEdit.getText().toString(), passEdit.getText().toString());
                }
            }
        });
    }

    //funkcia na login, kde posielam v HEADERY meno a heslo uzivatela databaze
    public void attemptToLogIn(String username, String password){
        Call<User> call = restApi.authenticate(username, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User mUser = response.body();
                if (response.code() == 200){
                    Log.d("login", mUser.getId().toString());
                    Toast.makeText(getApplicationContext(), "Prihlásenie úspešné! Vitaj!", Toast.LENGTH_SHORT).show();
                    Intent mainScreen = new Intent(LoginActivity.this, MainActivity.class);
                    mainScreen.putExtra("userId", mUser.getId());
                    startActivity(mainScreen);
                }else{
                    Toast.makeText(getApplicationContext(), "Zlý login alebo heslo", Toast.LENGTH_SHORT).show();
                }
            }

            //ak je zadane zle meno / heslo nepride ani odpoved
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Zlý login alebo heslo", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
