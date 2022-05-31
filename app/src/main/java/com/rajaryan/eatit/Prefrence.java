package com.rajaryan.eatit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

public class Prefrence extends AppCompatActivity {
    Spinner spinner;
    private CountryCodePicker ccp;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TableLayout male,female;
    String gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefrence);
        radioGroup = findViewById(R.id.radioGroup);
        spinner=findViewById(R.id.spinner1);
        male=findViewById(R.id.male_table);
        female=findViewById(R.id.female_table);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.age, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


    }

    public void checkButton(View view) {

            int radioId = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(radioId);
            gender=radioButton.getText().toString();
            if(gender.equals("Male")){
                male.setVisibility(View.VISIBLE);
                female.setVisibility(View.GONE);
            }
            if(gender.equals("Female")){
                male.setVisibility(View.GONE);
                female.setVisibility(View.VISIBLE);
            }
    }
}