package com.edutrilha.edutrilha;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final ArrayList<Disciplina> disciplinas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        disciplinas.add(new Disciplina("Matemática", 5.8, R.drawable.ic_matematica, R.color.danger));
        disciplinas.add(new Disciplina("Programação", 6.2, R.drawable.ic_programacao, R.color.warning));
        disciplinas.add(new Disciplina("Banco de Dados", 8.5, R.drawable.ic_banco, R.color.success));
        disciplinas.add(new Disciplina("Redes", 7.9, R.drawable.ic_redes, R.color.primary));
        disciplinas.add(new Disciplina("Português", 9.0, R.drawable.ic_portugues, R.color.success));

        TextView tvMedia = findViewById(R.id.tvMedia);
        double soma = 0;
        for (Disciplina d : disciplinas) soma += d.nota;
        double media = soma / disciplinas.size();
        tvMedia.setText(String.format(Locale.getDefault(), "%.1f", media).replace('.', ','));

        LinearLayout listaDisciplinas = findViewById(R.id.listaDisciplinas);
        LayoutInflater inflater = LayoutInflater.from(this);

        for (final Disciplina d : disciplinas) {
            View item = inflater.inflate(R.layout.item_disciplina, listaDisciplinas, false);

            ImageView icon = item.findViewById(R.id.ivIcon);
            TextView tvNome = item.findViewById(R.id.tvNome);
            TextView tvNota = item.findViewById(R.id.tvNota);
            TextView tvStatus = item.findViewById(R.id.tvStatus);
            ProgressBar progress = item.findViewById(R.id.progress);

            int cor = ContextCompat.getColor(this, d.corRes);

            Drawable iconDrawable = ContextCompat.getDrawable(this, d.iconRes);
            if (iconDrawable != null) {
                iconDrawable.setColorFilter(new PorterDuffColorFilter(cor, PorterDuff.Mode.SRC_IN));
                icon.setImageDrawable(iconDrawable);
            }

            tvNome.setText(d.nome);
            tvNota.setText(String.format(Locale.getDefault(), "%.1f", d.nota).replace('.', ','));
            tvNota.setTextColor(cor);
            tvStatus.setText(d.statusTexto());
            tvStatus.setTextColor(cor);
            progress.setProgress((int) (d.nota * 10));
            progress.getProgressDrawable().setColorFilter(
                    new PorterDuffColorFilter(cor, PorterDuff.Mode.SRC_IN));

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this,
                            d.nome + " — Nota: " + String.format(Locale.getDefault(), "%.1f", d.nota)
                                    + " (" + d.statusTexto() + ")",
                            Toast.LENGTH_SHORT).show();
                }
            });

            listaDisciplinas.addView(item);
        }

        View btnGerar = findViewById(R.id.btnGerarTrilha);
        btnGerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, TrilhaActivity.class);
                it.putExtra("disciplinas", disciplinas);
                startActivity(it);
            }
        });
    }
}
