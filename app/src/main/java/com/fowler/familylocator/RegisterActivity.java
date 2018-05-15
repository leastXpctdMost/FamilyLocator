package com.fowler.familylocator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class RegisterActivity extends AppCompatActivity {


    EditText e4_email;
    FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        e4_email = findViewById(R.id.register_email);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
    }


    public void goToPasswordActivity(View view) {

        dialog.setMessage("Checking for the Email Address");
        dialog.show();

        // Check for a valid and registered email
        auth.fetchProvidersForEmail(e4_email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {

                        if (task.isSuccessful()) {

                            dialog.dismiss();
                            boolean check = !task.getResult().getProviders().isEmpty();

                            if (!check) {
                                // The email doesn't exist, so we can create a new user email
                                Intent myIntent = new Intent(RegisterActivity.this,
                                        PasswordActivity.class);
                                myIntent.putExtra("email", e4_email.getText().toString());
                                startActivity(myIntent);



                            } else {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(),
                                        "This email has already been registered",
                                        Toast.LENGTH_SHORT).show();
                            } 
                        }

                    }
                });
    }
}
