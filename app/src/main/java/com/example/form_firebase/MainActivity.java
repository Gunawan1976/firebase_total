package com.example.form_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.form_firebase.adapter.UserAdapter;
import com.example.form_firebase.model.FunctionHelper;
import com.example.form_firebase.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab_add;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<User> list = new ArrayList<>();
    private UserAdapter userAdapter;
    ProgressDialog progressDialog;
    private TextView total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_view);
        fab_add = findViewById(R.id.floatingActionButton);
        total = findViewById(R.id.tvTotal);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Sedang Mengambil Data");

        userAdapter = new UserAdapter(getApplicationContext(),list);
        //dialgo delete data
        userAdapter.setDialog(new UserAdapter.Dialog() {
            @Override
            public void onClick(int post) {
                final CharSequence[] dialogitem = {"Edit","Hapus"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //agar bisa click muncul edit dan hapus
                        if (i == 0){
                            Intent intent = new Intent(getApplicationContext(),EditorActivity.class);
                            intent.putExtra("id",list.get(post).getId());
                            intent.putExtra("name",list.get(post).getName());
                            intent.putExtra("email",list.get(post).getEmail());
                            startActivity(intent);
                        }else{
                            deleteData(list.get(post).getId());
                        }
                    }
                });
                dialog.show();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(userAdapter);

        //tambah data
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),EditorActivity.class));
            }
        });

        //readData();

    }

    @Override
    protected void onStart() {
        super.onStart();
        readData();

    }

    public void readData(){
        progressDialog.show();
        //baca data
        db.collection("mencoba")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        int sumdata = 0;

                        list.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = new User(document.getString("name"),document.getString("email"),document.getString("uang"));
                                user.setId(document.getId());
                                list.add(user);

                                Object price = document.getString("uang");
                                int value = Integer.parseInt(String.valueOf(price));
                                sumdata += value;
                                String initprice = FunctionHelper.rupiahFormat(sumdata);

                                total.setText(initprice);
                                //total.setText(document.getString("uang"));
                            }
                            userAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), "data gagal diambil", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    public void deleteData(String id){
        progressDialog.show();
        db.collection("mencoba").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "BERHASIL dihapus", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        readData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(e.getLocalizedMessage(),"erorr");
                    }
                });
    }

}