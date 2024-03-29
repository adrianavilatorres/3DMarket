package com.example.a3dmarket.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a3dmarket.Item;
import com.example.a3dmarket.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Preview_Items_ImgAdapter extends RecyclerView.Adapter<Preview_Items_ImgAdapter.ViewHolder> {

    private ArrayList<Uri> previewImgList = new ArrayList<>();
    private LayoutInflater mInflater;
    Context context;

    RoundedImageView roundedImageView;
    ImageView imageView;



    public Preview_Items_ImgAdapter(Context context, ArrayList previewImgList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.previewImgList = previewImgList;

    }


    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.preview_items_img, parent, false);

        roundedImageView = view.findViewById(R.id.itemImg);
        imageView = view.findViewById(R.id.deleteBtn);
        roundedImageView.setZ(0);
        imageView.setZ(2);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setItemImg(previewImgList.get(position));



        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: real" + position  );

                AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                builder.setMessage("¿Desea eliminar esta imagen?")
                        .setCancelable(false)
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        })
                        .setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                previewImgList.remove(position);
                                notifyDataSetChanged();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();


               //Picasso.get().load(previewImgList.get(position)).into(imagen);
            }
        });


    }

    @Override
    public int getItemCount() {
        return previewImgList.size();
    }



     class ViewHolder extends RecyclerView.ViewHolder{

         RoundedImageView itemImgView;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            itemImgView = itemView.findViewById(R.id.itemImg);
        }

         void setItemImg(Uri item){


             Picasso.get().load(item).into(itemImgView);

         }


    }

}
