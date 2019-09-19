package com.example.custom2.settings;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.custom2.R;
import com.example.custom2.Repository;
import com.example.custom2.room.SetParams;

public class PassActivity extends AppCompatActivity {
    EditText old;
    EditText ne;
    EditText confirm;
    TextView button;
    String oldpas="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);
        old = findViewById(R.id.editText);
        ne = findViewById(R.id.editText2);
        confirm = findViewById(R.id.editText3);
        button = findViewById(R.id.button2);
        oldpas= Repository.getInstance().getSettings().getSetParams().passwordS;
        if(oldpas.equals("")){
            findViewById(R.id.editText).setVisibility(View.GONE);
        }
        PassActivity activity = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
                if (oldpas.equals("")) {
                    String s1=ne.getText().toString();
                    String s2=confirm.getText().toString();
                    if (ne.getText().toString().equals(confirm.getText().toString())) {
                        SetParams setParams=Repository.getInstance().getSettings().getSetParams();
                        setParams.passwordS=ne.getText().toString();
                        Thread thread=new Thread(){
                            @Override
                            public void run() {
                                Repository.getInstance().getDatabase().getSettingsDao().updateSettings(setParams);                                }
                        };
                        thread.start();
                        finish();
                    } else {
                        Toast.makeText(activity, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (oldpas.equals(old.getText().toString())) {
                        if (ne.getText().toString().equals(confirm.getText().toString())) {
                            SetParams setParams=Repository.getInstance().getSettings().getSetParams();
                            setParams.passwordS=ne.getText().toString();
                            Thread thread=new Thread(){
                                @Override
                                public void run() {
                                    Repository.getInstance().getDatabase().getSettingsDao().updateSettings(setParams);                                }
                            };
                            thread.start();
                            finish();
                        } else {
                            Toast.makeText(activity, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, "Старый пароль не верен", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
