package com.example.minhaculinriaapp.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.minhaculinriaapp.data.dao.CategoriaDao;
import com.example.minhaculinriaapp.data.database.AppDatabase;
import com.example.minhaculinriaapp.data.entity.Categoria;
import com.example.minhaculinriaapp.data.entity.CategoriaComContagem;

import java.util.List;

public class CategoriaRepository {

    private final CategoriaDao dao;

    public CategoriaRepository(Application app) {
        dao = AppDatabase.getInstance(app).categoriaDao();
    }

    public LiveData<List<Categoria>> listarTodas() {
        return dao.listarTodas();
    }

    public LiveData<List<CategoriaComContagem>> listarComContagem() {
        return dao.listarComContagem();
    }

    public void inserir(Categoria categoria) {
        new Thread(() -> dao.inserir(categoria)).start();
    }

    public void atualizar(Categoria categoria) {
        new Thread(() -> dao.atualizar(categoria)).start();
    }

    public void deletar(Categoria categoria) {
        new Thread(() -> dao.deletar(categoria)).start();
    }
}
