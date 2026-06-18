package com.example.paceapp.model;

import java.io.Serializable;

public class Turma implements Serializable{
    private static final long serialVersionUID = 1L;

    private String nomeTurma; //Chave identificadora unica
    private String serie;
    private int qtdAlunos;
    private Instituicao instituicao; // associação com Instituição

    public Turma(String nomeTurma, String serie, int qtdAlunos, Instituicao instituicao) {
        this.nomeTurma = nomeTurma;
        this.serie = serie;
        this.qtdAlunos = qtdAlunos;
        this.instituicao = instituicao;
    }

    public String getNomeTurma() {
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public int getQtdAlunos() {
        return qtdAlunos;
    }

    public void setQtdAlunos(int qtdAlunos) {
        this.qtdAlunos = qtdAlunos;
    }

    public Instituicao getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(Instituicao instituicao) {
        this.instituicao = instituicao;
    }
}
