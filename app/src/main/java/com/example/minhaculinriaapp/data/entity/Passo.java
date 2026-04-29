package com.example.minhaculinriaapp.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "passos",
    foreignKeys = @ForeignKey(
        entity = Receita.class,
        parentColumns = "id",
        childColumns = "receita_id",
        onDelete = ForeignKey.CASCADE
    ),
    indices = @Index("receita_id")
)
public class Passo {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "receita_id")
    public long receitaId;

    public int numero;

    public String descricao;

    @ColumnInfo(name = "tempo_minutos")
    public Integer tempoMinutos;
}
