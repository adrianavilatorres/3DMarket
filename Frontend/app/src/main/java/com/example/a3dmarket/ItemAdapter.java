package com.example.a3dmarket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

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
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    //clas---------------

    class ItemAdapterHolder extends RecyclerView.ViewHolder{

        RoundedImageView itemImgView;


        ItemAdapterHolder(@NonNull View itemView) {
            super(itemView);
            itemImgView = itemView.findViewById(R.id.itemImg);
        }

        void setItemImg(Item item){
            itemImgView.setImageResource(item.getImg());
        }



    }


}
