package com.example.a3dmarket;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ItemDetail extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemdetail);

        TextView name = findViewById(R.id.name);
        TextView price = findViewById(R.id.name);
        ImageView img = findViewById(R.id.img);

        Bundle extras = getIntent().getExtras();


        //img.setim(extras.getString("name"));
        name.setText(extras.getString("name"));
        price.setText(extras.getString("price"));
    }
}
