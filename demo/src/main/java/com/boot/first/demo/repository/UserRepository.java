package com.boot.first.demo.repository;

import com.boot.first.demo.entity.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, ObjectId> {

    Optional<UserEntity> findByUsername(String username);
}
