package com.serpro.email;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.mail.Message;

public class MainActivity extends AppCompatActivity{
    ConexionSQLiteHelper conn;
    Button button;
    TextView textView;
    BottomNavigationView bottomNavigation;
    String nombre, email, password;
    Chats fragmentChat;
    Personas fragmentPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conn = new ConexionSQLiteHelper(getApplicationContext());//conexion  con la bd

        fragmentChat = new Chats();
        fragmentPersonas = new Personas();
        getSupportFragmentManager().beginTransaction().add(R.id.contenedorFragment,fragmentChat).commit();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        button = findViewById(R.id.button);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        switch (item.getItemId()){
                            case R.id.menu_mensajes:

                                transaction.replace(R.id.contenedorFragment,fragmentChat);
                                break;
                            case R.id.menu_personas:
                                item.setEnabled(true);
                                transaction.replace(R.id.contenedorFragment,fragmentPersonas);
                                break;
                        }
                        transaction.commit();
                        return false;
                    }
                }
        );
    }

    @Override public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        MenuItem itemR,itemS;
        itemR = menu.findItem(R.id.menu_icreturn);
        itemS = menu.findItem(R.id.menu_setting);
        itemR.setVisible(false);
        itemS.setVisible(true);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem option_menu) {
        switch (option_menu.getItemId()) {
            case R.id.menu_setting:
                Intent intent;
                intent = new Intent(getApplicationContext(), Setting.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(option_menu);
        }
    }


}
