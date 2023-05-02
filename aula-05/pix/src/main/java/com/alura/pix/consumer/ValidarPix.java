package com.alura.pix.consumer;

import com.alura.pix.dto.PixDTO;
import com.alura.pix.dto.PixReturnDTO;
import com.alura.pix.dto.PixStatus;
import com.alura.pix.model.Pix;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.generic.GenericData;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ValidarPix {

    @KafkaListener(topics = "postgres_.public.pix", groupId = "group-1")
    public void process(GenericData.Record data) throws JsonProcessingException {
        System.out.println(data.get("after").toString());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        PixReturnDTO dto = objectMapper.readValue(data.get("after").toString(), PixReturnDTO.class);
        System.out.println(dto);
    }

}
