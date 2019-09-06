package br.edu.faculdadedelta.projetofirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import br.edu.faculdadedelta.projetofirebase.modelo.Usuario;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton btnEntrar;
    private MaterialButton btnNovo;
    private MaterialButton btnCadastrar;
    private MaterialButton btnCancelar;
    private MaterialButton btnEsqueciSenha;

    private TextInputEditText etLoginEmail;
    private TextInputEditText etLoginSenha;

    private LinearLayout frameLoginTop;
    private FrameLayout frameLogin;
    private FrameLayout frameCadastro;

    private LoginHelper helper;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        helper = new LoginHelper(this);

        etLoginEmail = findViewById(R.id.loginEmail);
        etLoginSenha = findViewById(R.id.loginSenha);
        frameLoginTop = findViewById(R.id.frameLoginTop);
        frameLogin = findViewById(R.id.frameLogin);
        frameCadastro = findViewById(R.id.frameCadastro);

        btnNovo = findViewById(R.id.btnNovoCadastro);
        btnNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLoginTop.setVisibility(View.GONE);
                frameLogin.setVisibility(View.GONE);
                frameCadastro.setVisibility(View.VISIBLE);
                helper.limparCampos();
            }
        });

        btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameCadastro.setVisibility(View.GONE);
                frameLogin.setVisibility(View.VISIBLE);
                frameLoginTop.setVisibility(View.VISIBLE);
                helper.limparCampos();
            }
        });

        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.isCadastroValid())
                    cadastrar();
            }
        });

        btnEntrar = findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.isLoginValid())
                    logar();
            }
        });

        btnEsqueciSenha = findViewById(R.id.btnEsqueciMinhaSenha);
        btnEsqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), EsqueciMinhaSenha.class);
                startActivity(intent);
            }
        });
    }

    private void cadastrar() {
        final Usuario usuario = helper.popularModelo();

        mAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();
                            if (user != null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(usuario.getNome()).build();
                                user.updateProfile(profileUpdates);
                            }
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Falha ao efetuar o cadastro! Tente novamente..",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        frameCadastro.setVisibility(View.GONE);
        frameLogin.setVisibility(View.VISIBLE);
        helper.limparCampos();
    }

    private void logar() {
        mAuth.signInWithEmailAndPassword(etLoginEmail.getText().toString(), etLoginSenha.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            // [Dica] do Andre - Não informar qual dos dois estão incorretos para evitar brute force.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "E-mail e/ou senha incorretos!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}
