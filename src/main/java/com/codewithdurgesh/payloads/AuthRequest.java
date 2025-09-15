package com.codewithdurgesh.payloads;

import lombok.Data;

@Data
public class AuthRequest { 
	
	private String username;
	private String password;

}
