package com.aphealthcare.assp;

import android.text.TextUtils;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button register;
    private Button login;


    private FirebaseAuth auth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(AuthActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String txtUsername = email.getText().toString();
                String txtPassword = password.getText().toString();

                if(checkValidation(txtUsername, txtPassword)){
                    loginUser(txtUsername, txtPassword);
                }
            }
        });
    }

    private void registerUser(String email, String password){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(Task<AuthResult> task){
                if(task.isSuccessful()){
                    Toast.makeText(AuthActivity.this, "Registeration succeeded", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AuthActivity.this, "Registeration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser(String email, String password){
        final String mEmail = email;
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AuthActivity.this, "Hi" + auth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                    setLoggedIn(mEmail);
                    startActivity(new Intent(AuthActivity.this, WorkActivity.class));
                    finish();
                }else{
                    Toast.makeText(AuthActivity.this, "Failed to sign in", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String str = e.getMessage();
                AlertDialog.Builder builder = new AlertDialog.Builder(AuthActivity.this);
                builder.setTitle("Error").setMessage(str);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void setLoggedIn(String email){
        Map<String, Object> setLoginStatus;
        setLoginStatus = new HashMap<>();
        setLoginStatus.put("isLoggedIn", true);

        db.collection("users").document(email)
                .update("isLoggedIn", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AuthActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private boolean checkValidation(String email, String password){
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(AuthActivity.this, "Empty credential details", Toast.LENGTH_SHORT).show();
            return false;
        } else if(password.length() < 6){
            Toast.makeText(AuthActivity.this, "Too short password", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }
}
