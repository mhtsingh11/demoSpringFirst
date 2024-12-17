package com.boot.first.demo.controlers;

import com.boot.first.demo.entity.UserEntity;
import com.boot.first.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllUsers() {
        List<UserEntity> userEntities = userService.findAll();
        if (userEntities != null) {
            return ResponseEntity.ok(userEntities);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        try {
            userService.save(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        Optional<UserEntity> userEntity = userService.findByName(username);
        if (userEntity.isPresent()) {
            return new ResponseEntity<>(userEntity.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{userName}")
    public ResponseEntity<?> updateUser(@PathVariable String userName, @RequestBody UserEntity user) {
        Optional<UserEntity> old = userService.findByName(userName);
        if (old != null) {
            old.get().setUsername(user.getUsername());
            old.get().setPassword(user.getPassword());
            userService.save(old.get());
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
