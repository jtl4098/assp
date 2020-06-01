package com.aphealthcare.assp;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.aphealthcare.assp.fragments.AccountFragment;
import com.aphealthcare.assp.fragments.DashboardFragment;
import com.aphealthcare.assp.fragments.NotifFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WorkActivity extends AppCompatActivity {
    private TextView greeting;

    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        bottomNavigationView = findViewById(R.id.bottomnav);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new DashboardFragment()).commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()){
                    case R.id.home:
                        fragment = new DashboardFragment();
                        break;
                    case R.id.account:
                        fragment = new AccountFragment();
                        break;
                    case R.id.notification:
                        fragment = new NotifFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                return true;
            }
        });

    }

    @Override
    protected void onPause(){
        super.onPause();

    }

    protected void onResume(){
        super.onResume();

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        moveTaskToBack(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.menu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.exit){
            setLoggedIn(auth.getCurrentUser().getEmail());
            auth.signOut();
            startActivity(new Intent(WorkActivity.this, AuthActivity.class));
            finish();
        }
        else if(id == R.id.search){
            Toast.makeText(getApplicationContext(), "you click setting", Toast.LENGTH_SHORT).show();
        }
        else if(id == R.id.setting){
            Toast.makeText(getApplicationContext(), "you click setting", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    private void setLoggedIn(String email){
        Map<String, Object> setLoginStatus;
        setLoginStatus = new HashMap<>();
        setLoginStatus.put("isLoggedIn", true);

        db.collection("users").document(email)
                .update("isLoggedIn", false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(WorkActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
