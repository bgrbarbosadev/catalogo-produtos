package br.com.bgrbarbosa.product_catalog.controller;

import br.com.bgrbarbosa.product_catalog.controller.mapper.CategoryMapper;
import br.com.bgrbarbosa.product_catalog.model.Category;
import br.com.bgrbarbosa.product_catalog.model.dto.CategoryDTO;
import br.com.bgrbarbosa.product_catalog.model.dto.ProductDTO;
import br.com.bgrbarbosa.product_catalog.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/category")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService service;
	private final CategoryMapper mapper;

	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll() {
		List<CategoryDTO> list = mapper.parseToListDTO(service.findAll());
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{uuid}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable UUID uuid) {
		CategoryDTO dto = mapper.parseToDto(service.findById(uuid));
		return ResponseEntity.ok().body(dto);
	}
	
	@PostMapping
	public ResponseEntity<CategoryDTO> insert(@RequestBody @Valid CategoryDTO dto) {
		Category result = service.insert(mapper.parseToEntity(dto));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{uuid}")
				.buildAndExpand(result.getUuid()).toUri();
		return ResponseEntity.created(uri).body(mapper.parseToDto(result));
	}

	@PutMapping
	public ResponseEntity<CategoryDTO> update(@RequestBody @Valid CategoryDTO dto) {
		Category result = service.update(mapper.parseToEntity(dto));
		return ResponseEntity.ok().body(mapper.parseToDto(result));
	}

	@DeleteMapping(value = "/{uuid}")
	public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
		service.delete(uuid);
		return ResponseEntity.noContent().build();
	}
} 
