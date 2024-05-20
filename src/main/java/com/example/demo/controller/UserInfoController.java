/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.controller;

import com.example.demo.model.CustommerInfo;
import com.example.demo.model.MailStructure;
import com.example.demo.service.UserInfoService;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.UnknownHostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HP
 */
@CrossOrigin(origins = {"http://localhost:5173",})
@RestController
@RequestMapping("user")
public class UserInfoController {
    
    // Hello Nice To Meet YOu 

    public UserInfoController() {
        System.out.println("I'm in Controller");
    }

    @Autowired
    private UserInfoService infoService;

    @PostMapping("/save")
    private ResponseEntity<Object> saveRecord(@RequestBody CustommerInfo custommerInfo) {
        String email = custommerInfo.getEmail();
        MailStructure mailStructure = new MailStructure();

        try {
            boolean validateEmail = infoService.validateEmail(email);
            if (!validateEmail) {
                CustommerInfo savedRecord = infoService.saveRecords(custommerInfo);
                infoService.sendMail(email, mailStructure);
                return ResponseEntity.ok().body(savedRecord); // Return the saved object with OK status
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Record Already Exist"); // Return 409 for conflict (duplicate record)
            }
        } catch (Exception e) {
            // Handle the exception (e.g., log it)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving data");
        }
    }

    @PostMapping("/validate")
    public boolean validateEmail(@RequestBody CustommerInfo custommerInfo) {
        String email = custommerInfo.getEmail();
        if (infoService.validateEmail(email)) {
            return true;
        } else {

            return false;
        }
    }

    @PutMapping("/update")
    public String updateCustomer(@RequestBody CustommerInfo customer) {
        boolean success = infoService.updateCustomerByEmail(customer.getEmail(), customer);
        if (success) {
            return "Customer Data updated successfully";
        } else {
            return "Customer with email " + customer.getEmail() + " not found";
        }
    }

    @PostMapping("/send/{mail}")
    public String sendMail(@PathVariable String mail, @RequestBody MailStructure mailStructure) {
        infoService.sendMail(mail, mailStructure);
        return "Sucessfully Mail Send";

    }

    @PostMapping("/login")
    public ResponseEntity<Object> verifyLoginCredentials(@RequestBody CustommerInfo custommerInfo) {
        String email = custommerInfo.getEmail();
        String password = custommerInfo.getPassword();
        boolean isValidCredentials = infoService.verifyLoginCredentials(email, password);

        if (isValidCredentials) {
            // If credentials are valid, retrieve the CustommerInfo object
            CustommerInfo objecctByEmail = infoService.getObjecctByEmail(email);
            // Return the CustommerInfo object along with OK status
            return ResponseEntity.ok(objecctByEmail);
        } else {
            // If credentials are invalid, return 401 Unauthorized status
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/checkDomain/{domainName}")
    public String checkDomainAvailability(@PathVariable String domainName) {
        try {
            // Perform DNS lookup
            InetAddress address = InetAddress.getByName(domainName);

            // If the address is reachable, the domain is available
            boolean isAvailable = address.isReachable(3000); // Timeout in milliseconds

            return "Domain " + domainName + " is not available";
        } catch (UnknownHostException e) {
            // If the domain cannot be resolved, it is not available
            return "Domain " + domainName + " is available: ";
        } catch (IOException e) {
// Other exceptions might occur due to network issues or other reasons
            return "Domain " + domainName + " is available: ";

        }
    }

}
