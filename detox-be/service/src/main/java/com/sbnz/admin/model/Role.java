package com.sbnz.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ROLE")
public class Role implements GrantedAuthority {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(name = "name")
	String name;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "role_privilege",
			joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
	private Set<Privilege> privileges;

	@JsonIgnore
	@ManyToMany(mappedBy = "roles" , fetch = FetchType.LAZY)
	private Set<User> users;

	@Override
	public String getAuthority() {
		return name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}