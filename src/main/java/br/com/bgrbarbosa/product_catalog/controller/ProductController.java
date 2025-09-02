package br.com.bgrbarbosa.product_catalog.controller;

import br.com.bgrbarbosa.product_catalog.controller.mapper.ProductMapper;
import br.com.bgrbarbosa.product_catalog.model.Product;
import br.com.bgrbarbosa.product_catalog.model.dto.ProductDTO;
import br.com.bgrbarbosa.product_catalog.service.EmailService;
import br.com.bgrbarbosa.product_catalog.service.ProductService;
import br.com.bgrbarbosa.product_catalog.service.ProductServiceReport;
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
import java.util.*;

@RestController
@RequestMapping(value = "/product")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService service;
	private final ProductMapper mapper;
	private final EmailService emailService;
	private final ProductServiceReport productReport;

	@GetMapping
	public ResponseEntity<Page<ProductDTO>> findAll(
			ProductFilter filter,
			@PageableDefault(page = 0, size = 10, sort = "uuid", direction = Sort.Direction.ASC) Pageable page){

		List<ProductDTO> listDTO = mapper.parseToListDTO(service.findAll(page, filter));
		Page<ProductDTO> pageDTO = mapper.toPageDTO(listDTO, page);
		return ResponseEntity.ok(pageDTO);
	}

	@GetMapping(value = "/{uuid}")
	public ResponseEntity<ProductDTO> findById(@PathVariable UUID uuid) {
		ProductDTO dto = mapper.parseToDto(service.findById(uuid));
		return ResponseEntity.ok().body(dto);
	}

	@GetMapping("/report")
	public void gerarRelatorio(
			HttpServletResponse response,
			ProductFilter filter,
			@RequestParam(name = "fileType", defaultValue = "pdf") String fileType
	) throws JRException, IOException {

		// Carregar e compilar o arquivo JRXML
		InputStream jasperStream = this.getClass().getResourceAsStream("/reports/Produto.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(jasperStream);

		// Definir os dados para o relatório
		List<Product> dados = service.findAll(filter);
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
	public ResponseEntity<ProductDTO> insert(@RequestBody @Valid ProductDTO dto) {
		Product result = service.insert(mapper.parseToEntity(dto));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{uuid}")
				.buildAndExpand(result.getUuidProduct()).toUri();
		return ResponseEntity.created(uri).body(mapper.parseToDto(result));
	}

	@PostMapping("/enviar-email")
	public String enviarEmail(@RequestParam String destination) {
		try {
			emailService.sendingProductListByEmail(destination);
			return "E-mail enviado com sucesso!";
		} catch (Exception e) {
			e.printStackTrace();
			return "Erro ao enviar e-mail: " + e.getMessage();
		}
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
