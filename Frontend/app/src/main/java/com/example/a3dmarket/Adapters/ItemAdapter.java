package com.example.a3dmarket.Adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a3dmarket.Item;
import com.example.a3dmarket.ItemDetail;
import com.example.a3dmarket.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemAdapterHolder>{

    private List<Item> itemList;

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemAdapterHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container,
                        parent,
                        false)
        );
    }



    @Override
    public void onBindViewHolder(@NonNull ItemAdapterHolder holder, int position) {
        holder.setItemImg(itemList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(v.getContext(), ItemDetail.class);
                intent.putExtra("name" ,"name2");
                intent.putExtra("price" ,"price2");

                v.getContext().startActivity(intent);



                Log.d("TAG", "onClick: " + position);
            }
        });

    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }




    //---------------------------class---------------

    class ItemAdapterHolder extends RecyclerView.ViewHolder{

        RoundedImageView itemImgView;


        ItemAdapterHolder(@NonNull View itemView) {
            super(itemView);

            itemImgView = itemView.findViewById(R.id.itemImg);
        }

        void setItemImg(Item item){


            Picasso.get().load(item.getImg()).into(itemImgView);

        }

    }



}
