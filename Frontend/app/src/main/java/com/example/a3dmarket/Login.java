package com.example.a3dmarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RecyclerView itemRecyclerView = findViewById(R.id.recyclerView);

        itemRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item(R.drawable.blue));
        itemList.add(new Item(R.drawable.red));
        itemList.add(new Item(R.drawable.bottle));
        itemList.add(new Item(R.drawable.docker));
        itemList.add(new Item(R.drawable.one));
        itemList.add(new Item(R.drawable.noimage));
        itemList.add(new Item(R.drawable.toyoi));

        itemRecyclerView.setAdapter(new ItemAdapter(itemList));
    }
}