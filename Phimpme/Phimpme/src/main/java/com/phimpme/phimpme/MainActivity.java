package com.phimpme.phimpme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button toAuthActivity = (Button) findViewById(R.id.mainActivityToAuthActivityButton);
        toAuthActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAuthActivity = new Intent();
                toAuthActivity.setClass(MainActivity.this, AuthActivity.class);
                startActivity(toAuthActivity);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_select) {
            Intent toSelectIntent = new Intent();
            toSelectIntent.setClass(this, SelectActivity.class);
            startActivity(toSelectIntent);
            return true;
        }
        if (id == R.id.action_settings) {
            // TODO: Implement the settings part
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}