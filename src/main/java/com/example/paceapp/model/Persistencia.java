package com.example.paceapp.model;

import java.util.ArrayList;

public interface Persistencia<T> {
    ArrayList<T> lerLista();
    void salvarLista(ArrayList<T> lista);
    void adicionar(T item);
    void atualizar(String chaveAntiga, T itemAtualizado);
    void excluir(String chave);
}
