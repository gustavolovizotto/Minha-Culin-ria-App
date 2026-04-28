package com.example.minhaculinriaapp.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.minhaculinriaapp.R;
import com.example.minhaculinriaapp.viewmodel.PerfilViewModel;

public class PerfilFragment extends Fragment {

    private PerfilViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Empurra o top bar para baixo da status bar / câmera frontal
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.top_bar_perfil), (v, insets) -> {
            Insets statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(v.getPaddingLeft(), statusBar.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        TextView tvReceitas = view.findViewById(R.id.tv_total_receitas);
        TextView tvExecucoes = view.findViewById(R.id.tv_total_execucoes);
        TextView tvHoras = view.findViewById(R.id.tv_total_horas);
        SwitchCompat switchVoz = view.findViewById(R.id.switch_voz);

        viewModel.totalReceitas.observe(getViewLifecycleOwner(), n ->
                tvReceitas.setText(n != null ? String.valueOf(n) : "0"));

        viewModel.totalExecucoes.observe(getViewLifecycleOwner(), n ->
                tvExecucoes.setText(n != null ? String.valueOf(n) : "0"));

        viewModel.totalHorasCozinhando.observe(getViewLifecycleOwner(), h ->
                tvHoras.setText((h != null ? String.valueOf(h) : "0") + "h"));

        view.findViewById(R.id.row_timer).setOnClickListener(v ->
                Toast.makeText(requireContext(), "Notificações de Timer em breve", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.row_notificacoes).setOnClickListener(v ->
                Toast.makeText(requireContext(), "Notificações em breve", Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.row_privacidade).setOnClickListener(v ->
                Toast.makeText(requireContext(), "Privacidade e Segurança em breve", Toast.LENGTH_SHORT).show());
    }
}
