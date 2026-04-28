package com.example.minhaculinriaapp.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "receitas",
    foreignKeys = @ForeignKey(
        entity = Categoria.class,
        parentColumns = "id",
        childColumns = "categoria_id",
        onDelete = ForeignKey.SET_NULL
    ),
    indices = @Index("categoria_id")
)
public class Receita {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "categoria_id")
    public Long categoriaId;

    @NonNull
    public String nome = "";

    public String descricao;

    @ColumnInfo(name = "foto_path")
    public String fotoPath;

    public Integer rendimento;

    @ColumnInfo(name = "tempo_minutos")
    public Integer tempoMinutos;

    public String dificuldade;

    public String tags;

    public String notas;

    @ColumnInfo(name = "criado_em")
    public long criadoEm;
}
