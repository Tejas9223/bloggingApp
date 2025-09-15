package com.codewithdurgesh.services;

import java.util.List;

import com.codewithdurgesh.payloads.PostDto;
import com.codewithdurgesh.payloads.PostResponse;

public interface PostService {
	
	//create 
	PostDto createPost(PostDto postDto,Integer userId,Integer categoryId);
	
	//update
	PostDto updatePost(PostDto postDto,Integer postId);
	
	//delete
	void deletePost(Integer postId);
	
	//get All Posts
	PostResponse getAllPost(Integer pageNumber,Integer pageSize,String sortBy, String sortDir);
	
	//get Single Post
	PostDto getPostById(Integer postId);
	
	//get all posts by Category
	List<PostDto> getPostsByCategory(Integer categoryId);
	
	//get all posts by User
	List<PostDto> getPostsByUser(Integer userId); 
	
	//search Posts
	List<PostDto> searchPosts(String Keyword);
}
