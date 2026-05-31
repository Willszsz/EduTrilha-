package com.edutrilha.edutrilha;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class TrilhaActivity extends AppCompatActivity {

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trilha);

        findViewById(R.id.btnVoltar).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { finish(); }
        });

        ArrayList<Disciplina> recebidas =
                (ArrayList<Disciplina>) getIntent().getSerializableExtra("disciplinas");
        if (recebidas == null) recebidas = new ArrayList<>();

        Collections.sort(recebidas, new Comparator<Disciplina>() {
            @Override public int compare(Disciplina a, Disciplina b) {
                return Double.compare(a.nota, b.nota);
            }
        });

        LinearLayout lista = findViewById(R.id.listaTrilhas);
        LayoutInflater inflater = LayoutInflater.from(this);

        for (final Disciplina d : recebidas) {
            View item = inflater.inflate(R.layout.item_trilha, lista, false);

            ImageView icon = item.findViewById(R.id.ivIcon);
            TextView tvNome = item.findViewById(R.id.tvNome);
            TextView tvRec = item.findViewById(R.id.tvRecomendacao);
            View btnIniciar = item.findViewById(R.id.btnIniciar);

            int cor = ContextCompat.getColor(this, d.corRes);
            Drawable ic = ContextCompat.getDrawable(this, d.iconRes);
            if (ic != null) {
                ic.setColorFilter(new PorterDuffColorFilter(cor, PorterDuff.Mode.SRC_IN));
                icon.setImageDrawable(ic);
            }

            tvNome.setText(d.nome);
            tvRec.setText(gerarRecomendacao(d));

            btnIniciar.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Toast.makeText(TrilhaActivity.this,
                            "Trilha iniciada: " + d.nome, Toast.LENGTH_SHORT).show();
                }
            });

            lista.addView(item);
        }
    }

    private String gerarRecomendacao(Disciplina d) {
        if (d.nota < 6.0)
            return "Reforço urgente. Revise conceitos básicos e faça exercícios diários.";
        if (d.nota < 7.0)
            return "Atenção. Revise os principais tópicos e pratique semanalmente.";
        if (d.nota < 8.0)
            return "Bom desempenho. Aprofunde-se nos tópicos avançados.";
        return "Excelente. Mantenha o ritmo com desafios extras.";
    }
}

