package com.aphealthcare.assp;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_ISLOGGEDIN = "isLoggedIn";
    private static final String KEY_RANK = "rank";

    private EditText name;
    private EditText password;
    private EditText confirmPassword;
    private EditText email;
    private Button register;
    private String txtEmail;
    private String txtPassword;
    private String txtName;
    private String rank;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        password = findViewById(R.id.passwordInRegister);
        confirmPassword = findViewById(R.id.confirm_password);
        email = findViewById(R.id.email);
        register = findViewById(R.id.registerInRegister);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Spinner rankSpinner = findViewById(R.id.rank);
        ArrayAdapter rankList = ArrayAdapter.createFromResource(this, R.array.rank, android.R.layout.simple_spinner_item);
        rankList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rankSpinner.setAdapter(rankList);
        rankSpinner.setOnItemSelectedListener(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEmail = email.getText().toString();
                txtPassword = password.getText().toString();
                String txtConfirmPassword = confirmPassword.getText().toString();
                txtName = name.getText().toString();

                if(checkValidation(txtEmail, txtPassword, txtConfirmPassword, txtName)){
                    registerUser(txtEmail, txtPassword);
                    Intent intent = new Intent(RegisterActivity.this, AuthActivity.class);
                    finish();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser(String email, String password){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(Task<AuthResult> task){
                if(task.isSuccessful()){
//                    updateDatabase();
//                    Toast.makeText(RegisterActivity.this, "Registeration succeeded", Toast.LENGTH_SHORT).show();
                }else{
//                    Toast.makeText(RegisterActivity.this, "Registeration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        updateDatabase();
    }

    private void updateDatabase(){
        Map<String, Object> note = new HashMap<>();
        note.put(KEY_EMAIL, txtEmail);
        note.put(KEY_PASSWORD, txtPassword);
        note.put(KEY_NAME, txtName);
        note.put(KEY_RANK, rank);
        note.put(KEY_ISLOGGEDIN, false);
        db.collection("users").document(txtEmail).set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegisterActivity.this, "Registeration succeeded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean checkValidation(String email, String password, String confirmPassword, String name){
        if(emailValidation(email))
            return false;
        if(rank.equals("default")){
            return false;
        }
        if(!password.equals(confirmPassword))
            return false;

        return true;
    }

    private boolean emailValidation(String email){
        char character;
        String address = "";

        for(int i = 0; i < email.length(); i++){
            character = email.charAt(i);
            if(character == '@'){
                address = email.substring(i+1);
                break;
            }
        }
        if(address.equals("") || !address.equals("naver.com") || !address.equals("gmail.com")){
            return false;
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String select = parent.getItemAtPosition(position).toString();
        if(select.equals("직책을 선택하십시오")){
            rank = "default";
        }
        else if(select.equals("사장")){
            rank = "director";
        }
        else if(select.equals("매니저")){
            rank = "manager";
        }
        else{
            rank = "employee";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
