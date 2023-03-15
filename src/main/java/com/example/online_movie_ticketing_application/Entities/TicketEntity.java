package com.example.online_movie_ticketing_application.Entities;

import com.example.online_movie_ticketing_application.Enums.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tickets")
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String movieName; //to be printed

    private LocalDate showDate; //to be printed

    private LocalTime showTime; //to be printed

    private int totalAmount; //to be printed

    private String ticketId = UUID.randomUUID().toString(); //to be printed

    private String theaterName; //to be printed

    private String bookedSeats;

    @Enumerated(value = EnumType.STRING)
    private TicketStatus status;

    @ManyToOne
    @JoinColumn
    ShowEntity showEntity;

    @ManyToOne
    @JoinColumn
    UserEntity userEntity;
}
