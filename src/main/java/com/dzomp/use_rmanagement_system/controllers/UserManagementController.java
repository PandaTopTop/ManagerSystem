package com.dzomp.use_rmanagement_system.controllers;

import com.dzomp.use_rmanagement_system.dto.OurUserDTO;
import com.dzomp.use_rmanagement_system.entities.OurUser;
import com.dzomp.use_rmanagement_system.services.OurUserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserManagementController {

    @Autowired
    OurUserManagementService userManagementService;

    @PostMapping("/auth/register")
    public ResponseEntity<OurUserDTO> register(@RequestBody OurUserDTO newUser){
        return ResponseEntity.ok( userManagementService.registerUser(newUser));
    }


    @PostMapping("/auth/login")
    public ResponseEntity<OurUserDTO> login(@RequestBody OurUserDTO loginUser){

        return  ResponseEntity.ok(userManagementService.login(loginUser));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<OurUserDTO> refreshToken(@RequestBody OurUserDTO refreshToken){
        return ResponseEntity.ok(userManagementService.refreshToken(refreshToken));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<OurUserDTO> getAllUsers(){
        return ResponseEntity.ok(userManagementService.getAllUsers());
    }

    @GetMapping("/admin/get-user/{id}")
    public ResponseEntity<OurUserDTO> getUser(@PathVariable Integer id){
        return ResponseEntity.ok(userManagementService.getUserById(id));
    }
}
