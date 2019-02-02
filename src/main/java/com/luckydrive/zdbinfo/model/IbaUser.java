package com.luckydrive.zdbinfo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.luckydrive.model.CarInfo;

@Entity(name="employee")
public class IbaUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmployeeID")
    private Long userId;
    
    private String name;
    
    private String surname;
    
    @Column(name = "e_mail")
    private String email;
    
    @JsonIgnore
    private String password;
    
    private String phone;
    
    @OneToOne
    @JoinColumn(name="car_info_id", nullable=true)
    private CarInfo carInfo;
}
