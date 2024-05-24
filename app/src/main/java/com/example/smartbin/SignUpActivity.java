package com.example.smartbin;

import static androidx.fragment.app.FragmentManager.TAG;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword, nom, prenom;
    Button buttonReg;
    private FirebaseAuth auth;
    TextView textView;
    FirebaseFirestore db;
    @SuppressLint({"WrongViewCast", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editTextEmail = findViewById(R.id.textViewEmail);
        editTextPassword = findViewById(R.id.Password);
        nom = findViewById(R.id.Nom);
        prenom = findViewById(R.id.Prenom);
        buttonReg = findViewById(R.id.buttonSignup);
        textView = findViewById(R.id.buttonLogin);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        textView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
            startActivity(intent);
            finish();
        });
        buttonReg.setOnClickListener(v -> {
            final String email, password;
            email = Objects.requireNonNull(editTextEmail.getText()).toString().trim();
            password = Objects.requireNonNull(editTextPassword.getText()).toString().trim();
            final String nomValue = Objects.requireNonNull(nom.getText()).toString().trim();
            final String prenomValue = Objects.requireNonNull(prenom.getText()).toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(nomValue) || TextUtils.isEmpty(prenomValue)) {
                Toast.makeText(SignUpActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Authentification de l'utilisateur
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            //stockage
                            // Create a new user with a first, middle, and last name
                            String nomValue1 = String.valueOf(nom.getText());
                            String prenomValue1 = String.valueOf(prenom.getText());


// Create a new user with a first, middle, and last name
                            Map<String, Object> userdoc = new HashMap<>();
                            userdoc.put("nom", nomValue1);
                            userdoc.put("prenom", prenomValue1);
                            userdoc.put("email", email);
                            userdoc.put("password", password);

                            // Enregistrer les données utilisateur dans Firestore
                            db.collection("user").document(email)
                                    .set(userdoc)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "Document utilisateur ajouté avec succès à Firestore !");
                                        Toast.makeText(SignUpActivity.this, "Compte créé avec succès", Toast.LENGTH_SHORT).show();
                                        // Rediriger l'utilisateur vers l'activité de gestion des tâches
                                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Erreur lors de l'ajout du document utilisateur à Firestore", e);
                                        Toast.makeText(SignUpActivity.this, "Erreur lors de la création du compte", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                    );
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Vérifier si l'utilisateur est déjà connecté
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
}