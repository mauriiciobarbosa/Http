package com.example.mauricio.http.model;

import java.io.Serializable;

/**
 * Created by mauricio on 15/11/16.
 */
public class Book implements Serializable {

    public String titulo;
    public String categoria;
    public String autor;
    public int ano;
    public int paginas;
    public String capa;

    public Book() {}

    public Book(String titulo, String categoria, String autor, int ano, int paginas, String capa) {
        this.titulo = titulo;
        this.categoria = categoria;
        this.autor = autor;
        this.ano = ano;
        this.paginas = paginas;
        this.capa = capa;
    }

    @Override
    public String toString() {
        return titulo;
    }
}
