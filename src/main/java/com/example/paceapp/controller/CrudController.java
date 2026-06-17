package com.example.paceapp.controller;

import com.example.paceapp.model.Persistencia;

import java.util.ArrayList;

public class CrudController<T> {
    private final Persistencia<T> persistencia;

    public CrudController(Persistencia<T> persistencia){
        this.persistencia = persistencia;
    }

    public ArrayList<T> listarTodos(){
        return persistencia.lerLista();
    }

    public void adicionar(T item){
        persistencia.adicionar(item);
    }

    public void atualizar(String chaveAntiga, T itemAtualizado){
        persistencia.atualizar(chaveAntiga, itemAtualizado);
    }

    public void excluir(String chave){
        persistencia.excluir(chave);
    }
}
