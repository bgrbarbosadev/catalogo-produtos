package br.com.bgrbarbosa.product_catalog.controller;

import br.com.bgrbarbosa.product_catalog.controller.mapper.ProductMapper;
import br.com.bgrbarbosa.product_catalog.model.Product;
import br.com.bgrbarbosa.product_catalog.model.dto.ProductDTO;
import br.com.bgrbarbosa.product_catalog.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/product")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService service;
	private final ProductMapper mapper;

	@GetMapping
	public ResponseEntity<List<ProductDTO>> findAll() {
		List<ProductDTO> list = mapper.parseToListDTO(service.findAll());
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{uuid}")
	public ResponseEntity<ProductDTO> findById(@PathVariable UUID uuid) {
		ProductDTO dto = mapper.parseToDto(service.findById(uuid));
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping
	public ResponseEntity<ProductDTO> insert(@RequestBody @Valid ProductDTO dto) {
		Product result = service.insert(mapper.parseToEntity(dto));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{uuid}")
				.buildAndExpand(result.getUuid()).toUri();
		return ResponseEntity.created(uri).body(mapper.parseToDto(result));
	}

	@PutMapping
	public ResponseEntity<ProductDTO> update(@RequestBody @Valid ProductDTO dto) {
		Product result = service.update(mapper.parseToEntity(dto));
		return ResponseEntity.ok().body(mapper.parseToDto(result));
	}

	@DeleteMapping(value = "/{uuid}")
	public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
		service.delete(uuid);
		return ResponseEntity.noContent().build();
	}
} 
