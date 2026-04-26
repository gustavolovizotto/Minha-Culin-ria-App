package com.example.minhaculinriaapp.ui.acervo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.minhaculinriaapp.R;

public class AcervoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_acervo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.fab_nova_receita).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_acervo_to_cadastro_receita));

        view.findViewById(R.id.card_nova_categoria).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_acervo_to_cadastro_categoria));
    }
}
