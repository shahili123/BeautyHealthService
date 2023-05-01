package uk.ac.tees.w9598552.beautyhealthservice;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class viewholder_recipes extends RecyclerView.Adapter<viewholder_recipes.MyViewholder>{

    Context context;
    View _view;
    ArrayList <Recipes> recipesArrayList;
    public viewholder_recipes(Context c , ArrayList<Recipes> list)
    {
        context = c;
        recipesArrayList = list;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        _view= LayoutInflater.from(context).inflate(R.layout.list_layout_recipes,parent, false);
        return new MyViewholder(_view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewholder holder, @SuppressLint("RecyclerView") final int position) {
        holder.txt_bspecialist_name.setText(recipesArrayList.get(position).getRecipe_name());
        Picasso.get().load(recipesArrayList.get(position).getRecipe_image()).into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(recipesArrayList.get(position).getRecipe_url())));
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
        return recipesArrayList.size();
    }

    class MyViewholder extends RecyclerView.ViewHolder{
        TextView txt_bspecialist_name;
        ImageView image;
        CardView cardView;

        public MyViewholder(View itemView){
            super(itemView);

            txt_bspecialist_name  = (TextView) itemView.findViewById(R.id.txt_name);
            image  = (ImageView) itemView.findViewById(R.id.image);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }




}


