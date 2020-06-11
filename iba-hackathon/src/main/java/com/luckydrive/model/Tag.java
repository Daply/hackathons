package com.luckydrive.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity(name="tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;
    
    @Column(name = "tag_name")
    private String tagName;
    
    @OneToOne
    @JoinColumn(name="tag_point_id", nullable=false)
    private Point tagPoint;
    
    @ManyToMany(mappedBy = "tags")
    private Set<Trip> trips = new HashSet<>();
    
    public Tag() {
        
    }

    public Tag(String tagName, Point tagPoint) {
        super();
        this.tagName = tagName;
        this.tagPoint = tagPoint;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Point getTagPoint() {
		return tagPoint;
	}

	public void setTagPoint(Point tagPoint) {
		this.tagPoint = tagPoint;
	}

	public Set<Trip> getTrips() {
        return trips;
    }

    public void setTrips(Set<Trip> trips) {
        this.trips = trips;
    }
    
}
