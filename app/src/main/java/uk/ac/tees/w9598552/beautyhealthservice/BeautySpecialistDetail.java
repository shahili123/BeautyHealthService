package uk.ac.tees.w9598552.beautyhealthservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import java.util.Locale;

import uk.ac.tees.w9598552.beautyhealthservice.ChatModule.MessageActivity;

public class BeautySpecialistDetail extends AppCompatActivity {

    TextView Name,Email,Address;
    ImageView Image,btn_map;
    String receiver_id,latitude,longitude,image,token;
    Button btn_messsage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_specialist_detail);
        Name  = (TextView) findViewById(R.id.txt_name);
        Email  = (TextView) findViewById(R.id.txt_email);
        Address  = (TextView) findViewById(R.id.txt_address);
        Image  = (ImageView) findViewById(R.id.image);
        btn_messsage=findViewById(R.id.btn_message);
        btn_messsage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BeautySpecialistDetail.this, MessageActivity.class);
                intent.putExtra("receiver_id", receiver_id);
                intent.putExtra("name", Name.getText().toString());
                intent.putExtra("image", image);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });
        btn_map=findViewById(R.id.btn_map);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)",Double.parseDouble(latitude),Double.parseDouble(longitude), Name.getText().toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });
        Bundle extras = getIntent().getExtras();
        if(extras != null){

            receiver_id = extras.getString("receiver_id");
            Name.setText( extras.getString("name"));
            Email.setText( extras.getString("email"));
            Address.setText( extras.getString("address"));
            latitude = extras.getString("latitude");
            longitude = extras.getString("longitude");
            image = extras.getString("image");
            token = extras.getString("token");

            Picasso.get().load(image).into(Image);





        }
    }
}