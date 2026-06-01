package com.edutrilha.edutrilha;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CadastroActivity extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtSenha;

    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        usuarioDAO = new UsuarioDAO(this);

        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);

        Button btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(v -> cadastrar());
    }

    private void cadastrar() {

        String nome = edtNome.getText()
                .toString()
                .trim();

        String email = edtEmail.getText()
                .toString()
                .trim();

        String senha = edtSenha.getText()
                .toString()
                .trim();

        if (nome.isEmpty()) {
            edtNome.setError("Informe seu nome");
            edtNome.requestFocus();
            return;
        }

        if (nome.length() < 3) {
            edtNome.setError("Nome muito curto");
            edtNome.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            edtEmail.setError("Informe o email");
            edtEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            edtEmail.setError("Email inválido");
            edtEmail.requestFocus();
            return;
        }

        if (senha.isEmpty()) {
            edtSenha.setError("Informe a senha");
            edtSenha.requestFocus();
            return;
        }

        if (senha.length() < 6) {
            edtSenha.setError(
                    "A senha deve ter no mínimo 6 caracteres"
            );
            edtSenha.requestFocus();
            return;
        }

        if (usuarioDAO.emailExiste(email)) {

            edtEmail.setError(
                    "Este email já está cadastrado"
            );

            edtEmail.requestFocus();

            return;
        }

        Usuario usuario =
                new Usuario(
                        nome,
                        email,
                        senha
                );

        long id =
                usuarioDAO.cadastrar(usuario);

        if (id > 0) {

            Toast.makeText(
                    this,
                    "Conta criada com sucesso",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Erro ao criar conta",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}