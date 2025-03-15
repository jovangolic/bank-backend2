package com.bankapp.bank_backend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Data
@Entity
public class User {	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String password;
	
	@Email
	@NotEmpty
	@Column(unique = true)
	private String email;
	
	@NotEmpty
	private String countryCode;
	
	@NotEmpty
	@Column(unique = true)
	private String phoneNumber;
	
	@NotEmpty
	private String address;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Account account;
}
