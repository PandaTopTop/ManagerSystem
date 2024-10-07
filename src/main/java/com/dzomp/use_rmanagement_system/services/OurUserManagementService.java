package com.dzomp.use_rmanagement_system.services;

import com.dzomp.use_rmanagement_system.dto.ReqResp;
import com.dzomp.use_rmanagement_system.entities.OurUser;
import com.dzomp.use_rmanagement_system.repositories.OurUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class OurUserManagementService {
    @Autowired
    private OurUserRepository ourUserRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ReqResp registerUser(ReqResp registrationRequest) {

        ReqResp resp = new ReqResp();



        try {

            OurUser ourUser = new OurUser();
            ourUser.setEmail(registrationRequest.getEmail());
            ourUser.setCity(registrationRequest.getCity());
            ourUser.setRole(registrationRequest.getRole());
            ourUser.setName(registrationRequest.getName());
            ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

            OurUser ourUserResult = ourUserRepository.save(ourUser);
            if(ourUserResult.getId() > 0){
                resp.setOurUser(ourUserResult);
                resp.setMessage("User registered successfully");
                resp.setStatusCode(200);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setMessage(e.getMessage());
        }
        return resp;
    }


    public ReqResp login(ReqResp loginRequest){
        ReqResp response = new ReqResp();

        try{
 authenticationManager.
         authenticate(new UsernamePasswordAuthenticationToken
                 (loginRequest.getEmail(), loginRequest.getPassword()));

            var user = ourUserRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully logged in");
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public  ReqResp refreshToken(ReqResp  refreshRequest){

        ReqResp response = new ReqResp();

        try{
            String ourEmail = jwtUtils.extractUsername(refreshRequest.getToken());//*
            OurUser ourUser = ourUserRepository.findByEmail(ourEmail).orElseThrow();

            if(jwtUtils.isTokenValid(refreshRequest.getToken(),ourUser)){
                var jwt = jwtUtils.generateToken(ourUser);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshRequest.getRefreshToken());///*
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully refreshed Token");

            }

        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());

        }
        return response;
    }

    public  ReqResp getAllUsers(){

        ReqResp response = new ReqResp();

        try{

            List<OurUser> result = ourUserRepository.findAll();

            if(!result.isEmpty()){
                response.setOurUserList(result);
                response.setStatusCode(200);
                response.setMessage("Successful");
            } else {
                response.setStatusCode(404);
                response.setMessage("No users found");
            }

        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occurred: "+ e.getMessage());

        }

        return response;
    }

    public ReqResp getUserById(Integer id){
            ReqResp response = new ReqResp();

            try{
                OurUser userById = ourUserRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("User not found"));
                response.setOurUser(userById);
                response.setStatusCode(200);
                response.setMessage("User with id "+ id +"  found successfully");
            } catch (Exception e){
                response.setStatusCode(500);
                response.setMessage("Error occurred: "+ e.getMessage());
            }

            return response;
    }

    public ReqResp deleteUser(Integer userId){
        ReqResp response = new ReqResp();

        try{
            Optional<OurUser> userOptional = ourUserRepository.findById(userId);
            if(userOptional.isPresent()){
                ourUserRepository.deleteById(userId);
                response.setStatusCode(200);
                response.setMessage("User deleted successfully");
            }
            else {
                response.setStatusCode(404);
                response.setMessage("User not found for delete");
            }
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occurred while deleting user");
        }
        return  response;
    }

    public ReqResp updateUser(Integer userId, OurUser updateUser){
        ReqResp response = new ReqResp();
        try {
            Optional<OurUser> userOptional = ourUserRepository.findById(userId);
            if(userOptional.isPresent()){
                OurUser existingUser = userOptional.get();
                existingUser.setEmail(updateUser.getEmail());
                existingUser.setName(updateUser.getName());
                existingUser.setCity(updateUser.getCity());
                existingUser.setRole(updateUser.getRole());
                 //Check if password is present in the request
                if(updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()){
                    // Encode the password and update
                    existingUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));

                }

                OurUser saveUser = ourUserRepository.save(existingUser);
                response.setOurUser(saveUser);
                response.setStatusCode(200);
                response.setMessage("User updated successfully");
            } else {
                response.setStatusCode(404);
                response.setMessage("User not found for update");
            }

            } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occurred while updating");
        }
        return  response;
    }


    public ReqResp getMyInfo(String email) {
        ReqResp response = new ReqResp();

        try {
            Optional<OurUser> userOptional = ourUserRepository.findByEmail(email);

            if (userOptional.isPresent()) {
                response.setOurUser(userOptional.get());
                response.setStatusCode(200);
                response.setMessage("Successful");
            } else {
                response.setStatusCode(404);
                response.setMessage("User not found for upd");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while getting");
        }
        return response;
    }

}





