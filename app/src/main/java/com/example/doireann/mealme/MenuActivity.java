package com.example.doireann.mealme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MenuActivity extends AppCompatActivity {
    private Button search_btn, suggest_btn, help_btn;
    private EditText search_txt;
    RadioGroup radio_grp;
    RadioButton radio_btn;
    private Intent iSuggest;
    private Intent iSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        final Bundle bSuggest = new Bundle();
        final Bundle bSearch = new Bundle();

        search_btn = findViewById(R.id.id_search_menu_btn);
        suggest_btn = findViewById(R.id.id_suggest_menu_btn);
        help_btn = findViewById(R.id.id_help_menu_btn);
        radio_grp = findViewById(R.id.id_suggest_radio_grp);
        search_txt = findViewById(R.id.id_search_ingred_txt);

        radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                radio_btn = findViewById(checkedId);
                bSuggest.putString("tag", radio_btn.getText().toString().toLowerCase());
            }
        });

        suggest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iSuggest = new Intent(MenuActivity.this, FetchActivity.class);
                bSuggest.putBoolean("search", false);
                iSuggest.putExtras(bSuggest);
                startActivity(iSuggest);
            }
        });

        // handle spaces in input
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bSearch.putString("tag", search_txt.getText().toString());
                bSearch.putBoolean("search", true);
                iSearch = new Intent(MenuActivity.this, FetchActivity.class);
                iSearch.putExtras(bSearch);
                startActivity(iSearch);
            }
        });
//
//        help_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity((new Intent(MenuActivity.this, HelpActivity.class)));
//            }
//        });
    }
}
