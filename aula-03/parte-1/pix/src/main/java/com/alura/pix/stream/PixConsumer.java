package com.alura.pix.stream;

import com.alura.pix.dto.PixDTO;
import com.alura.pix.dto.PixStatus;
import com.alura.pix.serdes.PixSerdes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PixConsumer {



    private static final Serde<String> STRING_SERDE = Serdes.String();
    private static final Serde<Long> LONG_SERDE = Serdes.Long();


    @Autowired
    public void buildPipeline(StreamsBuilder streamsBuilder) {

        KStream<String, PixDTO> messageStream = streamsBuilder
                .stream("pix-topic", Consumed.with(STRING_SERDE, PixSerdes.serdes()))
                .peek((key, value) -> log.info("Payment event received with key=" + key + ", payment=" + value))
                .filter((key, value) -> value.getStatus().equals(PixStatus.PROCESSADO))
                .peek((key, value) -> log.info("Filtered payment event received with key=" + key + ", value=" + value));

        messageStream.to("pix-out", Produced.with(STRING_SERDE, PixSerdes.serdes()));

        KTable<String, Long> messageStream = streamsBuilder
                .stream("pix-topic", Consumed.with(STRING_SERDE, PixSerdes.serdes()))
                .peek((key, value) -> log.info("Payment event received with key=" + key + ", payment=" + value))
              //  .filter((key, value) -> value.getStatus().equals(PixStatus.PROCESSADO))
                .filter((key, value) -> value.getValor() != null)
                .peek((key, value) -> log.info("Filtered payment event received with key=" + key + ", value=" + value))
                .groupByKey()
                //.count(Materialized.as("count-store"))
                        .aggregate(new Initializer<Long>() {
                                       @Override
                                       public Long apply() {
                                           return 0L;
                                       }
                                   }, new Aggregator<String, PixDTO, Long>() {
                                       @Override
                                       public Long apply(final String key, final PixDTO value,final Long aggregate) {
                                           return (long) (aggregate + value.getValor()) ;
                                       }
                                   }

                        );


        messageStream.toStream().print(Printed.toSysOut());

        messageStream.toStream().to("pix-out", Produced.with(STRING_SERDE, Serdes.Long()));

    }
}
