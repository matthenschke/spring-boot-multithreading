package com.henschke.matthew.spring_boot_multithreading.controllers;

import com.henschke.matthew.spring_boot_multithreading.models.User;
import com.henschke.matthew.spring_boot_multithreading.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {
   private UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity saveUsers(@RequestParam(value = "files") MultipartFile [] files) {
        try {
            for (MultipartFile file : files) {
                service.saveUsers(file);
            }

            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(produces = "application/json")
    public CompletableFuture<ResponseEntity> findAllUsers(){
        return service.findAllUsers().thenApply(ResponseEntity :: ok);
    }

    @GetMapping(path = "/thread", produces = "application/json")
    public ResponseEntity getUsers(){
        // all three tasks of doing the same time with multithreading
        CompletableFuture<List<User>> users1 = service.findAllUsers();
        CompletableFuture<List<User>> users2 = service.findAllUsers();
        CompletableFuture<List<User>> users3 = service.findAllUsers();
        CompletableFuture.allOf(users1, users2, users3).join();

        return ResponseEntity.status(HttpStatus.OK).build();

    }


}
