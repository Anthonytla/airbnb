package com.quest.etna.controller;

import com.quest.etna.model.Message;
import com.quest.etna.model.MessageModel;
import com.quest.etna.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageModelController {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat/{to}")
    public void sendMessage(@DestinationVariable String to, Message message) {
        System.out.println("handling send message: " + message.getText() + " to: " + to);
        Message toSave = new Message(message.getFromHost(), message.getText(), message.getAddress());
        System.out.println(message.getText());
        messageRepository.save(toSave);
        //boolean isExists = UserStorage.getInstance().getUsers().contains(to);
        //if (isExists) {
            simpMessagingTemplate.convertAndSend("/topic/public/"+message.getAddress().getId(), toSave);
        //}
    }
}