package com.alura.pix.consumer;

import com.alura.pix.dto.PixDTO;
import com.alura.pix.dto.PixStatus;
import com.alura.pix.model.Key;
import com.alura.pix.model.Pix;
import com.alura.pix.repository.PixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.alura.pix.repository.KeyRepository;

@Service
public class ValidarPix {

    @Autowired
    private KeyRepository keyRepository;

    @Autowired
    private PixRepository pixRepository;

    @KafkaListener(topics = "pix-topic", groupId = "group-1")
    public void process(PixDTO pixDTO) {
        System.out.println(pixDTO);

        if (pixDTO.getValor() > 0) {
            pixDTO.setStatus(PixStatus.PROCESSADO);
        } else  {
            pixDTO.setStatus(PixStatus.ERRO);
        }

        Key origem = keyRepository.findByChave(pixDTO.getChaveOrigem());
        Key destino = keyRepository.findByChave(pixDTO.getChaveDestino());

        if (origem == null || destino == null) {
            pixDTO.setStatus(PixStatus.ERRO);
        }
        pixRepository.save(Pix.toEntity(pixDTO));
    }

}
