package fi.forsblom.kapsakki.list;

import java.security.Principal;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fi.forsblom.kapsakki.listItem.IListItemService;
import fi.forsblom.kapsakki.listItem.ListItem;
import fi.forsblom.kapsakki.listItem.ListItemDto;

@RestController()
@RequestMapping("/api/lists")
public class ListController {
	
	@Autowired
	IListItemService listItemService;
	
	@Autowired 
	IListService listService;
	
	@Autowired
	ModelMapper modelMapper;
	
    private static Logger LOGGER = LoggerFactory.getLogger(ListController.class);
	
	@GetMapping(
			produces=MediaType.APPLICATION_JSON_VALUE)
	public java.util.List<ListSimpleDto> getLists(Principal principal) {
		LOGGER.debug("event={} user={}", "get-lists", principal.getName());
		java.util.List<List> lists = listService.getLists(principal.getName());
		return lists.stream()
		          .map(list -> convertToSimpleDto(list))
		          .collect(Collectors.toList());
	}
	
	@PostMapping(
			consumes=MediaType.APPLICATION_JSON_VALUE, 
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ListDto createList(@Valid @RequestBody ListDto listDto, Principal principal) {
		LOGGER.debug("event={} user={}", "create-list", principal.getName());
		return convertToDto(listService.createList(convertToEntity(listDto), principal.getName()));
	}
	
	@GetMapping(value="/{listId}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ListDto getList(Principal principal, @PathVariable String listId) {
		LOGGER.debug("event={} user={} list={}", "get-list", principal.getName(), listId);
		return convertToDto(listService.getList(Integer.parseInt(listId), principal.getName()));
	}
	
	@PutMapping(value="/{listId}",
			consumes=MediaType.APPLICATION_JSON_VALUE, 
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ListDto editList(@PathVariable String listId, @Valid @RequestBody ListDto listDto, Principal principal) {
		LOGGER.debug("event={} user={} list={}", "edit-list", principal.getName(), listId);
		return convertToDto(listService.editList(convertToEntity(listDto), principal.getName()));
	}
		
	@DeleteMapping(value="/{listId}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public void deleteList(Principal principal, @PathVariable String listId) {
		LOGGER.debug("event={} user={} list={}", "delete-list", principal.getName(), listId);
		listService.deleteList(Integer.parseInt(listId), principal.getName());
	}
	
	@PostMapping(value="/{listId}/listitems",
			consumes=MediaType.APPLICATION_JSON_VALUE, 
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ListItemDto addListItem(@PathVariable String listId, @RequestBody ListItemDto listItemDto, Principal principal) {
		LOGGER.debug("event={} user={} list={}", "create-list-item", principal.getName(), listId);
		return convertToDto(listItemService.addListItem(Integer.parseInt(listId), convertToEntity(listItemDto), principal.getName()));
	}
	
	@DeleteMapping(value="/{listId}/listitems",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public void deleteListItems(@PathVariable String listId, @RequestBody java.util.List<ListItemDto> listItemsDto, Principal principal) {
		LOGGER.debug("event={} user={} list={}", "delete-list", principal.getName(), listId);
		java.util.List<ListItem> listItems = listItemsDto.stream()
				.map(listItemDto -> convertToEntity(listItemDto))
		        .collect(Collectors.toList());
		
		listItemService.deleteListItems(Integer.parseInt(listId), listItems, principal.getName());
	}
	
	@PatchMapping(value="/{listId}/listitems",
			consumes=MediaType.APPLICATION_JSON_VALUE, 
			produces=MediaType.APPLICATION_JSON_VALUE)
	public java.util.List<ListItemDto> patchListItems(@PathVariable String listId, @RequestBody java.util.List<ListItemDto> listItemsDto, Principal principal) {
		LOGGER.debug("event={} user={} list={}", "patch-list-items", principal.getName(), listId);
		java.util.List<ListItem> listItems = listItemsDto.stream()
				.map(listItemDto -> convertToEntity(listItemDto))
		        .collect(Collectors.toList());
		
		java.util.List<ListItem> patchedListItems = listItemService.patchListItems(Integer.parseInt(listId), listItems, principal.getName());
		
		return patchedListItems.stream()
				.map(listItem -> convertToDto(listItem))
		        .collect(Collectors.toList());
	}
	
	@PutMapping(value="/{listId}/listitems/{listItemId}",
			consumes=MediaType.APPLICATION_JSON_VALUE, 
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ListItemDto editListItem(@PathVariable String listId, @PathVariable String listItemId, @RequestBody ListItemDto listItemDto, Principal principal) {
		LOGGER.debug("event={} user={} list={} list-item={}", "edit-list-item", principal.getName(), listId, listItemId);
		return convertToDto(listItemService.editListItem(convertToEntity(listItemDto), principal.getName()));
	}
	
	private ListSimpleDto convertToSimpleDto(List list) {
		LOGGER.debug("entity={}", list);
		ListSimpleDto listSimpleDto = modelMapper.map(list, ListSimpleDto.class);
		LOGGER.debug("dto={}", listSimpleDto);

		return listSimpleDto;
	}
	
	private ListDto convertToDto(List list) {
		LOGGER.debug("entity={}", list);
		ListDto listDto = modelMapper.map(list, ListDto.class);
		LOGGER.debug("dto={}", listDto);

		return listDto;
	}
	
	private ListItemDto convertToDto(ListItem listItem) {
		LOGGER.debug("entity={}", listItem);
		ListItemDto listItemDto = modelMapper.map(listItem, ListItemDto.class);
		LOGGER.debug("dto={}", listItemDto);
		
		return listItemDto;
	}
	
	private List convertToEntity(ListDto listDto) {
		LOGGER.debug("dto={}", listDto);
		List list = modelMapper.map(listDto, List.class);
		LOGGER.debug("entity={}", list);
		
		return list;
	}
	
	private ListItem convertToEntity(ListItemDto listItemDto) {
		LOGGER.debug("dto={}", listItemDto);
		ListItem listItem = modelMapper.map(listItemDto, ListItem.class);
		LOGGER.debug("entity={}", listItem);

		return listItem;
	}
}
