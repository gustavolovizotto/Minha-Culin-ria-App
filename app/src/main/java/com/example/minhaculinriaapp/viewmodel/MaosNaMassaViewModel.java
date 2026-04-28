package com.example.minhaculinriaapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.minhaculinriaapp.data.database.AppDatabase;
import com.example.minhaculinriaapp.data.entity.Ingrediente;
import com.example.minhaculinriaapp.data.entity.Passo;
import com.example.minhaculinriaapp.data.entity.ReceitaResumida;

import java.util.List;

public class MaosNaMassaViewModel extends AndroidViewModel {

    private final MutableLiveData<Long> idLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> indice = new MutableLiveData<>(0);

    public final LiveData<ReceitaResumida> receita =
            Transformations.switchMap(idLiveData, id ->
                    AppDatabase.getInstance(getApplication()).receitaDao().buscarResumidaPorId(id));

    public final LiveData<List<Passo>> passos =
            Transformations.switchMap(idLiveData, id ->
                    AppDatabase.getInstance(getApplication()).passoDao().listarPorReceita(id));

    public final LiveData<List<Ingrediente>> ingredientes =
            Transformations.switchMap(idLiveData, id ->
                    AppDatabase.getInstance(getApplication()).ingredienteDao().listarPorReceita(id));

    public MaosNaMassaViewModel(@NonNull Application app) {
        super(app);
    }

    public void carregarReceita(long id) {
        idLiveData.setValue(id);
    }

    public LiveData<Integer> getIndice() {
        return indice;
    }

    public void avancar() {
        List<Passo> lista = passos.getValue();
        if (lista == null) return;
        int atual = indice.getValue() != null ? indice.getValue() : 0;
        if (atual < lista.size() - 1) indice.setValue(atual + 1);
    }

    public void voltar() {
        int atual = indice.getValue() != null ? indice.getValue() : 0;
        if (atual > 0) indice.setValue(atual - 1);
    }
}
