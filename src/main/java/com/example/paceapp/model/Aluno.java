package com.example.paceapp.model;

import java.io.Serializable; // vai permitir que a classe seja gravada em disc cm objeto bin

public class Aluno implements Serializable{
    private static final long serialVersionUID = 1L;

    // aqui é somente p criar os atributos. a validação dos seus tipos é feito na view.
    private String nome;
    private String serie;
    private String dataNasc;
    private String matricula;

    //constutor n pdr
    public Aluno(String nome, String serie, String dataNasc, String matricula){
        this.nome = nome;
        this.serie = serie;
        this.dataNasc = dataNasc;
        this.matricula = matricula;
    }

    //getters e setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
