package com.serpro.email;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import javax.mail.Message;

public class MainActivity extends AppCompatActivity {
    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MailService mailService = new MailService();
                try {


                    mailService.login("imap.nauta.cu","raulrosado91@nauta.cu", "Analia*91");
                    Integer cantidad = mailService.getMessageCount();

                    textView.setText("cantidad de correos: "+ cantidad.toString());
                    Toast.makeText(MainActivity.this, "cantidad de mensajes: "+ cantidad.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("mail", "cargo los mensages: "+cantidad.toString());

                    Message[] mensajes = mailService.getMessages();
                    for (int i=0;i<mensajes.length;i++)
                    {
                        System.out.println("From:"+mensajes[i].getFrom()[0].toString());
                        System.out.println("Subject:"+mensajes[i].getSubject());

                        Log.d("mail", "From:"+mensajes[i].getFrom()[0].toString());
                        Log.d("mail", "Subject:"+mensajes[i].getSubject());
                    }

                    mailService.logout();
                }catch (Exception e){
                    Log.d("mail", "error: "+e);
                }

            }
        });

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
