package com.test.beautyhealthservice;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class viewholder_beauty_specialist extends RecyclerView.Adapter<viewholder_beauty_specialist.MyViewholder>{

    Context context;
    View _view;
    ArrayList <BSpecialistModel> beauty_specialist_list;
    public viewholder_beauty_specialist(Context c , ArrayList<BSpecialistModel> list)
    {
        context = c;
        beauty_specialist_list = list;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        _view= LayoutInflater.from(context).inflate(R.layout.list_layout_beauty_specialist,parent, false);
        return new MyViewholder(_view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewholder holder, @SuppressLint("RecyclerView") final int position) {
        holder.txt_bspecialist_name.setText(beauty_specialist_list.get(position).getName());
        Picasso.get().load(beauty_specialist_list.get(position).getImage()).into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, BeautySpecialistDetail.class);
                intent.putExtra("receiver_id", beauty_specialist_list.get(position).getId());
                intent.putExtra("name", beauty_specialist_list.get(position).getName());
                intent.putExtra("email", beauty_specialist_list.get(position).getEmail());
                intent.putExtra("address", beauty_specialist_list.get(position).getAddress());
                intent.putExtra("latitude", beauty_specialist_list.get(position).getLatitude());
                intent.putExtra("longitude", beauty_specialist_list.get(position).getLongitude());
                intent.putExtra("image", beauty_specialist_list.get(position).getImage());
                intent.putExtra("token", beauty_specialist_list.get(position).getToken());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getItemCount() {
        return beauty_specialist_list.size();
    }

    class MyViewholder extends RecyclerView.ViewHolder{
        TextView txt_bspecialist_name;
        ImageView image;
        CardView cardView;

        public MyViewholder(View itemView){
            super(itemView);

            txt_bspecialist_name  = (TextView) itemView.findViewById(R.id.bspecialist_name);
            image  = (ImageView) itemView.findViewById(R.id.image);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }

    public void filterList(ArrayList<BSpecialistModel> filteredList) {
        beauty_specialist_list = filteredList;
        notifyDataSetChanged();
    }



}

