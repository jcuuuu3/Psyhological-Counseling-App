package com.example.seniorProject.services;

import com.example.seniorProject.mappers.MessageMapper;
import com.example.seniorProject.models.Counselor;
import com.example.seniorProject.models.DTOs.InputGptMessage;
import com.example.seniorProject.models.DTOs.InputMessage;
import com.example.seniorProject.models.DTOs.OutputGptMessage;
import com.example.seniorProject.models.DTOs.OutputMessage;
import com.example.seniorProject.models.Message;
import com.example.seniorProject.models.Student;
import com.example.seniorProject.repositories.MessageRepository;
import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@RequiredArgsConstructor
public class MessagingService {

    @Value("${key}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();

    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final CounselorService counselorService;
    private final StudentService studentService;
    private final MessageMapper messageMapper;

    @Transactional
    public void processIncomingMessage(InputMessage inputMessage, String senderUsername) {
        Message message = new Message();
        message.setText(inputMessage.getText());
        message.setDate(LocalDateTime.now());
        Counselor counselor;
        Student student;
        if(inputMessage.isFromCounselor()){
            counselor = counselorService.getCounselor(senderUsername);
            student = studentService.getStudentByUsername(inputMessage.getRecipientUsername());
            message.setSender(true);
        }
        else{
            counselor = counselorService.getCounselor(inputMessage.getRecipientUsername());
            student = studentService.getStudentByUsername(senderUsername);
            message.setSender(false);
        }
        message.setCounselor(counselor);
        message.setStudent(student);
        OutputMessage outputMessage = messageMapper.map(message);
        outputMessage.setSenderUsername(senderUsername);
        messageRepository.save(message);
        messagingTemplate.convertAndSendToUser(inputMessage.getRecipientUsername(), "/queue/messages", outputMessage);
    }


    public void processChatGptMessage(InputGptMessage gptMessage, String senderUsername) {
        gptMessage.setModel("gpt-4.1");
        String result = restClient.post()
                .uri("https://api.openai.com/v1/responses")
                .header("Authorization", "Bearer " + apiKey)
                .contentType(APPLICATION_JSON)
                .body(gptMessage)
                .retrieve()
                .body(String.class);
        String text = JsonPath.read(result, "$.output[0].content[0].text");
        messagingTemplate.convertAndSendToUser(senderUsername, "/queue/gpt", new OutputGptMessage(text));
    }


}
