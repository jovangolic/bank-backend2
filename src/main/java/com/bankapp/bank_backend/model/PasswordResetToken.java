package com.bankapp.bank_backend.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="passwordresettoken")
@Data
@NoArgsConstructor
public class PasswordResetToken implements Serializable {

	//private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passwordresettoken_sequence")
	@SequenceGenerator(name = "passwordresettoken_sequence", sequenceName = "passwordresettoken_sequence", allocationSize = 100)
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String token;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(nullable = false)
	private LocalDateTime expiredDateTime;
	
	public PasswordResetToken(String token, User user, LocalDateTime expiredDateTime) {
		this.token = token;
		this.user = user;
		this.expiredDateTime = expiredDateTime;
	}
	
	//metoda koja proverava da li je token validan
	public boolean isTokenValid() {
		return getExpiredDateTime().isAfter(LocalDateTime.now());
	}
}












