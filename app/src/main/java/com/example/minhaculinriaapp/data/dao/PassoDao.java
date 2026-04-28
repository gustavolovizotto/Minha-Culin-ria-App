package com.example.minhaculinriaapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.minhaculinriaapp.data.entity.Passo;

import java.util.List;

@Dao
public interface PassoDao {

    @Insert
    void inserir(Passo passo);

    @Insert
    void inserirLista(List<Passo> passos);

    @Update
    void atualizar(Passo passo);

    @Delete
    void deletar(Passo passo);

    @Query("SELECT * FROM passos WHERE receita_id = :receitaId ORDER BY numero ASC")
    LiveData<List<Passo>> listarPorReceita(long receitaId);

    @Query("DELETE FROM passos WHERE receita_id = :receitaId")
    void deletarPorReceita(long receitaId);
}
