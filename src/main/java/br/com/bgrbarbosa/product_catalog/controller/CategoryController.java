package br.com.bgrbarbosa.product_catalog.controller;

import br.com.bgrbarbosa.product_catalog.controller.mapper.CategoryMapper;
import br.com.bgrbarbosa.product_catalog.model.Category;
import br.com.bgrbarbosa.product_catalog.model.Product;
import br.com.bgrbarbosa.product_catalog.model.dto.CategoryDTO;
import br.com.bgrbarbosa.product_catalog.model.dto.ProductDTO;
import br.com.bgrbarbosa.product_catalog.service.CategoryService;
import br.com.bgrbarbosa.product_catalog.specification.filter.ProductFilter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	@GetMapping("/report")
	public void gerarRelatorio(
			HttpServletResponse response,
			ProductFilter filter,
			@RequestParam(name = "fileType", defaultValue = "pdf") String fileType
	) throws JRException, IOException {

		// Carregar e compilar o arquivo JRXML
		InputStream jasperStream = this.getClass().getResourceAsStream("/reports/Categoria.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(jasperStream);

		// Definir os dados para o relatório
		List<Category> dados = service.findAll();
		JRDataSource dataSource = new JRBeanCollectionDataSource(dados);

		// Adicionar parâmetros ao relatório (opcional)
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("titulo", "Relatório de Exemplo");

		// Preencher o relatório
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

		// --- Lógica de exportação dinâmica ---
		OutputStream outputStream = response.getOutputStream();

		if ("xlsx".equalsIgnoreCase(fileType)) {
			// Configurar para XLSX
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=\"relatorio.xlsx\"");

			// Exportar para XLSX
			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			exporter.exportReport();

		} else if ("csv".equalsIgnoreCase(fileType)) {
			// Configurar para CSV
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", "attachment; filename=\"relatorio.csv\"");

			// Exportar para CSV
			JRCsvExporter exporter = new JRCsvExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleWriterExporterOutput(outputStream));
			exporter.exportReport();

		} else {
			// Padrão: exportar para PDF
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=\"relatorio.pdf\"");

			JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
		}
	}
	
	@PostMapping
	public ResponseEntity<CategoryDTO> insert(@RequestBody @Valid CategoryDTO dto) {
		Category result = service.insert(mapper.parseToEntity(dto));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{uuid}")
				.buildAndExpand(result.getUuidCategory()).toUri();
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
