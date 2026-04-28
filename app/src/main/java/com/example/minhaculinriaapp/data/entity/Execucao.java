package com.example.minhaculinriaapp.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "execucoes",
    foreignKeys = @ForeignKey(
        entity = Receita.class,
        parentColumns = "id",
        childColumns = "receita_id",
        onDelete = ForeignKey.CASCADE
    ),
    indices = @Index("receita_id")
)
public class Execucao {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "receita_id")
    public long receitaId;

    public long data;

    public String nota;

    public String observacoes;

    @ColumnInfo(name = "foto_path")
    public String fotoPath;
}
