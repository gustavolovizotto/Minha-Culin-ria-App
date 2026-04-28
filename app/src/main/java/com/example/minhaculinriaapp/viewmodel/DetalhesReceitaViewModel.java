package com.example.minhaculinriaapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.minhaculinriaapp.data.dao.IngredienteDao;
import com.example.minhaculinriaapp.data.dao.PassoDao;
import com.example.minhaculinriaapp.data.dao.ReceitaDao;
import com.example.minhaculinriaapp.data.database.AppDatabase;
import com.example.minhaculinriaapp.data.entity.Ingrediente;
import com.example.minhaculinriaapp.data.entity.Passo;
import com.example.minhaculinriaapp.data.entity.ReceitaResumida;

import java.util.List;

public class DetalhesReceitaViewModel extends AndroidViewModel {

    private final MutableLiveData<Long> idLiveData = new MutableLiveData<>();

    public final LiveData<ReceitaResumida> receita =
            Transformations.switchMap(idLiveData, id -> {
                AppDatabase db = AppDatabase.getInstance(getApplication());
                return db.receitaDao().buscarResumidaPorId(id);
            });

    public final LiveData<List<Ingrediente>> ingredientes =
            Transformations.switchMap(idLiveData, id -> {
                AppDatabase db = AppDatabase.getInstance(getApplication());
                return db.ingredienteDao().listarPorReceita(id);
            });

    public final LiveData<List<Passo>> passos =
            Transformations.switchMap(idLiveData, id -> {
                AppDatabase db = AppDatabase.getInstance(getApplication());
                return db.passoDao().listarPorReceita(id);
            });

    public DetalhesReceitaViewModel(@NonNull Application app) {
        super(app);
    }

    public void carregarReceita(long id) {
        idLiveData.setValue(id);
    }
}
