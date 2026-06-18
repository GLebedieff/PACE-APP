package com.example.paceapp.model;

import java.io.*; // importa tudo de java.io
import java.util.ArrayList;

public class ArquivoProfissional implements Persistencia<Profissional> { // é na interface de persistencia q tem o ler,alterar,salvarl exlcuir e pa

    //declaração d constante (CAMINHO_ARQUIVO) q vai guardar o nome do arquivo
    private static final String CAMINHO_ARQUIVO = "profissionals.dat";

    @Override
    public void salvarLista(ArrayList<Profissional> lista) {
        try {
            File arq = new File(CAMINHO_ARQUIVO); // Cria um arquivo
            if (!arq.exists()) { // se o arquivo não existir
                arq.createNewFile();
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arq))) {
                /* File... define o destino (vai falar qual arquivo vai ser usado p salvar a lista)
                 *   Object.... salva e le  dados sallvos                                                                                    * */

                oos.writeObject(lista); // converte o estado do objeto
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar lista de profissionals: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Profissional> lerLista(){
        // começa uma lista vazia
        ArrayList<Profissional> lista = new ArrayList<>();
        File arq = new File(CAMINHO_ARQUIVO);

        if(arq.exists()){ // se houver arquivo ele vai ler o objeto q vai retornar o obj.
            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arq))){
                lista = (ArrayList<Profissional>) ois.readObject();
            }catch (IOException | ClassNotFoundException e){ // se der erro na classe ele retorna a lista vazia
                System.err.println("Erro ao ler lista de instituições: " + e.getMessage());
            }
        }
        return lista;
    }

    @Override
    public void adicionar (Profissional nova){
        ArrayList<Profissional> lista = lerLista();
        lista.add(nova);
        salvarLista(lista);
    }

    @Override
    public void atualizar (String CrmAntiga, Profissional atualizado){
        ArrayList<Profissional> lista = lerLista();
        //quando ele encontrar uma posição em q o cnpj ele substitui o item dql posição
        for (int i = 0 ; i<lista.size(); i++){
            if (lista.get(i).getCrm().equals(CrmAntiga)){
                lista.set(i, atualizado); // substituição
                break;
            }
        }
        salvarLista(lista);
    }

    @Override
    public void excluir (String Crm){
        ArrayList<Profissional> lista = lerLista();
        lista.removeIf(profissional -> profissional.getCrm().equals(Crm));
        salvarLista(lista);
    }
}

