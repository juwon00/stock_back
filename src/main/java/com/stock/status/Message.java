package com.stock.status;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Data
@Builder
public class Message {

    private String timestamp;
    private int code;
    private String status;
    private String message;
    private Object data;

    public static ResponseEntity<Message> MessagetoResponseEntity(Object data) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Message.builder()
                        .timestamp(String.valueOf(LocalDateTime.now()))
                        .code(HttpStatus.OK.value())
                        .status(String.valueOf(HttpStatus.OK).split(" ")[1])
                        .message("성공 코드")
                        .data(data)
                        .build()
                );
    }
}
