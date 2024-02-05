package org.osh.service;

import lombok.RequiredArgsConstructor;
import org.osh.entity.CompanyEntity;
import org.osh.repository.CompanyRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoreService {

    private final CompanyRepository companyRepository;

    public CompanyEntity getCompany(String uid) {
        return companyRepository.findByUid(uid);
    }
}
