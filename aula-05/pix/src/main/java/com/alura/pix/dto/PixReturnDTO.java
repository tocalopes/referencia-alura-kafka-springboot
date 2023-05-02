package com.alura.pix.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class PixReturnDTO {
    private Integer id;
    @JsonProperty("chave_origem")
    private String chaveOrigem;
    @JsonProperty("chave_destino")
    private String chaveDestino;
    private Double valor;
    @JsonProperty("data_transferencia")
    private Date dataTransferencia;
    private String status;
}
