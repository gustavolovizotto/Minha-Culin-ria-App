package com.example.minhaculinriaapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.minhaculinriaapp.data.entity.Ingrediente;

import java.util.List;

@Dao
public interface IngredienteDao {

    @Insert
    void inserir(Ingrediente ingrediente);

    @Insert
    void inserirLista(List<Ingrediente> ingredientes);

    @Update
    void atualizar(Ingrediente ingrediente);

    @Delete
    void deletar(Ingrediente ingrediente);

    @Query("SELECT * FROM ingredientes WHERE receita_id = :receitaId ORDER BY ordem ASC")
    LiveData<List<Ingrediente>> listarPorReceita(long receitaId);

    @Query("DELETE FROM ingredientes WHERE receita_id = :receitaId")
    void deletarPorReceita(long receitaId);
}
