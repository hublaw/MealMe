package com.example.doireann.mealme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {
    private Button search_btn, suggest_btn, help_btn;
    RadioGroup radio_grp;
    RadioButton radio_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        SharedPreferences prefs = getSharedPreferences("app_state", MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();

        search_btn = findViewById(R.id.id_search_menu_btn);
        suggest_btn = findViewById(R.id.id_suggest_menu_btn);
        help_btn = findViewById(R.id.id_help_menu_btn);
        radio_grp = findViewById(R.id.id_suggest_radio_grp);


        radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                radio_btn = findViewById(checkedId);
                editor.putString("rdm_tag", radio_btn.getText().toString().toLowerCase()).commit();
            }
        });

        suggest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, SuggestActivity.class));
            }
        });



//        search_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MenuActivity.this, SearchActivity.class));
//            }
//        });
//
//        help_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity((new Intent(MenuActivity.this, HelpActivity.class)));
//            }
//        });
    }
}
