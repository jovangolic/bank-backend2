package com.bankapp.bank_backend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bankapp.bank_backend.exception.UserInvalidException;
import com.bankapp.bank_backend.model.User;
import com.bankapp.bank_backend.repository.UserRepository;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;


import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Component
@RequiredArgsConstructor
public class ValidationUtil {

	public static final Logger log = LoggerFactory.getLogger(ValidationUtil.class);
	public static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
	private final UserRepository userRepository;
	
	public static boolean isValidEmail(String identifier) {
		try {
			new InternetAddress(identifier).validate();
		}
		catch(AddressException e) {
			log.warn("Ivalid email address {}" + identifier);
		}
		return false;
	}
	
	public static boolean isValidAccountNumber(String identifier) {
		//proverava duzinu karaktera
		return identifier != null && identifier.length() > 6;
	}
	
	//provera koda za region
	public static boolean isValidCountryCode(String countryCode) {
		if(!phoneNumberUtil.getSupportedRegions().contains(countryCode)) {
			return false;
		}
		return true;
	}
	
	//metoda za proveravanje broja telefona
	public static boolean isValidPhoneNumber(String phoneNumber, String countryCode) {
		PhoneNumber parsedNumber = null;
		try {
			parsedNumber = phoneNumberUtil.parse(phoneNumber, countryCode);
		}
		catch(NumberParseException e) {
			throw new UserInvalidException(String.format(ApiMessages.USER_PHONE_NUMBER_INVALID_ERROR.getMessage(), phoneNumber, countryCode));
		}
		return phoneNumberUtil.isValidNumber(parsedNumber);
	}
	
	//metoda za validiranje lozinke
	public static void validatePassword(String password) {
		if(password.length() < 8) {
			throw new UserInvalidException(ApiMessages.PASSWORD_TOO_SHORT_ERROR.getMessage());
		}
		if(password.length() >= 128) {
			throw new UserInvalidException(ApiMessages.PASSWORD_TOO_LONG_ERROR.getMessage());
		}
		//ako sadrzi razmake
		if(password.matches(".*\\s.*")) {
			throw new UserInvalidException(ApiMessages.PASSWORD_CONTAINS_WHITESPACE_ERROR.getMessage());
		}
		val msg = new StringBuilder();
		msg.append("Password must contain at least ");
		var comma = false;
		//velika slova
		if(!password.matches(".*[A-Z].*")) {
			msg.append("one uppercase letter");
			comma = true;
		}
		//mala slova
		if(!password.matches(".*[a-z].*")) {
			if (comma) {
                msg.append(", ");
            }
			msg.append("one lower case");
			comma = true;
		}
		//brojevi
		if(!password.matches(".*[0-9].*")) {
			if(comma) {
				msg.append(", ");
			}
			msg.append("one digit");
			comma = true;
		}
		//karakteri sem slova i brojeva
		if(!password.matches(".*[^A-Za-z0-9].*")) {
			if(comma) {
				msg.append(", ");
			}
			msg.append("one special character");
		}
		if (msg.length() > "Password must contain at least ".length()) {
            val lastCommaIndex = msg.lastIndexOf(",");
            if (lastCommaIndex > -1) {
                msg.replace(lastCommaIndex, lastCommaIndex + 1, " and");
            }
            throw new UserInvalidException(msg.toString());
        }
	}
	
	//provera da li nije null ili prazno
	public static void validateUserDetailsNotEmpty(User user) {
        if (user == null) {
            throw new UserInvalidException(ApiMessages.USER_DETAILS_EMPTY_ERROR.getMessage());
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            throw new UserInvalidException(ApiMessages.USER_NAME_EMPTY_ERROR.getMessage());
        }

        if (user.getAddress() == null || user.getAddress().isEmpty()) {
            throw new UserInvalidException(ApiMessages.USER_ADDRESS_EMPTY_ERROR.getMessage());
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new UserInvalidException(ApiMessages.USER_EMAIL_EMPTY_ERROR.getMessage());
        }

        if (user.getCountryCode() == null || user.getCountryCode().isEmpty()) {
            throw new UserInvalidException(ApiMessages.USER_COUNTRY_CODE_EMPTY_ERROR.getMessage());
        }

        if (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty()) {
            throw new UserInvalidException(ApiMessages.USER_PHONE_NUMBER_EMPTY_ERROR.getMessage());
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new UserInvalidException(ApiMessages.PASSWORD_EMPTY_ERROR.getMessage());
        }
    }
	
	public static void validateUserDetails(User user) {
        validateUserDetailsNotEmpty(user);

        if (!isValidEmail(user.getEmail())) {
            throw new UserInvalidException(String.format(ApiMessages.USER_EMAIL_ADDRESS_INVALID_ERROR.getMessage(), user.getEmail()));
        }

        if (!isValidCountryCode(user.getCountryCode())) {
            throw new UserInvalidException(String.format(ApiMessages.USER_COUNTRY_CODE_INVALID_ERROR.getMessage(), user.getCountryCode()));
        }

        if (!isValidPhoneNumber(user.getPhoneNumber(), user.getCountryCode())) {
            throw new UserInvalidException(String.format(ApiMessages.USER_PHONE_NUMBER_INVALID_ERROR.getMessage(), user.getPhoneNumber(), user.getCountryCode()));
        }

        validatePassword(user.getPassword());
    }

    public void validateNewUser(User user) {
        validateUserDetails(user);
        if (doesEmailExist(user.getEmail())) {
            throw new UserInvalidException(ApiMessages.USER_EMAIL_ALREADY_EXISTS_ERROR.getMessage());
        }
        if (doesPhoneNumberExist(user.getPhoneNumber())) {
            throw new UserInvalidException(ApiMessages.USER_PHONE_NUMBER_ALREADY_EXISTS_ERROR.getMessage());
        }
    }

    public boolean doesAccountExist(String accountNumber) {
        return userRepository.findByAccountAccountNumber(accountNumber).isPresent();
    }

    public boolean doesEmailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean doesPhoneNumberExist(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).isPresent();
    }
}
