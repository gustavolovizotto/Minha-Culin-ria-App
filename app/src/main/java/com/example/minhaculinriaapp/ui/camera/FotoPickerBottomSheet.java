package com.example.minhaculinriaapp.ui.camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.minhaculinriaapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FotoPickerBottomSheet extends BottomSheetDialogFragment {

    public interface Listener {
        void onEscolherCamera();
        void onEscolherGaleria();
    }

    private Listener listener;

    public static FotoPickerBottomSheet newInstance() {
        return new FotoPickerBottomSheet();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_foto_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_camera).setOnClickListener(v -> {
            dismiss();
            if (listener != null) listener.onEscolherCamera();
        });

        view.findViewById(R.id.btn_galeria).setOnClickListener(v -> {
            dismiss();
            if (listener != null) listener.onEscolherGaleria();
        });
    }
}
