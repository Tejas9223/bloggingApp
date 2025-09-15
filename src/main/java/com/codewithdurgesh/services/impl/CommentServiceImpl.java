package com.codewithdurgesh.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codewithdurgesh.entities.Comment;
import com.codewithdurgesh.entities.Post;
import com.codewithdurgesh.exceptions.ResourceNotFoundException;
import com.codewithdurgesh.payloads.CommentDto;
import com.codewithdurgesh.repositories.CommentRepo;
import com.codewithdurgesh.repositories.PostRepo;
import com.codewithdurgesh.services.CommentService;


@Service 
public class CommentServiceImpl implements CommentService {

	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private CommentRepo commentRepo; 
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId) {
		
		// TODO Auto-generated method stub 
		Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "post id", postId));
		
		Comment comment = this.modelMapper.map(commentDto,Comment.class);
		comment.setPost(post);
		Comment savedComment = this.commentRepo.save(comment);
		
		return this.modelMapper.map(savedComment, CommentDto.class );
	}

	@Override
	public void deleteComment(Integer commentId) {
		
		// TODO Auto-generated method stub 
		Comment comments = this.commentRepo.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment", "comment id", commentId));
		
		this.commentRepo.delete(comments);
	}

}
