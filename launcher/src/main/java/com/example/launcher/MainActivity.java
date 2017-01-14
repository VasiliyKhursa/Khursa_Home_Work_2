package com.example.launcher;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btnCall = (ImageButton) findViewById(R.id.btnCall);
        ImageButton btnSms = (ImageButton) findViewById(R.id.btnSms);
        Button btnApp = (Button) findViewById(R.id.btnApp);

        btnCall.setOnClickListener(this);
        btnApp.setOnClickListener(this);
        btnSms.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.itemSettings:
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();

        switch (v.getId()) {
            case R.id.btnCall:
                intent = new Intent(Intent.ACTION_DIAL);
                break;
            case R.id.btnApp:
                intent = new Intent(this, com.example.launcher.ActivitySoftware.class);
                break;
            case R.id.btnSms:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("sms:"));
                break;
        }
        startActivity(intent);
    }
}