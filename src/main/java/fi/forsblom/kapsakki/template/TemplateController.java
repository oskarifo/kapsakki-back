package fi.forsblom.kapsakki.template;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/templates")
public class TemplateController {

	@Autowired
	ITemplateService templateService;
	
	@Autowired
	ModelMapper modelMapper;
	
	@GetMapping()
	public List<TemplateSimpleDto> getTemplates(Principal principal) {
		List<Template> templates = templateService.getTemplates(principal.getName());
		return templates.stream()
				.map(template -> convertToSimpleDto(template))
		        .collect(Collectors.toList());
	}
	
	@GetMapping("/{templateId}")
	public TemplateDto getTemplate(Principal principal, @PathVariable String templateId) {
		return convertToDto(templateService.getTemplate(Integer.parseInt(templateId), principal.getName()));
	}
	
	@PostMapping()
	public TemplateDto createTemplate(@RequestBody TemplateDto templateDto, Principal principal) {
		return convertToDto(templateService.createTemplate(convertToEntity(templateDto), principal.getName()));
	}
	
	private TemplateSimpleDto convertToSimpleDto(Template template) {
		TemplateSimpleDto templateSimpleDto = modelMapper.map(template, TemplateSimpleDto.class);

		return templateSimpleDto;
	}
	
	private TemplateDto convertToDto(Template template) {
		TemplateDto templateDto = modelMapper.map(template, TemplateDto.class);
		
		return templateDto;
	}
	
	private Template convertToEntity(TemplateDto templateDto) {
		Template template = modelMapper.map(templateDto, Template.class);
		
		return template;
	}
}
