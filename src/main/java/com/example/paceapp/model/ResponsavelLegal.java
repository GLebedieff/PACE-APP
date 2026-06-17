package com.example.paceapp.model;

public class ResponsavelLegal extends Pessoa{
    private String cpf;
    private String telefone;
    private String parentesco;
    private String dataNascimento;
    public ResponsavelLegal(String nome, String cpf, String telefone, String parentesco, String dataNascimento){
        super(nome);
        this.cpf = cpf;
        this.telefone = telefone;
        this.parentesco = parentesco;
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public String getParentesco() {
        return parentesco;
    }
    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }
    public String getDataNascimento() {
        return dataNascimento;
    }
    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
