package com.aphealthcare.assp;

import android.content.Intent;
import android.os.Bundle;

import com.aphealthcare.assp.adapters.NotifAdapter;
import com.aphealthcare.assp.adapters.Notification;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private List<Notification> notifications = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotifAdapter notifAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification2);

        auth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.notif_recyclerview);
        notifAdapter = new NotifAdapter(notifications);
        RecyclerView.LayoutManager notifLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(notifLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(notifAdapter);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.notif_menu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.exit){
//            setLoggedIn(auth.getCurrentUser().getEmail());
//            auth.signOut();
            startActivity(new Intent(NotificationActivity.this, AuthActivity.class));
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

    private void prepareNotifData(){
        long time = System.currentTimeMillis();
        Notification notification = new Notification("tk", "First notif", "Notifications", time);
        notifications.add(notification);

        notification = new Notification("dt", "Second notif", "Notifications", time);
        notifications.add(notification);
        notification = new Notification("tkdt", "Third notif", "Notifications", time);
        notifications.add(notification);
        notification = new Notification("dttk", "First notif", "Notifications", time);
        notifications.add(notification);
        notification = new Notification("tk1", "First notif", "Notifications", time);
        notifications.add(notification);
        notification = new Notification("tk2", "First notif", "Notifications", time);
        notifications.add(notification);

        notifAdapter.notifyDataSetChanged();
    }
}

//https://www.androidhive.info/2016/01/android-working-with-recycler-view/