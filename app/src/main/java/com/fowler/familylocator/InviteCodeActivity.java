package com.fowler.familylocator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InviteCodeActivity extends AppCompatActivity {

    String name, email, password, date, code, isSharing;
    Uri imageUri;
    ProgressDialog progressDialog;

    TextView t1;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);
        t1 = findViewById(R.id.code_display_text);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        Intent myIntent = getIntent();

        if (myIntent != null) {
            name = myIntent.getStringExtra("name");
            email = myIntent.getStringExtra("email");
            password = myIntent.getStringExtra("password");
            code = myIntent.getStringExtra("code");
            isSharing = myIntent.getStringExtra("isSharing");
            imageUri = myIntent.getParcelableExtra("imageUri");
        }

        t1.setText(code);

    }

    public void registerUser(View view) {
        progressDialog.setMessage("Please wait; setting up your account");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // insert values in realtime to the database
                            CreateUser createUser = new CreateUser(name, email, password, code, "false", "na", "na", "na");

                            user = auth.getCurrentUser();
                            userId = user.getUid();


                            reference.child(userId).setValue(createUser)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();

                                            Toast.makeText(getApplicationContext(), "User was successfully registered", Toast.LENGTH_SHORT).show();
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Could not register user", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });



                        }
                    }
                });



    }
}
