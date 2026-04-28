package com.example.minhaculinriaapp.ui.detalhes;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.minhaculinriaapp.R;
import com.example.minhaculinriaapp.data.entity.Ingrediente;
import com.example.minhaculinriaapp.data.entity.Passo;
import com.example.minhaculinriaapp.data.entity.ReceitaResumida;
import com.example.minhaculinriaapp.viewmodel.DetalhesReceitaViewModel;

import java.util.List;

public class DetalhesReceitaFragment extends Fragment {

    private DetalhesReceitaViewModel viewModel;

    private ImageView ivFoto;
    private TextView tvNome, tvCategoria, tvDificuldade, tvDescricao, tvTempo, tvPorcoes;
    private LinearLayout containerIngredientes, containerPassos, containerTags;
    private View scrollTags;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalhes_receita, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(DetalhesReceitaViewModel.class);

        ivFoto = view.findViewById(R.id.iv_foto);
        tvNome = view.findViewById(R.id.tv_nome);
        tvCategoria = view.findViewById(R.id.tv_categoria);
        tvDificuldade = view.findViewById(R.id.tv_dificuldade);
        tvDescricao = view.findViewById(R.id.tv_descricao);
        tvTempo = view.findViewById(R.id.tv_tempo);
        tvPorcoes = view.findViewById(R.id.tv_porcoes);
        containerIngredientes = view.findViewById(R.id.container_ingredientes);
        containerPassos = view.findViewById(R.id.container_passos);
        containerTags = view.findViewById(R.id.container_tags);
        scrollTags = view.findViewById(R.id.scroll_tags);

        view.findViewById(R.id.btn_back).setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        view.findViewById(R.id.btn_iniciar_execucao).setOnClickListener(v -> {
            long receitaId = getArguments() != null ? getArguments().getLong("receitaId", -1) : -1;
            if (receitaId != -1) {
                Bundle args = new Bundle();
                args.putLong("receitaId", receitaId);
                Navigation.findNavController(v).navigate(R.id.action_detalhes_to_maos_massa, args);
            }
        });

        // Receber o ID passado pela navegação
        long receitaId = getArguments() != null ? getArguments().getLong("receitaId", -1) : -1;
        if (receitaId != -1) {
            viewModel.carregarReceita(receitaId);
        }

        viewModel.receita.observe(getViewLifecycleOwner(), this::preencherCabecalho);
        viewModel.ingredientes.observe(getViewLifecycleOwner(), this::preencherIngredientes);
        viewModel.passos.observe(getViewLifecycleOwner(), this::preencherPassos);
    }

    private void preencherCabecalho(ReceitaResumida r) {
        if (r == null) return;

        tvNome.setText(r.nome);
        tvDescricao.setText(r.descricao != null ? r.descricao : "");
        tvCategoria.setText(r.categoriaNome != null ? r.categoriaNome : "Geral");
        tvDificuldade.setText(r.dificuldade != null ? r.dificuldade : "");

        tvTempo.setText(r.tempoMinutos != null ? r.tempoMinutos + " min" : "—");
        tvPorcoes.setText(r.rendimento != null ? String.valueOf(r.rendimento) : "—");

        if (r.fotoPath != null && !r.fotoPath.isEmpty()) {
            try {
                ivFoto.setImageURI(Uri.parse(r.fotoPath));
            } catch (Exception e) {
                ivFoto.setImageResource(R.drawable.receita1);
            }
        }

        // Tags
        containerTags.removeAllViews();
        if (!TextUtils.isEmpty(r.tags)) {
            scrollTags.setVisibility(View.VISIBLE);
            for (String tag : r.tags.split(",")) {
                String t = tag.trim();
                if (!t.isEmpty()) {
                    TextView chip = (TextView) LayoutInflater.from(requireContext())
                            .inflate(R.layout.item_tag_chip, containerTags, false);
                    chip.setText(t);
                    chip.setBackground(ContextCompat.getDrawable(requireContext(),
                            R.drawable.chip_category_selected));
                    chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.on_primary));
                    chip.setClickable(false);
                    containerTags.addView(chip);
                }
            }
        } else {
            scrollTags.setVisibility(View.GONE);
        }
    }

    private void preencherIngredientes(List<Ingrediente> lista) {
        containerIngredientes.removeAllViews();
        if (lista == null || lista.isEmpty()) {
            adicionarTextoVazio(containerIngredientes, "Nenhum ingrediente cadastrado");
            return;
        }
        for (Ingrediente ing : lista) {
            TextView tv = new TextView(requireContext());
            String texto = "• ";
            if (!TextUtils.isEmpty(ing.quantidade)) texto += ing.quantidade + "  ";
            texto += ing.nome != null ? ing.nome : "";
            tv.setText(texto);
            tv.setTextSize(15f);
            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface));
            tv.setPadding(0, 0, 0, dpToPx(8));
            containerIngredientes.addView(tv);
        }
    }

    private void preencherPassos(List<Passo> lista) {
        containerPassos.removeAllViews();
        if (lista == null || lista.isEmpty()) {
            adicionarTextoVazio(containerPassos, "Nenhum passo cadastrado");
            return;
        }
        for (Passo passo : lista) {
            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            rowParams.bottomMargin = dpToPx(16);
            row.setLayoutParams(rowParams);

            // Badge com número
            TextView badge = new TextView(requireContext());
            badge.setText(String.valueOf(passo.numero));
            badge.setTextSize(13f);
            badge.setTextColor(ContextCompat.getColor(requireContext(), R.color.on_primary));
            badge.setBackground(ContextCompat.getDrawable(requireContext(),
                    R.drawable.chip_category_selected));
            badge.setGravity(android.view.Gravity.CENTER);
            int badgeSize = dpToPx(28);
            LinearLayout.LayoutParams badgeParams = new LinearLayout.LayoutParams(badgeSize, badgeSize);
            badgeParams.rightMargin = dpToPx(12);
            badgeParams.topMargin = dpToPx(2);
            badge.setLayoutParams(badgeParams);
            row.addView(badge);

            // Descrição
            TextView tvDesc = new TextView(requireContext());
            tvDesc.setText(passo.descricao != null ? passo.descricao : "");
            tvDesc.setTextSize(15f);
            tvDesc.setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface));
            LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            tvDesc.setLayoutParams(descParams);
            row.addView(tvDesc);

            containerPassos.addView(row);
        }
    }

    private void adicionarTextoVazio(LinearLayout container, String mensagem) {
        TextView tv = new TextView(requireContext());
        tv.setText(mensagem);
        tv.setTextSize(14f);
        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.outline));
        container.addView(tv);
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}
