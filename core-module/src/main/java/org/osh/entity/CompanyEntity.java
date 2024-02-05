package org.osh.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import lombok.Getter;

@Entity(name = "company")
@Getter
public class CompanyEntity implements Serializable {

    @Id
    private String uid;

    private String companyType;
    private String companyName;

}
