package com.example.minhaculinriaapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.minhaculinriaapp.data.database.AppDatabase;

public class PerfilViewModel extends AndroidViewModel {

    public final LiveData<Integer> totalReceitas;
    public final LiveData<Integer> totalExecucoes;
    public final LiveData<Integer> totalHorasCozinhando;

    public PerfilViewModel(@NonNull Application app) {
        super(app);
        AppDatabase db = AppDatabase.getInstance(app);
        totalReceitas = db.receitaDao().contarReceitas();
        totalExecucoes = db.execucaoDao().contarExecucoes();
        totalHorasCozinhando = Transformations.map(
                db.receitaDao().somarTempoMinutos(),
                minutos -> minutos != null ? minutos / 60 : 0
        );
    }
}
