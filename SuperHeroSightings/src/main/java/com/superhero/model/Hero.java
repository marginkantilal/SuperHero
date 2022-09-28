package com.superhero.model;

import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hero {
    private int id;
    
    @NotBlank(message = "Name must not be empty.")
    @Size(max = 50, message = "Name must be less than 50 characters.")
    private String name;    

    @Size(max = 50, message = "Description must be less than 50 characters.")
    private String description;
    private List<Location> locations;
    private List<Power> powers;
    private List<Organisation> organizations;
	@Override
	public int hashCode() {
		return Objects.hash(description, id, locations, name, organizations, powers);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Hero)) {
			return false;
		}
		Hero other = (Hero) obj;
		return Objects.equals(description, other.description) && id == other.id
				&& Objects.equals(locations, other.locations) && Objects.equals(name, other.name)
				&& Objects.equals(organizations, other.organizations) && Objects.equals(powers, other.powers);
	}
    
    

}
