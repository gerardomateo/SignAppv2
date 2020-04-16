package com.exp.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class HomeAdActivity extends AppCompatActivity {

    public static HomeAdActivity ins;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ins = this;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.exit) {
            SPUtil.put(this, "username", "");
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }
        if (item.getItemId() == R.id.ps) {
            startActivity(new Intent(this, FksActivity.class));
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void yhgl(View view) {
        startActivity(new Intent(this, UsersActivity.class));
    }

    public void zxgl(View view) {
        startActivity(new Intent(this, CarsActivity.class));
    }

}
