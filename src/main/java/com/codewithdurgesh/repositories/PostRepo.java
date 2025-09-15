package com.codewithdurgesh.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codewithdurgesh.entities.Category;
import com.codewithdurgesh.entities.Post;
import com.codewithdurgesh.entities.User;

public interface PostRepo extends JpaRepository<Post, Integer>{
	
	List<Post> findAllByUser(User user);
	List<Post> findByCategory(Category category);
	@Query("select p from Post p where p.title like :key")
	List<Post> findByTitleContaining(@Param("key") String keyword);
}
