package com.example.inventorymanagement;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText txtUser, txtPass;
    private ImageView btnExit;
    private MaterialButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUser = findViewById(R.id.txtUsername);
        txtPass = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btn_login);
        btnExit = findViewById(R.id.btn_loginExit);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = String.valueOf(txtUser.getText());
                String pass = String.valueOf(txtPass.getText());
                if(user.equals("admin") && pass.equals("123456")){
                    Toast.makeText(getApplicationContext(), "You are now logged in!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else if(user.equals("")){
                    Toast.makeText(getApplicationContext(), "Invalid! Empty username.", Toast.LENGTH_SHORT).show();
                } else if(pass.equals("")){
                    Toast.makeText(getApplicationContext(), "Invalid! Empty password.", Toast.LENGTH_SHORT).show();
                } else {
                    txtPass.setText("");
                    Toast.makeText(getApplicationContext(), "Incorrect username or password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
    }

}