package com.example.cb.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(	name = "users",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = "username"),
				@UniqueConstraint(columnNames = "email")
		})
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long user_id;
	private String username;
	private String email;
	private String password;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(	name = "user_role",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public User() {}

	public User(User user) {
		this.user_id = user.user_id;
		this.username = user.username;
		this.email = user.email;
		this.password = user.password;
		this.roles = user.roles;
	}

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Role> getRoles() {
		return roles;
	}

    public Set<String> getStrRoles(){
    	Set<String> list = new HashSet<>();
    	for(Role r : roles) {
    		if(r.getRole().equals(RoleEnum.ROLE_ADMIN)) list.add("admin");
    		else if (r.getRole().equals(RoleEnum.ROLE_USER)) list.add("user");
    	}
    	return list;
    }

    public void setId(Object o) {
    }
}
