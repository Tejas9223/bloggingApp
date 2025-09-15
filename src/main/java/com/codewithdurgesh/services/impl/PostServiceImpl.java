package com.codewithdurgesh.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.codewithdurgesh.entities.Category;
import com.codewithdurgesh.entities.Post;
import com.codewithdurgesh.entities.User;
import com.codewithdurgesh.exceptions.ResourceNotFoundException;
import com.codewithdurgesh.payloads.PostDto;
import com.codewithdurgesh.payloads.PostResponse;
import com.codewithdurgesh.repositories.CategoryRepo;
import com.codewithdurgesh.repositories.PostRepo;
import com.codewithdurgesh.repositories.UserRepo;
import com.codewithdurgesh.services.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;
	@Autowired
	private ModelMapper modelMapper; 
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private CategoryRepo categoryRepo;
	
	//create 
	@Override
	public PostDto createPost(PostDto postDto,Integer userId,Integer categoryId) {
		
		User user = this.userRepo.findById(userId)
				.orElseThrow(()-> new ResourceNotFoundException("User","User id", userId));
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(()-> new ResourceNotFoundException("Catgory", "Category id", categoryId));
		
		Post post = this.modelMapper.map(postDto,Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category); 
		
		Post newPost = this.postRepo.save(post);
		return this.modelMapper.map(newPost,PostDto.class);
	}
    
	//update
	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		
		Post post = this.postRepo.findById(postId)
				.orElseThrow(()->new ResourceNotFoundException("Post", "post id", postId));
		
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		
		Post updatedPost = this.postRepo.save(post); 
		
		return this.modelMapper.map(updatedPost,PostDto.class);
	}

	//delete
	@Override
	public void deletePost(Integer postId)  {
		
		Post post = this.postRepo.findById(postId)
				.orElseThrow(()->new ResourceNotFoundException("Post", "post id", postId));
		
		this.postRepo.delete(post);
	}

	//getAll 
	@Override
	public PostResponse getAllPost(Integer pageNumber,Integer pageSize,String sortBy, String sortDir) {
		
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
		Page<Post> pagePost = this.postRepo.findAll(pageable);
		
		List<Post> allPosts = pagePost.getContent();
		
		List<PostDto> postDtos = allPosts.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		 PostResponse postResponse = new PostResponse();
		    postResponse.setContent(postDtos);
		    postResponse.setPageNumber(pagePost.getNumber());
		    postResponse.setPageSize(pagePost.getSize());
		    postResponse.setTotalElements(pagePost.getTotalElements());
		    postResponse.setTotalPages(pagePost.getTotalPages());
		    postResponse.setLastPage(pagePost.isLast());

		    return postResponse;
	}

	//getPost by Id
	@Override
	public PostDto getPostById(Integer postId) {
		
		Post post = this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","post id", postId));
		
		return this.modelMapper.map(post,PostDto.class);
	}

	//getPost by Category
	@Override
	public List<PostDto> getPostsByCategory(Integer categoryId) {
		
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(()-> new ResourceNotFoundException("Category","category id", categoryId));
		
		List<Post> posts = this.postRepo.findByCategory(category);  
		
		List<PostDto> postDtos = posts.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		return postDtos;
	}

	//getPost By User
	@Override
	public List<PostDto> getPostsByUser(Integer userId) {
		
		User user = this.userRepo.findById(userId)
				.orElseThrow(()-> new ResourceNotFoundException("User","userId", userId));
		
		List<Post> posts= this.postRepo.findAllByUser(user);
		
		List<PostDto> postDtos = posts.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		return postDtos;
	}

	//Search Post
	@Override
	public List<PostDto> searchPosts(String Keyword) {
		
		List<Post> posts = this.postRepo.findByTitleContaining("%"+Keyword+"%");
	    return posts.stream()
	            .map(post -> this.modelMapper.map(post, PostDto.class))
	            .collect(Collectors.toList());
	}

}
