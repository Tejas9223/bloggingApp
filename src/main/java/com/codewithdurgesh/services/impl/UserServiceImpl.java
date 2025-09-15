package com.codewithdurgesh.services.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.codewithdurgesh.entities.Role;
import com.codewithdurgesh.entities.User;
import com.codewithdurgesh.exceptions.ResourceNotFoundException;
import com.codewithdurgesh.payloads.UserDto;
import com.codewithdurgesh.repositories.RoleRepo;
import com.codewithdurgesh.repositories.UserRepo;
import com.codewithdurgesh.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepo roleRepo;

	@Override
	public UserDto createUser(UserDto userDto) {
		User user = this.dtoUser(userDto);
		User savedUser = this.userRepo.save(user);
		return this.userToDto(savedUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(()-> new ResourceNotFoundException("User","Id", userId));
		
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setAbout(userDto.getAbout());
		user.setPassword(userDto.getPassword());
		
		User updatedUser = this.userRepo.save(user);
		UserDto userToDto1 = this.userToDto(updatedUser);
		
		return userToDto1;
	}

	@Override
	public UserDto getUserById(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(()-> new ResourceNotFoundException("User","Id",userId));
		return userToDto(user);
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<User> users = this.userRepo.findAll();
		List<UserDto> userDtos = users.stream().map(user->this.userToDto(user)).collect(Collectors.toList());
		return userDtos;
	}
 
	@Override
	public void deleteUser(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(()-> new ResourceNotFoundException("User","Id",userId));
		this.userRepo.delete(user);
	} 
	
	public User dtoUser(UserDto userDto)
	{
		User user = this.modelMapper.map(userDto, User.class);
		//user.setId(userDto.getId());
		//user.setName(userDto.getName());
		//user.setEmail(userDto.getEmail());
		//user.setAbout(userDto.getAbout());
		//user.setPassword(userDto.getPassword());
		
		return user;	
	}
	
	public UserDto userToDto(User user)
	{
		UserDto userDto = this.modelMapper.map(user, UserDto.class);
		//userDto.setId(user.getId());
		//userDto.setName(user.getName());
		//userDto.setEmail(user.getEmail());
		//userDto.setAbout(user.getAbout());
		//userDto.setPassword(user.getPassword()); 
		
		return userDto;
	}

	@Override
	public UserDto registerNewUser(UserDto userDto) {
	    User user = this.modelMapper.map(userDto, User.class);

	    // Encode password
	    user.setPassword(passwordEncoder.encode(user.getPassword()));

	    Set<Role> roles;

	    if (userDto.getRoleIds() == null || userDto.getRoleIds().isEmpty()) {
	        // 🔐 Assign default role ROLE_USER if no roleIds provided
	        Role userRole = roleRepo.findByName("ROLE_USER")
	                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", null));
	        roles = Set.of(userRole);
	    } else {
	        // 🔄 Map provided role IDs
	        roles = userDto.getRoleIds().stream()
	                .map(roleId -> roleRepo.findById(roleId)
	                        .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId)))
	                .collect(Collectors.toSet());
	    }

	    user.setRoles(roles);

	    // Save and return
	    User savedUser = userRepo.save(user);
	    return this.modelMapper.map(savedUser, UserDto.class);
	}

 
	
	@Override
	public boolean adminExists() {
	    Role adminRole = roleRepo.findByName("ROLE_ADMIN")
	            .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

	    return userRepo.existsByRolesContaining(adminRole);
	}


}

