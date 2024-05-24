package com.example.smartbin;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {

        EditText editTextEmail, editTextPassword;
        Button buttonlogin;
        FirebaseAuth auth;
        TextView buttonSignup;
        String email, password;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            editTextEmail = findViewById(R.id.email);
            editTextPassword = findViewById(R.id.password);
            buttonlogin= findViewById(R.id.buttonLogin);
            buttonSignup = findViewById(R.id.buttonSignup);
            auth = FirebaseAuth.getInstance();

            buttonSignup.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            });


            buttonlogin.setOnClickListener(v -> {

                email = String.valueOf(editTextEmail.getText());
                auth = FirebaseAuth.getInstance();
                password = String.valueOf(editTextPassword.getText());
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Login_Activity.this,"Enter email" , Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Login_Activity.this,"Enter password" , Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(
                                        Login_Activity.this,
                                        "Login Successful.",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(
                                        Login_Activity.this,
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        });

            });
        }
        @Override
        public void onStart() {
            super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null) {
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();        }
        }
    }
