package com.example.doireann.mealme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {
    TextView hSuggest1, hSuggest2, hSuggest3, hSuggest4, hSuggest5;
    TextView hSearch1, hSearch2, hSearch3, hSearch4, hSearch5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        hSuggest1 = findViewById(R.id.id_help_suggest1);
        hSuggest2 = findViewById(R.id.id_help_suggest2);
        hSuggest3 = findViewById(R.id.id_help_suggest3);
        hSuggest4 = findViewById(R.id.id_help_suggest4);
        hSuggest5 = findViewById(R.id.id_help_suggest5);
        hSearch1 = findViewById(R.id.id_help_search1);
        hSearch2 = findViewById(R.id.id_help_search2);
        hSearch3 = findViewById(R.id.id_help_search3);
        hSearch4 = findViewById(R.id.id_help_search4);
        hSearch5 = findViewById(R.id.id_help_search5);
    }
}
