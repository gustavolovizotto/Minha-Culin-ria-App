package com.example.minhaculinriaapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.minhaculinriaapp.data.entity.CategoriaComContagem;
import com.example.minhaculinriaapp.data.entity.ReceitaResumida;
import com.example.minhaculinriaapp.data.repository.CategoriaRepository;
import com.example.minhaculinriaapp.data.repository.ReceitaRepository;

import java.util.List;

public class AcervoViewModel extends AndroidViewModel {

    public final LiveData<List<CategoriaComContagem>> categorias;
    public final LiveData<List<ReceitaResumida>> receitas;

    public AcervoViewModel(@NonNull Application app) {
        super(app);
        categorias = new CategoriaRepository(app).listarComContagem();
        receitas = new ReceitaRepository(app).listarResumidas();
    }
}
