package com.example.cosasporhacer;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = TareaDB.class)
public class Tarea extends BaseModel {
    public static final String ID = "id";
    public static final String ORDEN = "orden";

    @PrimaryKey(autoincrement = true)
    private long id;
    @Column
    private String descripcion;
    @Column
    private long orden;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getOrden() {
        return orden;
    }

    public void setOrden(long orden) {
        this.orden = orden;
    }

    public Tarea() {
    }

    public Tarea(String descripcion, long orden) {
        this.descripcion = descripcion;
        this.orden = orden;
    }

    public Tarea(long id, String descripcion, long orden) {
        this.id = id;
        this.descripcion = descripcion;
        this.orden = orden;
    }
}
