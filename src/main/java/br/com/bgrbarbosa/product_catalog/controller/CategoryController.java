package br.com.bgrbarbosa.product_catalog.controller;

import br.com.bgrbarbosa.product_catalog.controller.mapper.CategoryMapper;
import br.com.bgrbarbosa.product_catalog.model.Category;
import br.com.bgrbarbosa.product_catalog.model.dto.CategoryDTO;
import br.com.bgrbarbosa.product_catalog.model.dto.ProductDTO;
import br.com.bgrbarbosa.product_catalog.service.CategoryService;
import br.com.bgrbarbosa.product_catalog.specification.filter.ProductFilter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
	public ResponseEntity<Page<CategoryDTO>> findAll(
			@PageableDefault(page = 0, size = 10, sort = "uuid", direction = Sort.Direction.ASC) Pageable page){

		List<CategoryDTO> listDTO = mapper.parseToListDTO(service.findAll(page));
		Page<CategoryDTO> pageDTO = mapper.toPageDTO(listDTO, page);
		return ResponseEntity.ok(pageDTO);
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
