package com.quest.etna.controller;

import com.quest.etna.model.Message;
import com.quest.etna.repositories.MessageRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;
    @MessageMapping("/chat")
    public Message send(@Payload Message message) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        Message toSave = new Message(message.getFromHost(), message.getText(), message.getAddress());
        System.out.println(message.getText());
        messageRepository.save(toSave);
        return toSave;
    }

    @CrossOrigin
    @GetMapping("/message/{address_id}")
    public ResponseEntity<List<Message>> list(@PathVariable int address_id) {
        return ResponseEntity.ok(messageRepository.findByAddressId(address_id).get());
    }
    @CrossOrigin
    @MessageMapping("/chat.newUser")
    @SendTo("/topic/public")
    public String newUser(@Payload Message chatMessage) {
        System.out.println(chatMessage);
        return "Helo";
    }
}
