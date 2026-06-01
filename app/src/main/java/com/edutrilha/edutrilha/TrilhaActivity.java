package com.edutrilha.edutrilha;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.List;

public class TrilhaActivity extends AppCompatActivity {

    private Disciplina disciplina;
    private ProgressoStore store;
    private ProgressBar barraTotal;
    private TextView tvProgressoTotal;
    private int totalTopicos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trilha);

        store = new ProgressoStore(this);
        disciplina = (Disciplina) getIntent().getSerializableExtra("disciplina");

        if (disciplina == null) {
            Toast.makeText(this, "Disciplina não encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView titulo = findViewById(R.id.tvTitulo);
        TextView subtitulo = findViewById(R.id.tvSubtitulo);
        TextView recomendacao = findViewById(R.id.tvRecomendacao);
        ImageView icon = findViewById(R.id.iconDisciplina);
        LinearLayout lista = findViewById(R.id.listaTrilhas);
        Button btnVoltar = findViewById(R.id.btnVoltar);
        barraTotal = findViewById(R.id.barraTotal);
        tvProgressoTotal = findViewById(R.id.tvProgressoTotal);

        titulo.setText("Trilha de " + disciplina.nome);
        subtitulo.setText("Nota atual: " + String.format("%.1f", disciplina.nota)
                + " • " + disciplina.statusTexto());
        recomendacao.setText(disciplina.recomendacao());
        icon.setImageResource(disciplina.iconRes);
        icon.setColorFilter(ContextCompat.getColor(this, disciplina.corRes));

        List<String> topicos = disciplina.trilhaTopicos();
        totalTopicos = topicos.size();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int idx = 0; idx < topicos.size(); idx++) {
            final String topico = topicos.get(idx);

            View item = inflater.inflate(R.layout.item_trilha, lista, false);
            final CheckBox check = item.findViewById(R.id.checkTopico);
            TextView nomeTopico = item.findViewById(R.id.tvTopico);
            Button btnIniciar = item.findViewById(R.id.btnIniciar);

            nomeTopico.setText(topico);
            check.setChecked(store.isConcluido(disciplina.nome, topico));
            aplicarRiscado(nomeTopico, check.isChecked());

            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    store.setConcluido(disciplina.nome, topico, isChecked);
                    aplicarRiscado(nomeTopico, isChecked);
                    atualizarBarra();
                }
            });

            btnIniciar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(TrilhaActivity.this,
                            "Iniciando: " + topico, Toast.LENGTH_SHORT).show();
                }
            });

            lista.addView(item);
        }

        barraTotal.getProgressDrawable().setTint(
                ContextCompat.getColor(this, disciplina.corRes));
        atualizarBarra();

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });
    }

    private void aplicarRiscado(TextView tv, boolean concluido) {
        if (concluido) {
            tv.setPaintFlags(tv.getPaintFlags()
                    | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
            tv.setAlpha(0.6f);
        } else {
            tv.setPaintFlags(tv.getPaintFlags()
                    & (~android.graphics.Paint.STRIKE_THRU_TEXT_FLAG));
            tv.setAlpha(1f);
        }
    }

    private void atualizarBarra() {
        int feitos = store.totalConcluidos(disciplina.nome);
        int pct = totalTopicos == 0 ? 0 : (feitos * 100) / totalTopicos;
        barraTotal.setMax(100);
        barraTotal.setProgress(pct);
        tvProgressoTotal.setText(feitos + " de " + totalTopicos
                + " concluídos (" + pct + "%)");
    }
}
