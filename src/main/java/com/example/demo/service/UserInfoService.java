/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service;

import com.example.demo.model.CustommerInfo;
import com.example.demo.model.MailStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repository.UserInfoRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 *
 * @author HP
 */
@Service
public class UserInfoService {

    public UserInfoService() {
        System.out.println("I'm in Service");
    }
    
    @Autowired
    private UserInfoRepository repository;
    @Autowired
    private JavaMailSender javaMailSender;
    
    
    public CustommerInfo saveRecords(CustommerInfo custommerInfo)
    {
        
        repository.save(custommerInfo);
        return custommerInfo;
    }
    
    
    public boolean verifyLoginCredentials(String email, String password) {
      CustommerInfo custommerInfo = repository.findByEmailAndPassword(email, password);
        return custommerInfo != null;
    }
    
    
    public boolean validateEmail(String email) {
      CustommerInfo custommerInfo = repository.findByEmail(email);  
                return custommerInfo != null;
    }
    
    
    public CustommerInfo getObjecctByEmail(String email)
    {
        CustommerInfo custommerInfo = repository.findByEmail(email);
        
        return custommerInfo;
    }
    
    
     public boolean updateCustomerByEmail(String email, CustommerInfo updatedCustomer) {
        CustommerInfo existingCustomer = repository.findByEmail(email);
        if (existingCustomer != null) {
            // Update the existing customer with the new data
            existingCustomer.setFirstName(updatedCustomer.getFirstName());
            existingCustomer.setLastName(updatedCustomer.getLastName());
            existingCustomer.setPhone(updatedCustomer.getPhone());
            existingCustomer.setPassword(updatedCustomer.getPassword());
            // Set other fields as needed

            // Save the updated customer to the database
            repository.save(existingCustomer);
            return true;
        } else {
            return false; // Customer with the given email not found
        }
    }
    
        public void sendMail(String mail, MailStructure mailStructure)
     {
         SimpleMailMessage mailMessage = new SimpleMailMessage();
         mailMessage.setFrom("nihalwanjari321@gmail.com");
         mailMessage.setSubject(mailStructure.getSubject());
         mailMessage.setText(mailStructure.getBody());
         mailMessage.setTo(mail);
         
         System.out.println("Mail Send");
         
         
         javaMailSender.send(mailMessage);
         
         
         
     }
     
}   
