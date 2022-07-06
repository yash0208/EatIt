package com.rajaryan.eatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

public class Prefrence extends AppCompatActivity {
    Spinner spinner;
    private CountryCodePicker ccp;
    RadioGroup radioGroup,radioGroup2;
    RadioButton radioButton;
    EditText age1,suger1,min_cal,max_cal;
    TableLayout male,female;
    Button submit;
    TextView name,mail;
    String age,gender,country,max_calories,min_calories,diabities,suger;
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
        ccp=findViewById(R.id.ccp);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                country= ccp.getSelectedCountryName().toString();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                age=spinner.getSelectedItem().toString();
                Toast.makeText(getApplicationContext(), age ,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        min_cal=findViewById(R.id.mc);
        min_calories=min_cal.getText().toString();
        max_cal=findViewById(R.id.maxc);
        max_calories=max_cal.getText().toString();
        suger1=findViewById(R.id.suger);
        suger=suger1.getText().toString();
        name=findViewById(R.id.name);
        mail=findViewById(R.id.email);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account !=  null){
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();
            Toast.makeText(getApplicationContext(),personName + personEmail ,Toast.LENGTH_SHORT).show();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    name.setText(personName);
                    mail.setText(personEmail);
                }
            });
        }
        radioGroup2 = findViewById(R.id.radioGroup2);
        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(age)){
                    Toast.makeText(getApplicationContext(),"Select Valid Age",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(gender)){
                    Toast.makeText(getApplicationContext(),"Select Valid Gender",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(max_cal.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Enter Valid Maximum Required Calories",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(country)){
                    Toast.makeText(getApplicationContext(),"Select Valid Country",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(min_cal.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Enter Valid Minimum Required Calories",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(diabities)){
                    Toast.makeText(getApplicationContext(),"Select Diabities Status",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(suger1.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Enter Valid Amount Of Suger Requirement",Toast.LENGTH_SHORT).show();
                }
                else {
                    HashMap<String,String> hashMap=new HashMap<>();
                    hashMap.put("age",age);
                    hashMap.put("gender",gender);
                    hashMap.put("max_calories",max_cal.getText().toString());
                    hashMap.put("country",country);
                    hashMap.put("min_calories",min_cal.getText().toString());
                    hashMap.put("diabities",diabities);
                    hashMap.put("suger",suger1.getText().toString());
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Data");
                    databaseReference.setValue(hashMap);
                    Intent i=new Intent(Prefrence.this,HomeActivity.class);
                    startActivity(i);
                }
            }
        });
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Data");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    data data=snapshot.getValue(com.rajaryan.eatit.data.class);
                    suger1.setText(data.getSuger());
                    min_cal.setText(data.getMin_calories());
                    max_cal.setText(data.getMax_calories());
                    spinner.setSelection(Integer.parseInt(data.getAge().toString()));
                    if(data.getGender().equals("Male")){
                        radioButton = findViewById(R.id.radio_one);
                        radioButton.setChecked(true);
                        RadioButton radioButton1 = findViewById(R.id.radio_two);
                        radioButton1.setChecked(false);
                    }
                    else{
                        radioButton = findViewById(R.id.radio_one);
                        radioButton.setChecked(false);
                        RadioButton radioButton1 = findViewById(R.id.radio_two);
                        radioButton1.setChecked(true);
                    }
                    if(data.getDiabities().equals("Yes")){
                        radioButton = findViewById(R.id.radio_one1);
                        radioButton.setChecked(true);
                        RadioButton radioButton1 = findViewById(R.id.radio_two1);
                        radioButton1.setChecked(false);
                    }
                    else{
                        radioButton = findViewById(R.id.radio_one1);
                        radioButton.setChecked(false);
                        RadioButton radioButton1 = findViewById(R.id.radio_two1);
                        radioButton1.setChecked(true);
                    }
                    Toast.makeText(getApplicationContext(),data.getCountry().toString(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    public void checkButton2(View view) {
        int radioId = radioGroup2.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        diabities=radioButton.getText().toString();

    }
}