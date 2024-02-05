package org.osh.repository;

import org.osh.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<CompanyEntity, String> {

    CompanyEntity findByUid(String uid);
}
