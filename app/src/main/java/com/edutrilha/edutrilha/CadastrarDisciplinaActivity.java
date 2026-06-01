package com.edutrilha.edutrilha;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CadastrarDisciplinaActivity extends AppCompatActivity {

    private Spinner spinnerDisciplina;
    private EditText etNota;

    private DisciplinaDAO disciplinaDAO;
    private SessaoUsuario sessao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_disciplina);

        spinnerDisciplina =
                findViewById(R.id.spinnerDisciplina);

        etNota =
                findViewById(R.id.etNota);

        Button btnSalvar =
                findViewById(R.id.btnSalvar);

        disciplinaDAO =
                new DisciplinaDAO(this);

        sessao =
                new SessaoUsuario(this);

        String[] disciplinas = {
                "Matemática",
                "Programação",
                "Banco de Dados",
                "Redes",
                "Português"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        disciplinas
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerDisciplina.setAdapter(adapter);

        btnSalvar.setOnClickListener(v -> salvar());
    }

    private void salvar() {

        String nome =
                spinnerDisciplina
                        .getSelectedItem()
                        .toString();

        String notaTexto =
                etNota.getText()
                        .toString()
                        .trim();

        if (TextUtils.isEmpty(notaTexto)) {

            Toast.makeText(
                    this,
                    "Informe a nota",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        double nota;

        try {

            nota =
                    Double.parseDouble(
                            notaTexto
                    );

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Nota inválida",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (nota < 0 || nota > 10) {

            Toast.makeText(
                    this,
                    "A nota deve estar entre 0 e 10",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        disciplinaDAO.salvar(
                sessao.getUsuarioId(),
                nome,
                nota
        );

        Toast.makeText(
                this,
                "Disciplina cadastrada com sucesso",
                Toast.LENGTH_SHORT
        ).show();

        finish();
    }
}