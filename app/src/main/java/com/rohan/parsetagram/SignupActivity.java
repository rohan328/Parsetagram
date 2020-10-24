package com.rohan.parsetagram;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btSignup;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        actionBar=getSupportActionBar();
        actionBar.hide();

        etUsername=findViewById(R.id.etUsernameS);
        etPassword=findViewById(R.id.etPasswordS);
        btSignup=findViewById(R.id.btSignupS);

        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                registerUser(username, password);
            }
        });


    }

    private void registerUser(String username, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null){
                    Log.e("SignupActivity", "error signing up", e);
                    return;
                }
                Toast.makeText(SignupActivity.this, "Signup successful. Log in using your new credentials.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}