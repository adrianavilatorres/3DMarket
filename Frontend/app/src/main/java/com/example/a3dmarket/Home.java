package com.example.a3dmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.a3dmarket.Adapters.ItemAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Home extends AppCompatActivity {


    FirebaseFirestore db    = FirebaseFirestore.getInstance();
    List<Item> itemList     = new ArrayList<>();
    RecyclerView itemRecyclerView;
    SwipeRefreshLayout refreshLayout;
    ItemAdapter adapter = new ItemAdapter(itemList);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        itemRecyclerView= findViewById(R.id.recyclerView);
        refreshLayout = findViewById(R.id.refreshLayout);
        itemRecyclerView.setAdapter(adapter);

        //Pinterest efect grid
        itemRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        // Create a firestore reference from our app
        CollectionReference item = db.collection("items");

        // Create a storage reference from our app
        //FirebaseStorage storage = FirebaseStorage.getInstance();
        //StorageReference storageRef = storage.getReference();



        printAllDocumentFromFirebase("items",itemRecyclerView);




        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                itemList.clear();
                printAllDocumentFromFirebase("items",itemRecyclerView);
                refreshLayout.setRefreshing(false);
                //adapter.notifyDataSetChanged();
                Log.d("TAG", "Refrash DONE");

            }
        });







        // ---------------------------OBTENER SOLO UNA COLLECCION--------------------------

        /*DocumentReference docRef = db.collection("items").document("eFhO21NZDQwcYWlNWwD5");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//                        Log.d("TAG", "DocumentSnapshot data items---: " + document.getData().get("url"));
                        itemList.add(new Item(document.getData().get("url").toString()));

                        *//*storageRef.child("imagen/bottle.gif").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("TAG", "onSuccess: URI " + uri);
                                itemList.add(new Item("http://i.imgur.com/DvpvklR.png"));
                            }
                        });*//*
                    } else {
                        Log.d("TAG", "No such document");
                    }
                    itemRecyclerView.setAdapter(new ItemAdapter(itemList));

                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });*/




    }


    //With this code i can get all data from a collection "items" in firebase
    private void printAllDocumentFromFirebase(String nameCollection, RecyclerView recyclerView) {

        db.collection(nameCollection).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String url = (String) document.getData().get("url");
                        String price = (String) document.getData().get("price");
                        String name = (String) document.getData().get("name");
                        //Log.d("TAG", document.getId() + " => " + url);
                        itemList.add(new Item(url,name,price));

                    }


                    //get first the new data with a reverse from list of firebase
                    //Collections.reverse(itemList);
                    adapter.notifyDataSetChanged();
                    Log.d("TAG", "Error dentro metodo");

                } else {

                    Log.d("TAG", "Error getting documents: ", task.getException());
                }

            }

        });

    }

}

