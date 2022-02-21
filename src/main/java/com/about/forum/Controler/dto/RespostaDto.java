package com.about.forum.Controler.dto;

import com.about.forum.Model.Resposta;
import com.about.forum.Model.StatusTopico;

import java.time.LocalDateTime;

public class RespostaDto {

    private  Long id;
    private  String mensagem;
    private LocalDateTime dataCriacao;
    private String nomeAutor;

    public RespostaDto (Resposta resposta){
        this.id = resposta.getId();
        this.mensagem = resposta.getMensagem();
        this.dataCriacao = resposta.getDataCriacao();
        this.nomeAutor = resposta.getAutor().getNome();
    }

    public Long getId() {
        return id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public String getNomeAutor() {
        return nomeAutor;
    }
}
