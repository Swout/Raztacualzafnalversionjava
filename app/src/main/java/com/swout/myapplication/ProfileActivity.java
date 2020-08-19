package com.swout.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private String receiverUserId;
    private CircleImageView userprofileImage;
    private TextView userprofileName,userProfileStatus;
    private Button SendMessageRequestButton;
    private DatabaseReference Userref;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Userref = FirebaseDatabase.getInstance().getReference().child("Users");

        receiverUserId = getIntent().getExtras().get("visit_user_id").toString();

        Toast.makeText(this, "User Id:"+receiverUserId, Toast.LENGTH_SHORT).show();

        userprofileImage = (CircleImageView) findViewById(R.id.visit_profile_image);
        userprofileName = (TextView) findViewById(R.id.visit_user_name);
        userProfileStatus = (TextView) findViewById(R.id.visit_user_status);
        SendMessageRequestButton = (Button) findViewById(R.id.send_message_request_button);

        RetrieveUserInfo();

    }

    private void RetrieveUserInfo()
    {

        Userref.child(receiverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if((snapshot.exists()) && (snapshot.hasChild("image")))
                {
                    String userImage = snapshot.child("image").getValue().toString();
                    String userName= snapshot.child("name").getValue().toString();
                    String userStatus = snapshot.child("status").getValue().toString();

                    Picasso.get().load(userImage).placeholder(R.drawable.profile_image).into(userprofileImage);
                    userprofileName.setText(userName);
                    userProfileStatus.setText(userStatus);
                }else{
                    String userName= snapshot.child("name").getValue().toString();
                    String userStatus = snapshot.child("status").getValue().toString();

                    userprofileName.setText(userName);
                    userProfileStatus.setText(userStatus);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
}