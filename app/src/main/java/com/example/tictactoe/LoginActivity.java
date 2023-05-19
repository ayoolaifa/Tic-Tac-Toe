package com.example.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    EditText editTextEmail, editTextPassword;
    Button buttonSignIn, buttonMoveToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase Reference
        mDatabase = FirebaseDatabase.getInstance("https://tic-tac-toe-2f8a4-default-rtdb.europe-west1.firebasedatabase.app").getReference();
        editTextEmail = findViewById(R.id.username_login);
        editTextPassword = findViewById(R.id.password_login);

        buttonSignIn = findViewById(R.id.sign_in);
        buttonMoveToRegister = findViewById(R.id.button_mv_to_register);

        ImageView myImageView = findViewById(R.id.imageViewLogin);
        myImageView.setImageResource(R.drawable.tictactoetitle);

        // On Click Listener to move from Login Intent to Register Intent
        buttonMoveToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        // On Click Listener to move from Login Intent to Starting page intent
        // Only moves intent if the input from user is a valid account on firebase storage
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Ues");
                String username, password;
                username = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                mDatabase.child("Accounts") // Getting snapshot of every account that has been registered
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List l = new ArrayList<String>();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()){
                            l.add(postSnapshot.getKey().toString());
                        }
                        if (l.contains(username)){  // checking username inputted by user is in the database
                            mDatabase.child("Accounts").child(username).child("password")
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String actualPassword = String.valueOf(snapshot.getValue());
                                            if (password.equals(actualPassword)){  // checking the password inputted by user matches that which was registered on the account
                                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), StartingPage.class);
                                                intent.putExtra("username", username);  // adding logged in username to the intent move
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                Toast.makeText(LoginActivity.this, "Wrong Password Please try again",
                                                        Toast.LENGTH_SHORT).show();
                                                editTextPassword.setText("");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.e("onCancelled", " cancelled");
                                        }
                                    });
                        }else{ // If the username is not present in the database
                            Toast.makeText(LoginActivity.this, "Invalid Username. Please Change Or Register Account",
                                    Toast.LENGTH_LONG).show();
                            editTextPassword.setText("");
                            editTextEmail.setText("");
                        }
                    }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("onCancelled", " cancelled");
                            }

                        });


            }
        });



    }
}