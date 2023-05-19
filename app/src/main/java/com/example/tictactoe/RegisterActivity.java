package com.example.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    EditText editTextEmail, editTextPassword, editTextConfirmPassword;
    Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageView myImageView = findViewById(R.id.imageViewRegister);
        myImageView.setImageResource(R.drawable.tictactoetitle);

        mDatabase = FirebaseDatabase.getInstance("https://tic-tac-toe-2f8a4-default-rtdb.europe-west1.firebasedatabase.app").getReference();
        editTextEmail = findViewById(R.id.username_register);
        editTextPassword = findViewById(R.id.password_login);
        editTextConfirmPassword = findViewById(R.id.confirm_password_register);

        buttonRegister = findViewById(R.id.register);

        // On Click Listener to allow the registration of a user
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username, password, confirmPassword;
                username = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                confirmPassword = String.valueOf(editTextConfirmPassword.getText());

                if(!(password.equals(confirmPassword))){ // Password Fields do not match in input
                    Toast.makeText(RegisterActivity.this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                }else { // adding values for user to correct tree branch in firebase storage
                    mDatabase.child("Accounts").child(username).child("password")
                            .setValue(password);

                    mDatabase.child("Accounts").child(username).child("username")
                            .setValue(username);

                    mDatabase.child("Leaderboard").child("Easy").child(username)
                            .child("Player Score").setValue(0);

                    mDatabase.child("Leaderboard").child("Easy").child(username)
                            .child("Computer Score").setValue(0);

                    mDatabase.child("Leaderboard").child("Medium").child(username)
                            .child("Player Score").setValue(0);

                    mDatabase.child("Leaderboard").child("Medium").child(username)
                            .child("Computer Score").setValue(0);

                    mDatabase.child("Leaderboard").child("Hard").child(username)
                            .child("Player Score").setValue(0);

                    mDatabase.child("Leaderboard").child("Hard").child(username)
                            .child("Computer Score").setValue(0);


                    Toast.makeText(RegisterActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), StartingPage.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}