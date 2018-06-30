package com.acme.a3csci3130;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class DetailViewActivity extends Activity implements AdapterView.OnItemSelectedListener{

    private EditText nameField, businessNumberField, addressField;
    private Spinner primaryBusinessField, provinceField;
    Contact receivedPersonInfo;
    private MyApplicationData appState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        receivedPersonInfo = (Contact)getIntent().getSerializableExtra("Contact");
        appState = (MyApplicationData) getApplicationContext();

        nameField = (EditText) findViewById(R.id.det_name);
        businessNumberField = (EditText) findViewById(R.id.det_businessNumber);
        primaryBusinessField = (Spinner) findViewById(R.id.det_primaryBusiness_spinner);
        addressField =(EditText) findViewById(R.id.det_address);
        provinceField = (Spinner) findViewById(R.id.det_province_spinner);

        ArrayAdapter<CharSequence> businessAdapter = ArrayAdapter.createFromResource(this,
                R.array.primaryBusiness_list, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> provinceAdapter = ArrayAdapter.createFromResource(this,
                R.array.province_list, android.R.layout.simple_spinner_dropdown_item);
        primaryBusinessField.setAdapter(businessAdapter);
        provinceField.setAdapter(provinceAdapter);
        primaryBusinessField.setOnItemSelectedListener(this);
        provinceField.setOnItemSelectedListener(this);
        int businessSpinnerPosition;
        int provinceSpinnerPosition;

        if(receivedPersonInfo != null){
            nameField.setText(receivedPersonInfo.name);
            businessNumberField.setText(receivedPersonInfo.number);

            businessSpinnerPosition = businessAdapter.getPosition(receivedPersonInfo.primaryBusiness);
            primaryBusinessField.setSelection(businessSpinnerPosition);
            if(receivedPersonInfo.address != null) {
                addressField.setText(receivedPersonInfo.address);
            }
            if(receivedPersonInfo.province != null){
                provinceSpinnerPosition = provinceAdapter.getPosition(receivedPersonInfo.province);
                provinceField.setSelection(provinceSpinnerPosition);
            }

        }
    }

    public void updateContact(View v){
        String uid = receivedPersonInfo.uid;
        String name = nameField.getText().toString();
        String number = businessNumberField.getText().toString();
        String primaryBusiness =String.valueOf(primaryBusinessField.getSelectedItem());
        String address = addressField.getText().toString();
        Contact person = new Contact(uid, name, number, primaryBusiness);
        appState.firebaseReference.child(uid).updateChildren(person.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Update Successful");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error = e.getMessage() + ": " + e.getCause();
                        Log.d(TAG, "Update Failed: " + error);
                    }
                });
        appState.firebaseReference.child(uid).updateChildren(person.toMap());
        finish();

    }

    public void eraseContact(View v)
    {
        String uid = receivedPersonInfo.uid;
        appState.firebaseReference.child(uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Write was successful!
                Log.d(TAG, "Remove Successful");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                        String error = e.getMessage() + ": " + e.getCause();
                        Log.d(TAG, "Remove Failed: " + error);
                    }
                });;
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
