package br.edu.faculdadedelta.projetofirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class EsqueciMinhaSenha extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText etResetPassword;
    private MaterialButton btnResetar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_minha_senha);


        mAuth = FirebaseAuth.getInstance();

        etResetPassword = findViewById(R.id.resetPassword);

        btnResetar = findViewById(R.id.btnEsqueciMinhaSenha);
        btnResetar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmailValid())
                    resetarSenha(etResetPassword.getText().toString());
            }
        });
    }

    private boolean isEmailValid() {
        if(TextUtils.isEmpty(etResetPassword.getText().toString())) {
            return false;
        }

        return true;
    }

    private void resetarSenha(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "Um e-mail foi enviado para reset da senha!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                } else
                    Toast.makeText(getBaseContext(), "E-mail não encontrado!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
