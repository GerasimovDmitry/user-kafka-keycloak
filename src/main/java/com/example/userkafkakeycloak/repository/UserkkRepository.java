package com.example.userkafkakeycloak.repository;

import com.example.userkafkakeycloak.entity.Userkk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserkkRepository extends JpaRepository<Userkk, Long> {
    Userkk findByUserName(String userName);

    @Modifying
    @Query(value = "SELECT * FROM userkk where company_id= :companyId", nativeQuery = true)
    List<Userkk> findByCompany_id(Integer companyId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE userkk SET company_id = null WHERE id = :id", nativeQuery = true)
    void updateCompany_id(Integer id);
}
