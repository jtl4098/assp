package com.aphealthcare.assp;

import android.os.Bundle;

import com.aphealthcare.assp.adapters.NotifAdapter;
import com.aphealthcare.assp.helpers.Notification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private static final String COLLECTION = "notifications";
    private static final String AUTHORFIELD = "author";
    private static final String PUBLISHDATEFIELD = "publishDate";
    private static final String TEXTFIELD = "text";
    private static final String TITLEFIELD = "title";
    private static final String CATEGORYFIELD = "category";
    private static String id;
    private String tag = "String tag";

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView tAuthor, tText, tTitle, tCategory, tPublishDate;

    private List<Notification> notifications = new ArrayList<>();   //Notifications list
    private RecyclerView recyclerView;        //Recyclerview
    private NotifAdapter notifAdapter;      //Notifications handler

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification2);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.notif_recyclerview);
        notifAdapter = new NotifAdapter(notifications);
        RecyclerView.LayoutManager notifLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(notifLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(notifAdapter);

        db.collection(COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot querySnapshot: task.getResult()){
                    if(!querySnapshot.contains("author"))
                        continue;
                    String author = querySnapshot.getString(AUTHORFIELD);
                    String title = querySnapshot.getString(TITLEFIELD);
                    String text = querySnapshot.getString(TEXTFIELD);
                    String publishDate = querySnapshot.getString(PUBLISHDATEFIELD);
                    String category = querySnapshot.getString(CATEGORYFIELD);

                    Notification notification = new Notification(author, title, text, publishDate, category);
                    notifications.add(notification);
                }

//              refresh notification list to show added notifications to current view
                notifAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NotificationActivity.this, "Cannot retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.notif_menu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return true;
    }

//    public void createNewDialog(){
//        dialogBuilder = new AlertDialog.Builder(this);
//        final View notificationPopupView = getLayoutInflater().inflate(R.layout.notif_popup, null);
//    }
}

//https://www.androidhive.info/2016/01/android-working-with-recycler-view/