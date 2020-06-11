package com.luckydrive.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity(name="organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_id")
    private Long organizationId;
    
    private String name;
    
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "organization_point_connect", 
        joinColumns = { @JoinColumn(name = "organization_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "point_id") }
    )
    private Set<Point> locationPoints;

    public Organization() {
    	
    }

	public Organization(String name, Set<Point> locationPoints) {
		super();
		this.name = name;
		this.locationPoints = locationPoints;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Point> getLocationPoints() {
		return locationPoints;
	}

	public void setLocationPoints(Set<Point> locationPoints) {
		this.locationPoints = locationPoints;
	}
    
	
    
}
