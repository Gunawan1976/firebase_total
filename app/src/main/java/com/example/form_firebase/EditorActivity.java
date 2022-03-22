package com.example.form_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditorActivity extends AppCompatActivity {
    private EditText etName, etEmail, etUang;
    private Button btnsave;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;
    private String id = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etUang =  findViewById(R.id.et_uang);
        btnsave = findViewById(R.id.btn_save);

        progressDialog = new ProgressDialog(EditorActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("menyimpan");

        Intent intent = getIntent();
        if (intent !=null){
            id = intent.getStringExtra("id");
            etName.setText(intent.getStringExtra("name"));
            etEmail.setText(intent.getStringExtra("email"));
            etUang.setText(intent.getStringExtra("uang"));
        }

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etName == null || etEmail == null || etUang == null){
                    Toast.makeText(getApplicationContext(), "silahkan isi semua data", Toast.LENGTH_SHORT).show();
                }else{
                    savedata(etName.getText().toString(), etEmail.getText().toString(), etUang.getText().toString());
                }
            }
        });

    }
    private void savedata(String name, String email, String uang){

        Map<String, Object> user = new HashMap<>();
        user.put("name",name);
        user.put("email",email);
        user.put("uang",uang);

        progressDialog.show();
        //edit data
        if (id != null){
            db.collection("mencoba")
                    .document(id)
                    .set(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "berhasil bossq", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Log.e("error","disini error");
                            }
                        }
                    });
        }else {
            progressDialog.show();
            //menambah data
            db.collection("mencoba")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(), "berhasil bossq", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
        }
    }
}