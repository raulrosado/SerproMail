package com.serpro.email;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AddPersona extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_persona);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        MenuItem itemR,itemS;
        itemR = menu.findItem(R.id.menu_icreturn);
        itemS = menu.findItem(R.id.menu_setting);
        itemR.setVisible(true);
        itemS.setVisible(false);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem option_menu) {
        switch (option_menu.getItemId()) {
            case R.id.menu_icreturn:
                Intent intent;
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(option_menu);
        }
    }
}
