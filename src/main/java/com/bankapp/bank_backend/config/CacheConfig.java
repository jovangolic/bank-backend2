package com.bankapp.bank_backend.config;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

import lombok.val;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public CacheManager cacheManager() {
		val cacheManager = new CaffeineCacheManager();
		//definise cache naziv
		cacheManager.setCacheNames(List.of("otpAttempts"));
		cacheManager.setCaffeine(caffeineConfig());
		return cacheManager;
	}

	private Caffeine<Object, Object> caffeineConfig() {
		return Caffeine.newBuilder()
				.expireAfterWrite(15, TimeUnit.MINUTES) //Unos u kes memoriju istice nakon 15 minuta
				.maximumSize(100) //Maksimalno 100 unosa u kes memoriji
				.recordStats(); //Za pracenje statistike kesa (opcionalno)
	}
	
}
