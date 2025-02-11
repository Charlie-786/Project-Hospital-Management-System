package com.zeecare.hms2.service;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zeecare.hms2.dto.MessageRequest;
import com.zeecare.hms2.entity.Message;
import com.zeecare.hms2.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message sendMessage(MessageRequest request) {
        Message message = new Message();
        message.setFirstName(request.getFirstName());
        message.setLastName(request.getLastName());
        message.setEmail(request.getEmail());
        message.setPhone(request.getPhone());
        message.setMessage(request.getMessage());
        message.setCreatedAt(new Date());

        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}