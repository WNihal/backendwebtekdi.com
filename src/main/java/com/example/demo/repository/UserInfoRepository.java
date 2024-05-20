/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.repository;

import com.example.demo.model.CustommerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HP
 */
@Repository
public interface UserInfoRepository extends JpaRepository<CustommerInfo, Integer>{
    
     public CustommerInfo findByEmailAndPassword(String email, String password);
     public CustommerInfo findByEmail(String email);
    
}
