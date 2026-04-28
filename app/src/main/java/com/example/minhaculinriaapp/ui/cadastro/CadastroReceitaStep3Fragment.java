package com.example.minhaculinriaapp.ui.cadastro;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.minhaculinriaapp.R;
import com.example.minhaculinriaapp.viewmodel.CadastroReceitaViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class CadastroReceitaStep3Fragment extends Fragment {

    private CadastroReceitaViewModel viewModel;
    private LinearLayout containerTags;
    private TextInputEditText etNovaTag;
    private TextInputEditText etRendimento, etTempo, etNotas;
    private RadioGroup rgDificuldade;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cadastro_receita_3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CadastroReceitaViewModel.class);

        containerTags = view.findViewById(R.id.container_tags);
        etNovaTag = view.findViewById(R.id.et_nova_tag);
        etRendimento = view.findViewById(R.id.et_rendimento);
        etTempo = view.findViewById(R.id.et_tempo);
        etNotas = view.findViewById(R.id.et_notas);
        rgDificuldade = view.findViewById(R.id.rg_dificuldade);

        // Tags de sugestão
        addTagChip("sem glúten", false);
        addTagChip("rápido", false);
        addTagChip("vegano", false);
        addTagChip("sem lactose", false);

        view.findViewById(R.id.btn_add_tag).setOnClickListener(v -> commitTag());

        etNovaTag.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                commitTag();
                return true;
            }
            return false;
        });

        view.findViewById(R.id.btn_back).setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        view.findViewById(R.id.btn_voltar).setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        view.findViewById(R.id.btn_salvar).setOnClickListener(v -> salvarReceita(v));
    }

    private void salvarReceita(View v) {
        // Coletar dados do Step 3
        String rendStr = etRendimento.getText() != null ? etRendimento.getText().toString().trim() : "";
        String tempoStr = etTempo.getText() != null ? etTempo.getText().toString().trim() : "";

        viewModel.rendimento = rendStr.isEmpty() ? null : Integer.parseInt(rendStr);
        viewModel.tempoMinutos = tempoStr.isEmpty() ? null : Integer.parseInt(tempoStr);

        int rdId = rgDificuldade.getCheckedRadioButtonId();
        if (rdId == R.id.rb_facil) viewModel.dificuldade = "Fácil";
        else if (rdId == R.id.rb_dificil) viewModel.dificuldade = "Difícil";
        else viewModel.dificuldade = "Médio";

        viewModel.tags = coletarTagsSelecionadas();
        viewModel.notas = etNotas.getText() != null ? etNotas.getText().toString().trim() : null;

        viewModel.salvar();
        viewModel.limpar();

        Navigation.findNavController(v).navigate(R.id.action_step3_to_acervo);
    }

    private String coletarTagsSelecionadas() {
        List<String> selecionadas = new ArrayList<>();
        for (int i = 0; i < containerTags.getChildCount(); i++) {
            TextView chip = (TextView) containerTags.getChildAt(i);
            if (chip.getTag() != null && (boolean) chip.getTag()) {
                selecionadas.add(chip.getText().toString());
            }
        }
        return selecionadas.isEmpty() ? null : TextUtils.join(", ", selecionadas);
    }

    private void commitTag() {
        String texto = etNovaTag.getText() != null ? etNovaTag.getText().toString().trim() : "";
        if (!TextUtils.isEmpty(texto)) {
            addTagChip(texto, true);
            etNovaTag.setText("");
        }
    }

    private void addTagChip(String texto, boolean selecionada) {
        TextView chip = (TextView) LayoutInflater.from(requireContext())
                .inflate(R.layout.item_tag_chip, containerTags, false);
        chip.setText(texto);
        chip.setTag(selecionada);
        applyChipStyle(chip, selecionada);
        chip.setOnClickListener(v -> {
            boolean estado = chip.getTag() != null && (boolean) chip.getTag();
            chip.setTag(!estado);
            applyChipStyle(chip, !estado);
        });
        containerTags.addView(chip);
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
