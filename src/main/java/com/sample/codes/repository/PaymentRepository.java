package com.sample.codes.repository;

import com.sample.codes.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.formEntity.id IN :ids")
    List<Payment> findAllByFormEntityIds(@Param("ids") List<Long> ids);

}
