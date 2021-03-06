package com.vet24.models.medicine;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"manufactureName" , "name"})})
@Data
@NoArgsConstructor
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String manufactureName;

    @Column(nullable = false)
    private String name;

    private String icon;

    @Column(nullable = false)
    private String description;

    public Medicine(String manufactureName, String name, String icon, String description) {
        this.manufactureName = manufactureName;
        this.name = name;
        this.icon = icon;
        this.description = description;
    }
}
