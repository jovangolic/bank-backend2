package com.bankapp.bank_backend.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;

import com.bankapp.bank_backend.dto.GeolocationResponse;



public interface GeolocationService {

	@Async
    public CompletableFuture<GeolocationResponse> getGeolocation(String ip);
}
