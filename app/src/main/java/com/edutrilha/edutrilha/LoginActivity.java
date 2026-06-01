package com.edutrilha.edutrilha;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;

    private UsuarioDAO usuarioDAO;
    private SessaoUsuario sessao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuarioDAO = new UsuarioDAO(this);
        sessao = new SessaoUsuario(this);

        if (sessao.isLogado()) {

            startActivity(
                    new Intent(
                            LoginActivity.this,
                            MainActivity.class
                    )
            );

            finish();
            return;
        }

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);

        Button btnEntrar = findViewById(R.id.btnEntrar);
        Button btnCadastrar = findViewById(R.id.btnCadastrar);

        btnEntrar.setOnClickListener(v -> {

            String email = edtEmail.getText().toString().trim();
            String senha = edtSenha.getText().toString().trim();

            Usuario usuario = usuarioDAO.login(email, senha);

            if (usuario == null) {

                Toast.makeText(
                        this,
                        "Email ou senha inválidos",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            sessao.login(usuario);

            startActivity(
                    new Intent(
                            LoginActivity.this,
                            MainActivity.class
                    )
            );

            finish();
        });

        btnCadastrar.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            LoginActivity.this,
                            CadastroActivity.class
                    )
            );
        });
    }
}