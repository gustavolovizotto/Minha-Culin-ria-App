package com.example.minhaculinriaapp.ui.acervo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minhaculinriaapp.R;
import com.example.minhaculinriaapp.viewmodel.AcervoViewModel;

public class AcervoFragment extends Fragment {

    private AcervoViewModel viewModel;
    private CategoriaAdapter categoriaAdapter;
    private ReceitaAdapter receitaAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_acervo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(AcervoViewModel.class);

        // RecyclerView categorias (grid 2 colunas)
        categoriaAdapter = new CategoriaAdapter();
        RecyclerView rvCategorias = view.findViewById(R.id.rv_categorias);
        rvCategorias.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rvCategorias.setAdapter(categoriaAdapter);
        rvCategorias.setHasFixedSize(false);

        // RecyclerView receitas
        receitaAdapter = new ReceitaAdapter();
        RecyclerView rvReceitas = view.findViewById(R.id.rv_receitas);
        rvReceitas.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvReceitas.setAdapter(receitaAdapter);
        rvReceitas.setHasFixedSize(false);

        // Observar dados
        viewModel.categorias.observe(getViewLifecycleOwner(), lista ->
                categoriaAdapter.submitList(lista));

        viewModel.receitas.observe(getViewLifecycleOwner(), lista -> {
            receitaAdapter.submitList(lista);
            view.findViewById(R.id.layout_vazio)
                    .setVisibility(lista == null || lista.isEmpty() ? View.VISIBLE : View.GONE);
        });

        // Clique na receita → tela de detalhes
        receitaAdapter.setOnItemClickListener(receita -> {
            Bundle args = new Bundle();
            args.putLong("receitaId", receita.id);
            Navigation.findNavController(view)
                    .navigate(R.id.action_acervo_to_detalhes_receita, args);
        });

        // Navegação
        view.findViewById(R.id.fab_nova_receita).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_acervo_to_cadastro_receita));

        view.findViewById(R.id.card_nova_categoria).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_acervo_to_cadastro_categoria));
    }
}
