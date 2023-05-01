package com.test.beautyhealthservice.ChatModule.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.test.beautyhealthservice.BSpecialistModel;
import com.test.beautyhealthservice.ChatModule.Chat;
import com.test.beautyhealthservice.ChatModule.MessageActivity;
import com.test.beautyhealthservice.Helper;
import com.test.beautyhealthservice.R;
import com.test.beautyhealthservice.Users;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class viewholder_bspecialist_chats extends RecyclerView.Adapter<viewholder_bspecialist_chats.ViewHolder> {

    private Context _context;
    private List<BSpecialistModel> list;
    private boolean ischat;
    String theLastMessage;

    public viewholder_bspecialist_chats(Context context, List<BSpecialistModel> _list, boolean ischat){
        this.list = _list;
        this._context = context;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_context).inflate(R.layout.list_layout_chats, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.username.setText(list.get(position).getName());
        Picasso.get().load(list.get(position).getImage()).into(holder.profile_image);

        if (ischat){
            lastMessage(list.get(position).getId(), holder.last_msg,holder.txt_unread,holder.cardView);
        } else {
            holder.last_msg.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(_context, MessageActivity.class);
                intent.putExtra("receiver_id", list.get(position).getId());
                intent.putExtra("token", list.get(position).getToken());
                intent.putExtra("name", list.get(position).getName());
                intent.putExtra("image", list.get(position).getImage());
                _context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public CircleImageView profile_image;
        private TextView last_msg;
        private TextView txt_unread;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            txt_unread = itemView.findViewById(R.id.unread);
            profile_image = itemView.findViewById(R.id.profile_image);
            last_msg = itemView.findViewById(R.id.last_msg);
            cardView=itemView.findViewById(R.id.card_view_unread);
        }
    }
    private void lastMessage(final String userid, final TextView last_msg, final TextView txt_unread,final CardView cardView){
        theLastMessage = "default";

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int unread_messages=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Chat chat = snapshot.getValue(Chat.class);
                    if ( chat != null) {
                        if (chat.getReceiver().equals(Helper.GetData(_context,"user_id")) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(Helper.GetData(_context,"user_id"))) {
                            theLastMessage = chat.getMessage();
                            if(!chat.isIsseen()){
                                if(chat.getReceiver().equals(Helper.GetData(_context,"user_id"))){
                                    unread_messages++;
                                }

                            }
                            if(unread_messages>0){
                                txt_unread.setText(String.valueOf(unread_messages));
                                cardView.setVisibility(View.VISIBLE);
                            }
                            else{
                                cardView.setVisibility(View.GONE);
                            }

                        }
                    }
                }

                switch (theLastMessage){
                    case  "default":
                        last_msg.setText("No Message");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
