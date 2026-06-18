package com.example.paceapp.model;

public class Professor extends Pessoa{
    private static final long serialVersionUID = 1L;

    private String cpf;
    private String telefone;
    private String email;
    private String disciplina;

    public Professor(String nome, String cpf, String telefone, String email, String disciplina){
        super(nome);
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.disciplina = disciplina;
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getDisciplina() {
        return disciplina;
    }
    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }
}
