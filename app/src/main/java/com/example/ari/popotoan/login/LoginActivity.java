package com.example.ari.popotoan.login;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ari.popotoan.R;
import com.example.ari.popotoan.fragment.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListen;
    private Context mKonteks;
    EditText eEmail, ePass;
    ProgressBar mProgressbar;
    TextView tvPb;

    private static final String TAG ="LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //memanggil komponen komponen layout dalam bentuk id
        //agar nilai dari tiap komponen dapat di simpan
        eEmail = (EditText) findViewById(R.id.email);
        ePass = (EditText) findViewById(R.id.password);
        Button btnDaftar = (Button) findViewById(R.id.btnDaftar);
        mProgressbar = (ProgressBar) findViewById(R.id.login_progress);
        tvPb = (TextView) findViewById(R.id.tvPB);
        mKonteks = LoginActivity.this;
        Log.d(TAG,"onCreate: mulai");

        tvPb.setVisibility(View.GONE);
        mProgressbar.setVisibility(View.GONE);

        //menjalankan method FirebaseAuthentication
        setUpFirebaseAuth();
        //menjalankan method init
        init();

        //listener yang digunakan untuk intent ke aktivitas selanjutnya
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, DaftarAct.class);
                startActivity(i);
                Toast.makeText(LoginActivity.this, "Ayo segera daftar", Toast.LENGTH_SHORT).show();

            }
        });
    }
    //Method untuk menginisialisasi apakah Suatu String bernilai Null
    private boolean StringNull(String string){
        if (string.equals("")){
            return false;
        }else {
            return true;
        }
    }
    /*
    -----------------------------------------Firebase-----------------------------------------------
    */
    //Method dimana untuk menginisialisasi
    // dan menangkap nilai dari Edit Text
    private void init(){
        //menginisialisasi tombol login
        Button eLogin = (Button) findViewById(R.id.btn_signin);
        // menghilangkan atau mereset notifikasi error merah
        eEmail.setError(null);
        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: Mencoba untuk login");

                // menyimpan nilai email &password dalam text dan merubahnya ke String
                String email = eEmail.getText().toString();
                String password = ePass.getText().toString();
                //logika jika field email&password kosong maka muncul toast
                if (!StringNull(email) &&(!StringNull(password))){
                    Toast.makeText(mKonteks, "Kamu harus isi bagian yang kosong", Toast.LENGTH_SHORT).show();
                    // fungsi dimana saat email field tidak diisi akan muncul notifikasi error
                    eEmail.setError(getString(R.string.error_field_required));
                }else{
                    //memunculkan progressbar jika tombol ditekan
                    mProgressbar.setVisibility(View.VISIBLE);
                    //memunculkan textview bersamaan dengan progress bar
                    tvPb.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()){
                                        Log.d(TAG,"LoginDenganEmail: GAGAL", task.getException());
                                        Toast.makeText(LoginActivity.this,getString(R.string.auth_fail), Toast.LENGTH_SHORT).show();
                                        mProgressbar.setVisibility(View.GONE);
                                        tvPb.setVisibility(View.GONE);
                                    }else{
                                        Toast.makeText(LoginActivity.this,getString(R.string.auth_success) , Toast.LENGTH_SHORT).show();
                                        mProgressbar.setVisibility(View.GONE);
                                        tvPb.setVisibility(View.GONE);

                                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(i);
                                    }
                                }
                            });
                }
            }
        });
    }

    //Method yang disediakan Firebase untuk Aplikasi terhubung dengan Firebase
    private void setUpFirebaseAuth(){
        Log.d(TAG,"SettingFirebaseAuth : Men setting Firebase Auth");

        mAuth = FirebaseAuth.getInstance();
        mAuthListen = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    //LOG JIKA USER LOGIN DAN MENAMPILKAN USER ID NYA SESUAI DATABASE
                    Log.d(TAG,"onAuthStateChanged: Signed in" + user.getUid());
                }else {
                    Log.d(TAG,"onAuthStateChanged: Signed out");
                }

            }
        };
    }
}
