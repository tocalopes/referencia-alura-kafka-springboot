package com.alura.pix.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import  com.alura.pix.dto.PixDTO;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Pix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String chaveOrigem;
    private String chaveDestino;
    private Double valor;
    private LocalDateTime dataTransferencia;
    private String status;

    public static Pix toEntity(PixDTO pixDTO) {
        Pix pix = new Pix();
        pix.setChaveDestino(pixDTO.getChaveDestino());
        pix.setStatus(pixDTO.getStatus().toString());
        pix.setValor(pixDTO.getValor());
        pix.setDataTransferencia(pixDTO.getDataTransferencia());
        pix.setChaveOrigem(pixDTO.getChaveOrigem());
        return pix;
    }
}
