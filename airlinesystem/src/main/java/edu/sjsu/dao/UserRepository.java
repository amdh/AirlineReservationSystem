package edu.sjsu.dao;

import org.springframework.data.repository.CrudRepository;

import edu.sjsu.compe275.lab2.User;

public interface UserRepository extends CrudRepository<User, Long> {

}