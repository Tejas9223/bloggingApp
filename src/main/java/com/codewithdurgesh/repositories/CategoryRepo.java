package com.codewithdurgesh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithdurgesh.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

}
