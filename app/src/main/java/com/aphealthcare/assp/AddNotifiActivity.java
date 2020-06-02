package com.aphealthcare.assp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNotifiActivity<string> extends AppCompatActivity {

    private EditText title;
    private EditText content;
    private TextView notiAuthor;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private static final String TAG = "MainActivity";
    private String name;
    private String notiCategory;
    private Spinner category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notifi);

        category = findViewById(R.id.id_noti_category);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String select = parent.getItemAtPosition(position).toString();
                if(select.equals("카테고리를 선택해주십시요.")){
                    notiCategory = "default";
                }
                else if(select.equals("회식공지")){
                    notiCategory = "전체 회식공지";
                }
                else if(select.equals("PA.C 제조 수정관련")){
                    notiCategory = "PA.C 제조 수정관련";
                }
                else if(select.equals("FCS 비급여관련")){
                    notiCategory = "FCS 비급여관련";
                }
                else if(select.equals("IV VALVE FILTER 급여관련")){
                    notiCategory = "IV VALVE FILTER 급여관련";
                }
                else{
                    notiCategory = "CSS 급여관련";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        

        notiAuthor = findViewById(R.id.id_noti_author);
        //To connect firebase database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //To get data from firebase (users collection)
        db.collection("users")
                //verify between the current user and the author by using whereEqulTo method
                .whereEqualTo("email",auth.getCurrentUser().getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot document : documents){
                    notiAuthor.setText(document.getString("name"));

                }

                //to debug the name of current user
                //Toast.makeText(AddNotifiActivity.this, auth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddNotifiActivity.this, "Cannot retrieve data", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void addOperation(View view){
        title = findViewById(R.id.id_noti_content);
        content = findViewById(R.id.id_noti_title);
        auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //set simple time
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //set notiuser object with author, publishdate, text, category, title
        Map<String, Object> notiuser = new HashMap<>();
        notiuser.put("author",notiAuthor.getText().toString());
        notiuser.put("publishDate", sdf.format(d));
        notiuser.put("text",content.getText().toString());
        notiuser.put("category",notiCategory);
        notiuser.put("title",title.getText().toString());

        //Store data into notification selection.
        db.collection("notifications")
                .document()
                .set(notiuser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddNotifiActivity.this, "Registeration succeeded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNotifiActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });


        Intent intent = new Intent(AddNotifiActivity.this, NotificationActivity.class);
        startActivity(intent);

        Toast.makeText(AddNotifiActivity.this, notiCategory, Toast.LENGTH_SHORT).show();

    }
}