package com.example.minhaculinriaapp.data.entity;

import androidx.room.ColumnInfo;

public class ReceitaResumida {
    public long id;
    public String nome;
    public String descricao;

    @ColumnInfo(name = "foto_path")
    public String fotoPath;

    public Integer rendimento;

    @ColumnInfo(name = "tempo_minutos")
    public Integer tempoMinutos;

    public String dificuldade;

    public String tags;

    @ColumnInfo(name = "criado_em")
    public long criadoEm;

    @ColumnInfo(name = "categoria_nome")
    public String categoriaNome;
}
