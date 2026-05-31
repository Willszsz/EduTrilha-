package com.edutrilha.edutrilha;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static class Disciplina {
        final String nome;
        final float nota;
        final int iconRes;
        final int color;

        Disciplina(String nome, float nota, int iconRes, int color) {
            this.nome = nome;
            this.nota = nota;
            this.iconRes = iconRes;
            this.color = color;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout container = findViewById(R.id.containerDisciplinas);

        Disciplina[] disciplinas = new Disciplina[]{
                new Disciplina("Matemática", 5.8f, R.drawable.ic_matematica, Color.parseColor("#E53935")),
                new Disciplina("Programação", 6.2f, R.drawable.ic_programacao, Color.parseColor("#F59E0B")),
                new Disciplina("Banco de Dados", 8.5f, R.drawable.ic_banco, Color.parseColor("#2E7D32")),
                new Disciplina("Redes", 7.9f, R.drawable.ic_redes, Color.parseColor("#1565C0")),
                new Disciplina("Português", 9.0f, R.drawable.ic_portugues, Color.parseColor("#2E7D32"))
        };

        LayoutInflater inflater = LayoutInflater.from(this);
        for (Disciplina d : disciplinas) {
            View item = inflater.inflate(R.layout.item_disciplina, container, false);

            ImageView iconBg = item.findViewById(R.id.iconBg);
            ImageView icon = item.findViewById(R.id.icon);
            TextView tvNome = item.findViewById(R.id.tvNome);
            TextView tvNota = item.findViewById(R.id.tvNota);
            TextView tvStatus = item.findViewById(R.id.tvStatus);
            ProgressBar progress = item.findViewById(R.id.progress);

            int tintBg = (d.color & 0x00FFFFFF) | 0x22000000;
            iconBg.setColorFilter(tintBg, PorterDuff.Mode.SRC_IN);
            icon.setImageResource(d.iconRes);
            icon.setColorFilter(d.color, PorterDuff.Mode.SRC_IN);

            tvNome.setText(d.nome);
            tvNota.setText(String.format("%.1f", d.nota).replace('.', ','));
            tvNota.setTextColor(d.color);

            String status;
            if (d.nota >= 8f) status = "EXCELENTE";
            else if (d.nota >= 7f) status = "BOM";
            else if (d.nota >= 6f) status = "ATENÇÃO";
            else status = "CRÍTICO";
            tvStatus.setText(status);
            tvStatus.setTextColor(d.color);

            progress.setProgress((int) (d.nota * 10));
            GradientDrawable progressColor = new GradientDrawable();
            progressColor.setColor(d.color);
            progressColor.setCornerRadius(20f);

            android.graphics.drawable.LayerDrawable layer =
                    (android.graphics.drawable.LayerDrawable) ContextCompat.getDrawable(this, R.drawable.progress_disciplina);
            if (layer != null) {
                android.graphics.drawable.ClipDrawable clip = new android.graphics.drawable.ClipDrawable(
                        progressColor, android.view.Gravity.START,
                        android.graphics.drawable.ClipDrawable.HORIZONTAL);
                layer.setDrawableByLayerId(android.R.id.progress, clip);
                progress.setProgressDrawable(layer);
                progress.setProgress((int) (d.nota * 10));
            }

            container.addView(item);
        }

        findViewById(R.id.btnGerarTrilha).setOnClickListener(v ->
                Toast.makeText(this, "Gerando trilha de aprendizagem...", Toast.LENGTH_SHORT).show());
    }
}
