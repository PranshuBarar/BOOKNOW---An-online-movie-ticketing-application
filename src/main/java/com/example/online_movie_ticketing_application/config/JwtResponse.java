package com.example.online_movie_ticketing_application.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class JwtResponse {
    private final String jwttoken;
}
