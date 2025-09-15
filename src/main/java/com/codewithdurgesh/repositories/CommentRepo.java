package com.codewithdurgesh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithdurgesh.entities.Comment;

public interface CommentRepo extends JpaRepository<Comment, Integer>{ 

}
