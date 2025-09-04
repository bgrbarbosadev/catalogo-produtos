package br.com.bgrbarbosa.product_catalog.controller;

import br.com.bgrbarbosa.product_catalog.controller.mapper.UserMapper;
import br.com.bgrbarbosa.product_catalog.model.User;
import br.com.bgrbarbosa.product_catalog.model.dto.UserDTO;
import br.com.bgrbarbosa.product_catalog.model.dto.UserRequestDTO;
import br.com.bgrbarbosa.product_catalog.model.dto.UserResponseDTO;
import br.com.bgrbarbosa.product_catalog.security.TokenService;
import br.com.bgrbarbosa.product_catalog.service.UserService;
import br.com.bgrbarbosa.product_catalog.service.exception.UserException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService service;
	private final UserMapper mapper;
	private final PasswordEncoder passwordEncoder;
	private final TokenService tokenService;

	@GetMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<Page<UserDTO>> findAll(
			@PageableDefault(page = 0, size = 10, sort = "uuid", direction = Sort.Direction.ASC) Pageable page){

		List<UserDTO> listDTO = mapper.parseToListDTO(service.findAll(page));
		Page<UserDTO> pageDTO = mapper.toPageDTO(listDTO, page);
		return ResponseEntity.ok(pageDTO);
	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<UserDTO> findById(@PathVariable UUID uuid) {
		UserDTO dto = mapper.parseToDto(service.findById(uuid));
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<UserDTO> insert(@RequestBody @Valid UserDTO dto) throws UserException {
		User result = service.insert(mapper.parseToEntity(dto));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{uuid}")
				.buildAndExpand(result.getUuid()).toUri();
		return ResponseEntity.created(uri).body(mapper.parseToDto(result));
	}

	@PutMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<UserDTO> update(@RequestBody @Valid UserDTO dto) {
		User result = service.update(mapper.parseToEntity(dto));
		return ResponseEntity.ok().body(mapper.parseToDto(result));
	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
		service.delete(uuid);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/login")
	public ResponseEntity login(@RequestBody UserRequestDTO request){
		User user = service.loadUserByUsername(request.email());
		if(passwordEncoder.matches(request.password(), user.getPassword())) {
			String token = this.tokenService.generateToken(user);
			return ResponseEntity.ok(new UserResponseDTO(user.getFirstName(), token));
		}
		return ResponseEntity.badRequest().build();
	}
} 
