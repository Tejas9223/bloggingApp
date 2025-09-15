package com.codewithdurgesh.payloads;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
	private Integer id;

	@NotEmpty
	@Size(min = 4, message = "username must be minimum of 4 characters")
	private String name;

	@NotEmpty
	@Email(message = "email should be valid")
	private String email;

	@NotEmpty
	@Size(min = 9, message = "password must be at least 9 characters")
	private String password;

	@NotEmpty
	private String about;

	private Set<RoleDto> roles = new HashSet<>();   // used in response

	private Set<Integer> roleIds = new HashSet<>();  // ✅ used during registration
	
	private String roleName; // ✅ optional, used by controller to assign one role directly like "ROLE_USER" or "ROLE_ADMIN"
}
