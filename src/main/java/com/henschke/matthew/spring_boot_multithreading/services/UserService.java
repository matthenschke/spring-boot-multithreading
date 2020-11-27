package com.henschke.matthew.spring_boot_multithreading.services;

import com.henschke.matthew.spring_boot_multithreading.models.User;
import com.henschke.matthew.spring_boot_multithreading.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {
    private UserRepository repository;
    static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }


    @Async
    public CompletableFuture<List<User>> saveUsers(MultipartFile file) throws Exception {

        long start = System.currentTimeMillis();
        List<User> users = parseCSVFile(file);
        logger.info("Saving list of size {}", users.size(), "" + Thread.currentThread().getName());
        users = repository.saveAll(users);
        long end = System.currentTimeMillis();
        logger.info("Total Time {}", end - start);
        return CompletableFuture.completedFuture(users);
    }

    @Async
    public CompletableFuture<List<User>> findAllUsers(){
        logger.info("Get list of user by {}", Thread.currentThread().getName());
        List<User> users = repository.findAll();
        return CompletableFuture.completedFuture(users);
    }

    private List<User> parseCSVFile(final MultipartFile file) throws Exception{
        final List<User> users = new ArrayList<User>();
        try{
            final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            while ((line = br.readLine()) != null){
                final String[] data = line.split(",");
                final User user = new User();
                user.setName(data[0]);
                user.setEmail(data[1]);
                user.setGender(data[2]);
                users.add(user);
            }
            return users;
        }
        catch(Exception e){
            logger.error("Failed to parse CSV file {}");
            throw new Exception("Failed to read csv file {}", e);
        }

    }

}
