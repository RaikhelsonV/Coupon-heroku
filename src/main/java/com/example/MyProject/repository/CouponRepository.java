package com.example.MyProject.repository;

import com.example.MyProject.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    List<Coupon> findAllByDescription(String description);
    List<Coupon> findAllByTitle(String title);
    List<Coupon> findAllByCategory(String category);
    List<Coupon> findAllByPriceLessThan(double price);
    List<Coupon> findAllByPriceIsGreaterThan(double price);

    void deleteById(long id_coupon);

    @Query("select coupon from Customer as customer join customer.coupons as coupon where customer.id=:customer_id")
    List<Coupon> findAllByCustomerId(long customer_id);

    @Query("select coupon from Company as company join company.coupons as coupon where company.id=:company_id")
    List<Coupon> findAllByCompanyId(long company_id);


}
