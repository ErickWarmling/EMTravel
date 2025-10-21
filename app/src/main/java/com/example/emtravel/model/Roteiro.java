package com.example.emtravel.model;

import java.io.Serializable;
import java.util.List;

/**
 * Modelo que representa um Roteiro de viagem.
 * Contém informações básicas e a lista de destinos selecionados.
 */
public class Roteiro implements Serializable {

    private String uid;
    private String nome;
    private String dataInicio;
    private String dataFim;
    private List<Destino> destinos;
    private String imagemUrl;

    public Roteiro() {
    }

    public Roteiro(String uid, String nome, String dataInicio, String dataFim, List<Destino> destinos, String imagemUrl) {
        this.uid = uid;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.destinos = destinos;
        this.imagemUrl = imagemUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public List<Destino> getDestinos() {
        return destinos;
    }

    public void setDestinos(List<Destino> destinos) {
        this.destinos = destinos;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }
}
