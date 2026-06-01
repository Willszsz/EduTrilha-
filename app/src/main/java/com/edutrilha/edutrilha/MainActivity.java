package com.edutrilha.edutrilha;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LinearLayout lista;

    private TextView tvMedia;
    private TextView tvTotalDisciplinas;
    private TextView tvTotalConcluidos;
    private TextView tvProgressoGeral;
    private TextView tvRecomendacao;
    private ProgressoStore store;
    private SessaoUsuario sessaoUsuario;
    private DisciplinaDAO disciplinaDAO;
    private TextView tvNomeUsuario;
    private TextView tvAvatar;
    private TextView tvQtdDisciplinasHeader;
    private TextView tvStatusDesempenho;

    private List<Disciplina> disciplinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessaoUsuario = new SessaoUsuario(this);

        if (!sessaoUsuario.isLogado()) {
            startActivity(
                    new Intent(
                            MainActivity.this,
                            LoginActivity.class
                    )
            );
            finish();
            return;
        }

        store = new ProgressoStore(this);
        disciplinaDAO = new DisciplinaDAO(this);

        lista = findViewById(R.id.listaDisciplinas);

        tvMedia = findViewById(R.id.tvMedia);
        tvTotalDisciplinas = findViewById(R.id.tvTotalDisciplinas);
        tvTotalConcluidos = findViewById(R.id.tvTotalConcluidos);
        tvProgressoGeral = findViewById(R.id.tvProgressoGeral);
        tvRecomendacao = findViewById(R.id.tvRecomendacao);
        tvNomeUsuario = findViewById(R.id.tvNomeUsuario);
        tvAvatar = findViewById(R.id.tvAvatar);
        tvQtdDisciplinasHeader =
                findViewById(
                        R.id.tvQtdDisciplinasHeader
                );

        String nomeUsuario = sessaoUsuario.getNome();

        tvNomeUsuario.setText(nomeUsuario);
        tvStatusDesempenho =
                findViewById(
                        R.id.tvStatusDesempenho
                );

        if (nomeUsuario != null && !nomeUsuario.isEmpty()) {

            String[] partes = nomeUsuario.split(" ");

            String iniciais = "";

            if (partes.length >= 2) {

                iniciais =
                        partes[0].substring(0, 1)
                                + partes[1].substring(0, 1);

            } else {

                iniciais =
                        nomeUsuario.substring(0, 1);
            }

            tvAvatar.setText(
                    iniciais.toUpperCase()
            );
        }

        Button btnGerar = findViewById(R.id.btnGerar);

        btnGerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (disciplinas == null || disciplinas.isEmpty()) {
                    Toast.makeText(
                            MainActivity.this,
                            "Cadastre uma disciplina primeiro",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                Disciplina pior = disciplinas.get(0);

                for (Disciplina d : disciplinas) {
                    if (d.nota < pior.nota) {
                        pior = d;
                    }
                }

                Intent i = new Intent(
                        MainActivity.this,
                        TrilhaActivity.class
                );

                i.putExtra("disciplina", pior);

                startActivity(i);
            }
        });

        Button btnLogout = findViewById(R.id.btnLogout);

        if (btnLogout != null) {

            btnLogout.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            sessaoUsuario.logout();

                            Intent intent =
                                    new Intent(
                                            MainActivity.this,
                                            LoginActivity.class
                                    );

                            intent.setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            );

                            startActivity(intent);
                            finish();
                        }
                    }
            );
        }

        Button btnCadastrarDisciplina =
                findViewById(
                        R.id.btnCadastrarDisciplina
                );

        if (btnCadastrarDisciplina != null) {

            btnCadastrarDisciplina.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            startActivity(
                                    new Intent(
                                            MainActivity.this,
                                            CadastrarDisciplinaActivity.class
                                    )
                            );
                        }
                    }
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        carregarDisciplinas();
        renderizarLista();
    }

    private void carregarDisciplinas() {

        disciplinas =
                disciplinaDAO.listar(
                        sessaoUsuario.getUsuarioId()
                );
    }

    @SuppressLint("SetTextI18n")
    private void renderizarLista() {

        lista.removeAllViews();

        if (disciplinas == null || disciplinas.isEmpty()) {

            tvMedia.setText("0.0");
            tvTotalDisciplinas.setText("0");
            tvQtdDisciplinasHeader.setText("0");
            tvTotalConcluidos.setText("0");
            tvProgressoGeral.setText("0%");

            tvStatusDesempenho.setText(
                    "Sem disciplinas"
            );

            tvStatusDesempenho.setBackgroundResource(
                    R.drawable.bg_badge_neutral
            );

            tvRecomendacao.setText(
                    "Cadastre uma disciplina para começar."
            );

            return;
        }

        LayoutInflater inflater =
                LayoutInflater.from(this);

        double somaNotas = 0;

        for (Disciplina d : disciplinas) {
            somaNotas += d.nota;
        }

        double media =
                somaNotas / disciplinas.size();

        tvMedia.setText(
                String.format(
                        Locale.getDefault(),
                        "%.1f",
                        media
                )
        );

        if (media >= 8.0) {

            tvStatusDesempenho.setText(
                    "Ótimo desempenho"
            );

            tvStatusDesempenho.setBackgroundResource(
                    R.drawable.bg_badge_success
            );

            tvStatusDesempenho.setTextColor(
                    ContextCompat.getColor(
                            this,
                            R.color.success
                    )
            );

        } else if (media >= 6.5) {

            tvStatusDesempenho.setText(
                    "Desempenho regular"
            );

            tvStatusDesempenho.setBackgroundResource(
                    R.drawable.bg_badge_warning
            );

            tvStatusDesempenho.setTextColor(
                    ContextCompat.getColor(
                            this,
                            R.color.warning
                    )
            );

        } else {

            tvStatusDesempenho.setText(
                    "Baixo desempenho"
            );

            tvStatusDesempenho.setBackgroundResource(
                    R.drawable.bg_badge_danger
            );

            tvStatusDesempenho.setTextColor(
                    ContextCompat.getColor(
                            this,
                            R.color.danger
                    )
            );
        }

        atualizarResumo();

        for (final Disciplina d : disciplinas) {

            View item =
                    inflater.inflate(
                            R.layout.item_disciplina,
                            lista,
                            false
                    );

            ImageView icon =
                    item.findViewById(R.id.icon);

            TextView nome =
                    item.findViewById(R.id.nome);

            TextView nota =
                    item.findViewById(R.id.nota);

            TextView status =
                    item.findViewById(R.id.status);

            ProgressBar progress =
                    item.findViewById(R.id.progress);

            TextView progressoTrilha =
                    item.findViewById(R.id.progressoTrilha);

            ProgressBar barraTrilha =
                    item.findViewById(R.id.barraTrilha);

            icon.setImageResource(d.iconRes);

            icon.setColorFilter(
                    ContextCompat.getColor(
                            this,
                            d.corRes
                    )
            );

            nome.setText(d.nome);

            nota.setText(
                    String.format(
                            Locale.getDefault(),
                            "%.1f",
                            d.nota
                    )
            );

            nota.setTextColor(
                    ContextCompat.getColor(
                            this,
                            d.corRes
                    )
            );

            status.setText(
                    d.statusTexto()
            );

            status.setTextColor(
                    ContextCompat.getColor(
                            this,
                            d.corRes
                    )
            );

            progress.setProgress(
                    (int) (d.nota * 10)
            );

            progress.getProgressDrawable()
                    .setTint(
                            ContextCompat.getColor(
                                    this,
                                    d.corRes
                            )
                    );

            int total =
                    d.trilhaTopicos().size();

            int feitos =
                    store.totalConcluidos(
                            d.nome
                    );

            int pct =
                    total == 0
                            ? 0
                            : (feitos * 100) / total;

            progressoTrilha.setText(
                    "Trilha: "
                            + feitos
                            + "/"
                            + total
                            + " ("
                            + pct
                            + "%)"
            );

            barraTrilha.setProgress(pct);

            barraTrilha.getProgressDrawable()
                    .setTint(
                            ContextCompat.getColor(
                                    this,
                                    d.corRes
                            )
                    );

            item.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent i =
                                    new Intent(
                                            MainActivity.this,
                                            TrilhaActivity.class
                                    );

                            i.putExtra(
                                    "disciplina",
                                    d
                            );

                            startActivity(i);
                        }
                    }
            );

            ImageView btnExcluir =
                    item.findViewById(R.id.btnExcluir);

            btnExcluir.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            new androidx.appcompat.app.AlertDialog.Builder(
                                    MainActivity.this
                            )
                                    .setTitle("Excluir disciplina")
                                    .setMessage(
                                            "Deseja realmente excluir a disciplina \""
                                                    + d.nome
                                                    + "\"?"
                                    )
                                    .setPositiveButton(
                                            "Excluir",
                                            (dialog, which) -> {

                                                disciplinaDAO.excluir(
                                                        sessaoUsuario.getUsuarioId(),
                                                        d.nome
                                                );

                                                carregarDisciplinas();
                                                renderizarLista();

                                                Toast.makeText(
                                                        MainActivity.this,
                                                        "Disciplina removida com sucesso",
                                                        Toast.LENGTH_SHORT
                                                ).show();
                                            }
                                    )
                                    .setNegativeButton(
                                            "Cancelar",
                                            null
                                    )
                                    .show();
                        }
                    }
            );

            lista.addView(item);
        }
    }

    @SuppressLint("SetTextI18n")
    private void atualizarResumo() {

        int totalTopicos = 0;
        int totalConcluidos = 0;

        Disciplina pior =
                disciplinas.get(0);

        for (Disciplina d : disciplinas) {

            if (d.nota < pior.nota) {
                pior = d;
            }

            totalTopicos +=
                    d.trilhaTopicos().size();

            totalConcluidos +=
                    store.totalConcluidos(
                            d.nome
                    );
        }

        int progressoGeral =
                totalTopicos == 0
                        ? 0
                        : (totalConcluidos * 100)
                        / totalTopicos;

        tvTotalDisciplinas.setText(
                String.valueOf(disciplinas.size())
        );

        tvQtdDisciplinasHeader.setText(
                String.valueOf(disciplinas.size())
        );

        tvTotalConcluidos.setText(
                String.valueOf(totalConcluidos)
        );

        tvProgressoGeral.setText(
                progressoGeral + "%"
        );

        int totalPior =
                pior.trilhaTopicos().size();

        int feitosPior =
                store.totalConcluidos(
                        pior.nome
                );

        int pctPior =
                totalPior == 0
                        ? 0
                        : (feitosPior * 100)
                        / totalPior;

        tvRecomendacao.setText(
                "Seu desempenho mais baixo está em "
                        + pior.nome
                        + " ("
                        + String.format(
                        Locale.getDefault(),
                        "%.1f",
                        pior.nota
                )
                        + ").\n\nVocê concluiu "
                        + pctPior
                        + "% da trilha dessa disciplina.\n\n"
                        + "Recomendamos continuar seus estudos por ela."
        );
    }
}