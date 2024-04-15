package com.example.survey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.survey.entity.Login;

@Repository
public interface LoginDao extends JpaRepository<Login, String> {
	
}
