package fi.forsblom.kapsakki.list;

import java.util.ArrayList;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import fi.forsblom.kapsakki.listItem.ListItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString @AllArgsConstructor
public class ListDto {
	@NotNull
	private int id;
	@Size(min = 3, max = 20)
	private String name;
	@Pattern(regexp = "^.{3,20}$|^$", message = "size must be between 3 and 20 or empty")
	private String description;
	private ArrayList<ListItemDto> listItems = new ArrayList<ListItemDto>();
}
