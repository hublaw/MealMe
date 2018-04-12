package com.example.doireann.mealme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {
    TextView hSearch, hSuggest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        hSearch = findViewById(R.id.id_help_search);
        hSuggest = findViewById(R.id.id_help_suggest);
    }
}
