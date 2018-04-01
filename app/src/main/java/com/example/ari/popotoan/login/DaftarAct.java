package com.example.ari.popotoan.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ari.popotoan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class DaftarAct extends AppCompatActivity {
    EditText etEmail, etNamaL, etPass;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        setupUI();

        firebaseAuth = FirebaseAuth.getInstance();


        TextView tv1 = (TextView) findViewById(R.id.tvReg);
        Button btnReg = (Button) findViewById(R.id.btnRegist) ;

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    //memasukan data ke database
                    String userEmail = etEmail.getText().toString().trim();
                    String userName = etNamaL.getText().toString().trim();
                    String userPass= etPass.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(DaftarAct.this, "Registrasi Berhasil!",
                                        Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(DaftarAct.this, LoginActivity.class);
                                startActivity(i);
                            }else {
                                Toast.makeText(DaftarAct.this, "Registrasi Gagal!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DaftarAct.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
    private void setupUI(){
        etEmail = (EditText) findViewById(R.id.etEmail);
        etNamaL = (EditText) findViewById(R.id.ETnamalengkap);
        etPass = (EditText) findViewById(R.id.etPassword);
    }
    private boolean validate(){
        Boolean hasil = false;

        String email = etEmail.getText().toString();
        String namaL = etNamaL.getText().toString();
        String pass = etPass.getText().toString();

        if (email.isEmpty()&& namaL.isEmpty() && pass.isEmpty()){
            Toast.makeText(this, "Harap di isi semua", Toast.LENGTH_SHORT).show();
        }else{
            hasil = true;
        }
        return hasil;
    }
}
