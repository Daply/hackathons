package com.luckydrive.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name="car_info")
public class CarInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_info_id")
    private Long carInfoId;
    
    @OneToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    
    @Column(name = "car_mark", nullable=true)
    private String carMark;
    
    @Column(name = "car_number", nullable=true)
    private String carNumber;
    
    @Column(name = "car_color", nullable=true)
    private String carColor;
    
    @OneToOne
    @JoinColumn(name="car_rules_id", nullable=true)
    private CarRules carRules;

    public CarInfo() {
        
    }

	public CarInfo(User user, String carMark, String carNumber, String carColor) {
		super();
		this.user = user;
		this.carMark = carMark;
		this.carNumber = carNumber;
		this.carColor = carColor;
	}

	public Long getCarInfoId() {
		return carInfoId;
	}

	public void setCarInfoId(Long carInfoId) {
		this.carInfoId = carInfoId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCarMark() {
		return carMark;
	}

	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public String getCarColor() {
		return carColor;
	}

	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}

	public CarRules getCarRules() {
		return carRules;
	}

	public void setCarRules(CarRules carRules) {
		this.carRules = carRules;
	}

}
