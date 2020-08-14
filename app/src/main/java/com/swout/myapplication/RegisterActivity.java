package com.swout.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccountButton;
    private EditText UserEmail,UserPassword;
    private TextView AlreadyHaveAccountLink;
    private FirebaseAuth mAuth;
    private ProgressDialog LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        InitializeFields();

        AlreadyHaveAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    SendUserToLoginActivity();
            }
        });

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });
    }

    private void CreateNewAccount() {
            String Email = UserEmail.getText().toString();
            String Password = UserPassword.getText().toString();

            if(TextUtils.isEmpty(Email)){
                Toast.makeText(this,"please enter an Email....",Toast.LENGTH_SHORT).show();
            }
            if(TextUtils.isEmpty(Password)){
            Toast.makeText(this,"please enter a Password....",Toast.LENGTH_SHORT).show();
            }
            else{

                LoadingBar.setTitle("Creating New Account");
                LoadingBar.setMessage("Please wait, While we are creating you new account...");
                LoadingBar.setCanceledOnTouchOutside(true);
                LoadingBar.show();

                mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            SendUserToLoginActivity();
                            Toast.makeText(RegisterActivity.this,"Account Create Successfully..",Toast.LENGTH_SHORT).show();
                            LoadingBar.dismiss();
                        }
                        else{
                            String messege = task.getException().toString();
                            Toast.makeText(RegisterActivity.this,"Error" + messege ,Toast.LENGTH_SHORT).show();
                            LoadingBar.dismiss();
                        }
                    }
                });
            }
    }

    private void InitializeFields() {

        CreateAccountButton = (Button) findViewById(R.id.register_button);
        UserEmail = (EditText) findViewById(R.id.register_email);
        UserPassword = (EditText) findViewById(R.id.register_password);
        AlreadyHaveAccountLink = (TextView) findViewById(R.id.already_have_account_link);

        LoadingBar = new ProgressDialog(this);
    }


    private void SendUserToLoginActivity() {
        Intent LoginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(LoginIntent);
    }
}