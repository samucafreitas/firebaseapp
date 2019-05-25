package br.edu.faculdadedelta.projetofirebase;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
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
    private TextInputLayout ilResetPassword;
    private TextInputEditText etResetPassword;
    private MaterialButton btnResetar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_minha_senha);

        mAuth = FirebaseAuth.getInstance();

        ilResetPassword = findViewById(R.id.hintResetPassword);
        etResetPassword = findViewById(R.id.resetPassword);

        btnResetar = findViewById(R.id.btnResetar);
        btnResetar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmailValid())
                    resetarSenha(etResetPassword.getText().toString());
            }
        });
    }

    private void resetarSenha(final String email) {
        new AlertDialog.Builder(EsqueciMinhaSenha.this)
                .setTitle("Confirmação")
                .setMessage("Confirmar envio de reset de senha?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        enviarEmailReset(email);
                    }
                })
                .setNegativeButton("Não", null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();

    }

    private void enviarEmailReset(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EsqueciMinhaSenha.this, "E-mail enviado!", Toast.LENGTH_SHORT).show();;
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    new AlertDialog.Builder(EsqueciMinhaSenha.this)
                            .setTitle("E-mail")
                            .setMessage("Falha, tente novamente!")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
    }

    private boolean isEmailValid() {
        if(TextUtils.isEmpty(etResetPassword.getText().toString())) {
            ilResetPassword.setError(" ");
            return false;
        }

        return true;
    }
}
