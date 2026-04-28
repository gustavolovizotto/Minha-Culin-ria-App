package com.example.minhaculinriaapp.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.minhaculinriaapp.data.dao.IngredienteDao;
import com.example.minhaculinriaapp.data.dao.PassoDao;
import com.example.minhaculinriaapp.data.dao.ReceitaDao;
import com.example.minhaculinriaapp.data.database.AppDatabase;
import com.example.minhaculinriaapp.data.entity.Ingrediente;
import com.example.minhaculinriaapp.data.entity.Passo;
import com.example.minhaculinriaapp.data.entity.Receita;
import com.example.minhaculinriaapp.data.entity.ReceitaResumida;

import java.util.List;

public class ReceitaRepository {

    private final ReceitaDao receitaDao;
    private final IngredienteDao ingredienteDao;
    private final PassoDao passoDao;

    public ReceitaRepository(Application app) {
        AppDatabase db = AppDatabase.getInstance(app);
        receitaDao = db.receitaDao();
        ingredienteDao = db.ingredienteDao();
        passoDao = db.passoDao();
    }

    public LiveData<List<ReceitaResumida>> listarResumidas() {
        return receitaDao.listarResumidas();
    }

    public void inserirCompleta(Receita receita, List<Ingrediente> ingredientes, List<Passo> passos) {
        new Thread(() -> {
            long id = receitaDao.inserir(receita);
            for (int i = 0; i < ingredientes.size(); i++) {
                ingredientes.get(i).receitaId = id;
                ingredientes.get(i).ordem = i;
            }
            for (Passo p : passos) {
                p.receitaId = id;
            }
            if (!ingredientes.isEmpty()) ingredienteDao.inserirLista(ingredientes);
            if (!passos.isEmpty()) passoDao.inserirLista(passos);
        }).start();
    }

    public void deletar(Receita receita) {
        new Thread(() -> receitaDao.deletar(receita)).start();
    }
}
