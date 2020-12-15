package com.example.MyProject.repository;

import com.example.MyProject.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findCompanyByName(String name);
    Company findCompanyById(long id);

}
