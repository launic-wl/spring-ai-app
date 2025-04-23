package com.laurentiu.spring_ai_app.controller;

import com.laurentiu.spring_ai_app.exception.ControllerException;
import com.laurentiu.spring_ai_app.exception.ErrorCode;
import com.laurentiu.spring_ai_app.service.consumer.ConsumerForKafka;
import com.laurentiu.spring_ai_app.service.producer.ProducerForKafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// curl -X POST "http://localhost:8085/api/kmc/sendMessage" -d "message=What is Hello World?"

@RestController
@RequestMapping("/api/kmc")
public class KafkaMsgController {

    private final ProducerForKafka producerForKafka;
    private final ConsumerForKafka consumerForKafka;

    // constructor bean injection
    public KafkaMsgController(ProducerForKafka producerForKafka, ConsumerForKafka consumerForKafka) {
        this.producerForKafka = producerForKafka;
        this.consumerForKafka = consumerForKafka;
    }


    @PostMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@RequestParam String message) {

        try {
            producerForKafka.sendMessageToTopic(message);

            String response = consumerForKafka.getResponseFuture().get();

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e) {
            ControllerException controllerException = new ControllerException(ErrorCode.KMC_BAD_REQUEST, e);
            return new ResponseEntity<>(controllerException, HttpStatus.BAD_REQUEST);
        }
    }

}
