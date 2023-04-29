package com.test.beautyhealthservice.ChatModule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.test.beautyhealthservice.ChatModule.Adapter.viewholder_messages;
import com.test.beautyhealthservice.ChatModule.Notifications.APIService;
import com.test.beautyhealthservice.ChatModule.Notifications.Client;
import com.test.beautyhealthservice.ChatModule.Notifications.Data;
import com.test.beautyhealthservice.ChatModule.Notifications.MyResponse;
import com.test.beautyhealthservice.ChatModule.Notifications.Sender;
import com.test.beautyhealthservice.Helper;
import com.test.beautyhealthservice.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    ImageView profile_image;
    TextView username;
    DatabaseReference global_reference;
    ImageButton btn_send;
    EditText text_send;
    viewholder_messages adapter_messages;
    List<Chat> list_chat;
    RecyclerView recyclerView;
    Intent intent;
    ValueEventListener seenListener;
    String receiver_id,receiver_token;
    APIService apiService;
    boolean notify = false;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        intent = getIntent();
        receiver_id = intent.getStringExtra("receiver_id");
        receiver_token = intent.getStringExtra("token");

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String msg = text_send.getText().toString();
                if (!msg.equals("")){
                    sendMessage(Helper.GetData(MessageActivity.this,"user_id"), receiver_id,receiver_token, msg,"text");
                } else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });


        global_reference = FirebaseDatabase.getInstance().getReference("Users").child(receiver_id);

        readMesagges(Helper.GetData(MessageActivity.this,"user_id"), receiver_id);

        seenMessage(receiver_id);
    }

    private void seenMessage(final String receiver_id){
        global_reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = global_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(Helper.GetData(MessageActivity.this,"user_id")) && chat.getSender().equals(receiver_id)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, final String receiver,String receiver_token, String message,String message_type){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("type", message_type);
        hashMap.put("isseen", false);

        reference.child("Chats").push().setValue(hashMap);


        // add user to chat fragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist").child(Helper.GetData(MessageActivity.this,"user_id")).child(receiver_id);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(receiver_id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        
        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(receiver_id)
                .child(Helper.GetData(MessageActivity.this,"user_id"));
        chatRefReceiver.child("id").setValue(Helper.GetData(MessageActivity.this,"user_id"));

        final String msg = message;

        sendNotifiaction(receiver_token, Helper.GetData(MessageActivity.this,"first_name"), msg);


    }

    private void sendNotifiaction(String receiver_token, final String username, final String message){

                    Data data = new Data(Helper.GetData(MessageActivity.this,"user_id"), R.mipmap.ic_launcher, username+": "+message, "New Message",
                            receiver_id,"message");

                    Sender sender = new Sender(data, receiver_token);

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(MessageActivity.this, "Success!", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Toast.makeText(MessageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

    }

    private void readMesagges(final String myid, final String receiver_id){
        list_chat = new ArrayList<>();

        global_reference = FirebaseDatabase.getInstance().getReference("Chats");
        global_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_chat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(receiver_id) ||
                            chat.getReceiver().equals(receiver_id) && chat.getSender().equals(myid)){
                        list_chat.add(chat);
                    }

                    adapter_messages = new viewholder_messages(MessageActivity.this, list_chat);
                    recyclerView.setAdapter(adapter_messages);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        global_reference.removeEventListener(seenListener);

    }
}
