package com.example.minhaculinriaapp.ui.cadastro;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.minhaculinriaapp.R;
import com.example.minhaculinriaapp.data.entity.Categoria;
import com.example.minhaculinriaapp.viewmodel.CadastroReceitaViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class CadastroReceitaStep1Fragment extends Fragment {

    private CadastroReceitaViewModel viewModel;
    private TextInputLayout tilNome;
    private TextInputEditText etNome, etDescricao;
    private LinearLayout chipsContainer;
    private Long categoriaIdSelecionada = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cadastro_receita_1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ViewModel compartilhado com os outros passos (escopo Activity)
        viewModel = new ViewModelProvider(requireActivity()).get(CadastroReceitaViewModel.class);

        tilNome = view.findViewById(R.id.til_nome);
        etNome = view.findViewById(R.id.et_nome);
        etDescricao = view.findViewById(R.id.et_descricao);
        chipsContainer = view.findViewById(R.id.chips_categorias);

        // Restaurar dados do ViewModel se o usuário voltou
        if (viewModel.nome != null) etNome.setText(viewModel.nome);
        if (viewModel.descricao != null) etDescricao.setText(viewModel.descricao);
        categoriaIdSelecionada = viewModel.categoriaId;

        // Carregar chips de categorias do banco
        viewModel.categorias.observe(getViewLifecycleOwner(), this::popularChipsCategorias);

        // "+ Nova" chip navega para cadastro de categoria
        view.findViewById(R.id.chip_nova_categoria).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_step1_to_cadastro_categoria));

        view.findViewById(R.id.btn_back).setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        view.findViewById(R.id.btn_proximo).setOnClickListener(v -> {
            String nome = etNome.getText() != null ? etNome.getText().toString().trim() : "";
            if (TextUtils.isEmpty(nome)) {
                tilNome.setError("Nome é obrigatório");
                return;
            }
            tilNome.setError(null);
            viewModel.nome = nome;
            viewModel.descricao = etDescricao.getText() != null
                    ? etDescricao.getText().toString().trim() : null;
            viewModel.categoriaId = categoriaIdSelecionada;
            Navigation.findNavController(v).navigate(R.id.action_step1_to_step2);
        });
    }

    private void popularChipsCategorias(List<Categoria> categorias) {
        // Remove chips dinâmicos (mantém o chip "+ Nova" que é o último filho fixo do XML)
        // O container_chips já tem os chips estáticos do XML, então limpamos e reconstruímos
        // apenas os chips de categorias (antes do chip "+ Nova")
        View chipNova = chipsContainer.findViewWithTag("chip_nova");
        if (chipNova == null) {
            // Primeira vez: marcar o chip "+ Nova" com tag e remover os chips estáticos do XML
            // que foram colocados apenas como placeholder visual
            chipsContainer.removeAllViews();
            chipNova = requireView().findViewById(R.id.chip_nova_categoria);
        } else {
            // Remover todos exceto o chip "+ Nova"
            for (int i = chipsContainer.getChildCount() - 2; i >= 0; i--) {
                chipsContainer.removeViewAt(i);
            }
        }

        if (categorias != null) {
            for (Categoria cat : categorias) {
                TextView chip = (TextView) LayoutInflater.from(requireContext())
                        .inflate(R.layout.item_tag_chip, chipsContainer, false);
                chip.setText(cat.nome);
                boolean selecionada = cat.id == (categoriaIdSelecionada != null ? categoriaIdSelecionada : -1);
                chip.setTag(selecionada);
                applyChipStyle(chip, selecionada);
                chip.setOnClickListener(v -> selecionarCategoria(chip, cat.id, categorias.size()));
                chipsContainer.addView(chip, chipsContainer.getChildCount());
            }
        }
    }

    private void selecionarCategoria(TextView chipClicado, long catId, int total) {
        boolean jaEstavaSelecionada = categoriaIdSelecionada != null && categoriaIdSelecionada == catId;
        if (jaEstavaSelecionada) {
            categoriaIdSelecionada = null;
            applyChipStyle(chipClicado, false);
            chipClicado.setTag(false);
        } else {
            categoriaIdSelecionada = catId;
            // Desselecionar todos e selecionar o clicado
            for (int i = 0; i < chipsContainer.getChildCount() - 1; i++) {
                View child = chipsContainer.getChildAt(i);
                if (child instanceof TextView) {
                    applyChipStyle((TextView) child, false);
                    child.setTag(false);
                }
            }
            applyChipStyle(chipClicado, true);
            chipClicado.setTag(true);
        }
    }

    private void applyChipStyle(TextView chip, boolean selecionada) {
        if (selecionada) {
            chip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chip_category_selected));
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.on_primary));
        } else {
            chip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chip_category));
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_variant));
        }
    }
}
