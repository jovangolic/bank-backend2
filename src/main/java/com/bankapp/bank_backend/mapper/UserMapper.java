package com.bankapp.bank_backend.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.bankapp.bank_backend.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	public void updateUser(User source, @MappingTarget User target);
}
















