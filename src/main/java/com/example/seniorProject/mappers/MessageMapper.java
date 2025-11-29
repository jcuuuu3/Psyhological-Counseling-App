package com.example.seniorProject.mappers;

import com.example.seniorProject.models.DTOs.OutputMessage;
import com.example.seniorProject.models.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public OutputMessage map(Message message) {
        OutputMessage outputMessage = new OutputMessage();
        outputMessage.setDate(message.getDate());
        outputMessage.setText(message.getText());
        return outputMessage;
    }
}
