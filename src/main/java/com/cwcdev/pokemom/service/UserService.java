package com.cwcdev.pokemom.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cwcdev.pokemom.exceptions.DatabaseException;
import com.cwcdev.pokemom.exceptions.ResourceNotFoundException;
import com.cwcdev.pokemom.model.Role;
import com.cwcdev.pokemom.model.User;
import com.cwcdev.pokemom.model.dto.RoleDTO;
import com.cwcdev.pokemom.model.dto.UserDTO;
import com.cwcdev.pokemom.projection.UserDetailsProjection;
import com.cwcdev.pokemom.repository.RoleRepository;
import com.cwcdev.pokemom.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository rolerepository;
	
	@Autowired
	private AuthService authService;

	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> list = repository.findAll(pageable);
		return list.map(x -> new UserDTO(x));

	}
	
	
	
	
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public UserDTO findMe() {
		User entity = authService.authenticated();
		return new UserDTO(entity);
	}


	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new UserDTO(entity);
	}

	
	
	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
		}
	}

	private void copyDtoToEntity(UserDTO dto, User entity) {

		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());

		entity.getRoles().clear();
		for (RoleDTO roleDto : dto.getRoles()) {

			/* getReferenceById nao busca no banco.... sem tocar no banco de dados */
			Role role = rolerepository.getReferenceById(roleDto.getId());
			entity.getRoles().add(role);

		}

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);

	    if (result.isEmpty()) {
	        throw new UsernameNotFoundException("User not found");
	    }

	    String password = result.get(0).getPassword();
	    List<String> authorities = result.stream()
	        .map(projection -> projection.getAuthority())
	        .collect(Collectors.toList());

	    return org.springframework.security.core.userdetails.User.builder()
	        .username(username)
	        .password(password)
	        .authorities(authorities.toArray(new String[0]))
	        .build();
	}

}
