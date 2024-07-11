package com.example.concert_reservation.controller;


import com.example.concert_reservation.dto.ConcertResponseDto;
import com.example.concert_reservation.entity.Concert;
import com.example.concert_reservation.service.ConcertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ConcertController {

   private final ConcertService concertService;

   public ConcertController(ConcertService concertService) {
       this.concertService = concertService;
   }
    @GetMapping("/concerts")
    public ResponseEntity<?> getConcertList() {
        List<Concert> concertList = concertService.getConcertList();
        return ResponseEntity.ok(new ConcertResponseDto(concertList));
    }
}
