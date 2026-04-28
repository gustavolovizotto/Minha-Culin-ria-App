package com.example.minhaculinriaapp.ui.cadastro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.minhaculinriaapp.R;
import com.example.minhaculinriaapp.data.entity.Ingrediente;
import com.example.minhaculinriaapp.data.entity.Passo;
import com.example.minhaculinriaapp.viewmodel.CadastroReceitaViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class CadastroReceitaStep2Fragment extends Fragment {

    private CadastroReceitaViewModel viewModel;
    private LinearLayout containerIngredientes;
    private LinearLayout containerPassos;
    private int passoCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cadastro_receita_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CadastroReceitaViewModel.class);

        containerIngredientes = view.findViewById(R.id.container_ingredientes);
        containerPassos = view.findViewById(R.id.container_passos);

        // Iniciar com 3 linhas de ingredientes e 2 passos
        addIngredienteRow();
        addIngredienteRow();
        addIngredienteRow();
        addPassoRow();
        addPassoRow();

        view.findViewById(R.id.btn_add_ingrediente).setOnClickListener(v -> addIngredienteRow());
        view.findViewById(R.id.btn_add_passo).setOnClickListener(v -> addPassoRow());

        view.findViewById(R.id.btn_back).setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        view.findViewById(R.id.btn_voltar).setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        view.findViewById(R.id.btn_proximo).setOnClickListener(v -> {
            coletarIngredientes();
            coletarPassos();
            Navigation.findNavController(v).navigate(R.id.action_step2_to_step3);
        });
    }

    private void addIngredienteRow() {
        View row = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_ingrediente, containerIngredientes, false);
        containerIngredientes.addView(row);
    }

    private void addPassoRow() {
        passoCount++;
        View row = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_passo, containerPassos, false);
        ((TextView) row.findViewById(R.id.tv_numero_passo)).setText(String.valueOf(passoCount));
        containerPassos.addView(row);
    }

    private void coletarIngredientes() {
        List<Ingrediente> lista = new ArrayList<>();
        for (int i = 0; i < containerIngredientes.getChildCount(); i++) {
            View row = containerIngredientes.getChildAt(i);
            TextInputEditText etQtd = row.findViewById(R.id.et_quantidade);
            TextInputEditText etNome = row.findViewById(R.id.et_nome_ingrediente);
            String nome = etNome.getText() != null ? etNome.getText().toString().trim() : "";
            if (!nome.isEmpty()) {
                Ingrediente ing = new Ingrediente();
                ing.quantidade = etQtd.getText() != null ? etQtd.getText().toString().trim() : "";
                ing.nome = nome;
                ing.ordem = i;
                lista.add(ing);
            }
        }
        viewModel.ingredientes.clear();
        viewModel.ingredientes.addAll(lista);
    }

    private void coletarPassos() {
        List<Passo> lista = new ArrayList<>();
        for (int i = 0; i < containerPassos.getChildCount(); i++) {
            View row = containerPassos.getChildAt(i);
            TextInputEditText etDesc = row.findViewById(R.id.et_descricao_passo);
            String desc = etDesc.getText() != null ? etDesc.getText().toString().trim() : "";
            if (!desc.isEmpty()) {
                Passo p = new Passo();
                p.numero = i + 1;
                p.descricao = desc;
                lista.add(p);
            }
        }
        viewModel.passos.clear();
        viewModel.passos.addAll(lista);
    }
}
