package com.example.MyProject.repository;

import com.example.MyProject.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository <Customer, Long>{

    Customer findByLastName(String lastName);
    Optional<Customer> findByFirstName(String firstName);
    List<Customer> findAllCustomersByCoupons(long coupon_id);

}
