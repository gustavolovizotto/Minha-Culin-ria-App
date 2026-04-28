package com.example.minhaculinriaapp.ui.execucao;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.minhaculinriaapp.R;
import com.example.minhaculinriaapp.data.entity.Ingrediente;
import com.example.minhaculinriaapp.data.entity.Passo;
import com.example.minhaculinriaapp.viewmodel.MaosNaMassaViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MaosNaMassaFragment extends Fragment {

    private MaosNaMassaViewModel viewModel;

    private View layoutVozChip, cardPasso;
    private TextView tvNomeReceita, tvPassoNumero, tvPassoTotal, tvBadge;
    private TextView tvTitulo, tvDescricao, tvSemPassos;
    private ProgressBar progressBar;
    private View dividerChips;
    private HorizontalScrollView scrollChips;
    private LinearLayout containerChips;
    private Button btnAnterior, btnProximo;

    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private boolean micPermissionGranted = false;

    private List<Passo> listaPassos = new ArrayList<>();
    private List<Ingrediente> listaIngredientes = new ArrayList<>();

    private final ActivityResultLauncher<String> requestMicPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                micPermissionGranted = granted;
                if (granted) {
                    initSpeechRecognizer();
                } else {
                    layoutVozChip.setVisibility(View.GONE);
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maos_massa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutVozChip = view.findViewById(R.id.layout_voz_chip);
        cardPasso = view.findViewById(R.id.card_passo);
        tvNomeReceita = view.findViewById(R.id.tv_nome_receita);
        tvPassoNumero = view.findViewById(R.id.tv_passo_numero);
        tvPassoTotal = view.findViewById(R.id.tv_passo_total);
        tvBadge = view.findViewById(R.id.tv_badge_numero);
        tvTitulo = view.findViewById(R.id.tv_titulo_passo);
        tvDescricao = view.findViewById(R.id.tv_descricao_passo);
        tvSemPassos = view.findViewById(R.id.tv_sem_passos);
        progressBar = view.findViewById(R.id.progress_passos);
        dividerChips = view.findViewById(R.id.divider_chips);
        scrollChips = view.findViewById(R.id.scroll_chips);
        containerChips = view.findViewById(R.id.container_chips);
        btnAnterior = view.findViewById(R.id.btn_anterior);
        btnProximo = view.findViewById(R.id.btn_proximo);

        viewModel = new ViewModelProvider(this).get(MaosNaMassaViewModel.class);

        long receitaId = getArguments() != null ? getArguments().getLong("receitaId", -1) : -1;
        if (receitaId != -1) {
            viewModel.carregarReceita(receitaId);
        }

        viewModel.receita.observe(getViewLifecycleOwner(), r -> {
            if (r != null) tvNomeReceita.setText(r.nome.toUpperCase(Locale.getDefault()));
        });

        viewModel.passos.observe(getViewLifecycleOwner(), passos -> {
            listaPassos = passos != null ? passos : new ArrayList<>();
            atualizarUI();
        });

        viewModel.ingredientes.observe(getViewLifecycleOwner(), ing -> {
            listaIngredientes = ing != null ? ing : new ArrayList<>();
            renderIngredientChips();
        });

        viewModel.getIndice().observe(getViewLifecycleOwner(), idx -> atualizarUI());

        view.findViewById(R.id.btn_back_massa).setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        btnAnterior.setOnClickListener(v -> viewModel.voltar());
        btnProximo.setOnClickListener(v -> viewModel.avancar());

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            micPermissionGranted = true;
            initSpeechRecognizer();
        } else {
            requestMicPermission.launch(Manifest.permission.RECORD_AUDIO);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onStop() {
        super.onStop();
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destroySpeechRecognizer();
    }

    private void atualizarUI() {
        if (listaPassos.isEmpty()) {
            tvSemPassos.setVisibility(View.VISIBLE);
            tvBadge.setVisibility(View.GONE);
            cardPasso.setVisibility(View.GONE);
            btnAnterior.setEnabled(false);
            btnProximo.setEnabled(false);
            return;
        }

        tvSemPassos.setVisibility(View.GONE);
        tvBadge.setVisibility(View.VISIBLE);
        cardPasso.setVisibility(View.VISIBLE);

        int idx = viewModel.getIndice().getValue() != null ? viewModel.getIndice().getValue() : 0;
        idx = Math.max(0, Math.min(idx, listaPassos.size() - 1));
        Passo passo = listaPassos.get(idx);
        int total = listaPassos.size();

        tvBadge.setText(String.valueOf(passo.numero));
        tvTitulo.setText("Passo " + passo.numero);
        tvDescricao.setText(passo.descricao != null ? passo.descricao : "");

        tvPassoNumero.setText("Passo " + (idx + 1));
        tvPassoTotal.setText(" / " + total);
        int progress = total > 1 ? (int) (((float) (idx + 1) / total) * 100) : 100;
        progressBar.setProgress(progress);

        btnAnterior.setEnabled(idx > 0);
        btnProximo.setEnabled(idx < total - 1);
    }

    private void renderIngredientChips() {
        containerChips.removeAllViews();
        if (listaIngredientes.isEmpty()) {
            dividerChips.setVisibility(View.GONE);
            scrollChips.setVisibility(View.GONE);
            return;
        }

        dividerChips.setVisibility(View.VISIBLE);
        scrollChips.setVisibility(View.VISIBLE);

        for (Ingrediente ing : listaIngredientes) {
            StringBuilder label = new StringBuilder();
            if (ing.quantidade != null && !ing.quantidade.isEmpty()) {
                label.append(ing.quantidade).append("  ");
            }
            label.append(ing.nome != null ? ing.nome.toUpperCase(Locale.getDefault()) : "");

            TextView chip = new TextView(requireContext());
            chip.setText(label.toString());
            chip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_diario_chip));
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.on_secondary_container));
            chip.setTextSize(11f);
            chip.setTypeface(null, android.graphics.Typeface.BOLD);
            chip.setPadding(dpToPx(10), dpToPx(4), dpToPx(10), dpToPx(4));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = dpToPx(8);
            chip.setLayoutParams(params);

            containerChips.addView(chip);
        }
    }

    private void initSpeechRecognizer() {
        if (!SpeechRecognizer.isRecognitionAvailable(requireContext())) {
            layoutVozChip.setVisibility(View.GONE);
            return;
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext());
        speechRecognizer.setRecognitionListener(recognitionListener);

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        speechRecognizer.startListening(recognizerIntent);
    }

    private void restartListening() {
        if (speechRecognizer != null && micPermissionGranted) {
            speechRecognizer.cancel();
            speechRecognizer.startListening(recognizerIntent);
        }
    }

    private void destroySpeechRecognizer() {
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
            speechRecognizer.cancel();
            speechRecognizer.destroy();
            speechRecognizer = null;
        }
    }

    private final RecognitionListener recognitionListener = new RecognitionListener() {
        @Override public void onReadyForSpeech(Bundle params) {}
        @Override public void onBeginningOfSpeech() {}
        @Override public void onRmsChanged(float rmsdB) {}
        @Override public void onBufferReceived(byte[] buffer) {}
        @Override public void onEndOfSpeech() {}
        @Override public void onPartialResults(Bundle partialResults) {}
        @Override public void onEvent(int eventType, Bundle params) {}

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (matches != null) {
                for (String match : matches) {
                    String lower = match.toLowerCase(Locale.getDefault());
                    if (lower.contains("próximo") || lower.contains("proximo")
                            || lower.contains("avança") || lower.contains("avancar")) {
                        viewModel.avancar();
                        break;
                    }
                    if (lower.contains("voltar") || lower.contains("anterior")
                            || lower.contains("volta")) {
                        viewModel.voltar();
                        break;
                    }
                }
            }
            restartListening();
        }

        @Override
        public void onError(int error) {
            if (error == SpeechRecognizer.ERROR_NO_MATCH
                    || error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT
                    || error == SpeechRecognizer.ERROR_RECOGNIZER_BUSY) {
                restartListening();
            } else {
                layoutVozChip.setVisibility(View.GONE);
            }
        }
    };

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}
