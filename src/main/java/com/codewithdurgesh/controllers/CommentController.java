package com.codewithdurgesh.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithdurgesh.payloads.ApiResponse;
import com.codewithdurgesh.payloads.CommentDto;
import com.codewithdurgesh.services.CommentService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/")
public class CommentController { 
	
	@Autowired
	private CommentService commentService;
	
	// Create comment for a post
	
	@PostMapping("/post/{postId}/comments")
	public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto, @PathVariable Integer postId)
	{ 
		CommentDto createdComment = commentService.createComment(commentDto, postId);
		return new ResponseEntity<>(createdComment,HttpStatus.CREATED);
		
	} 
	
	 // Delete comment by ID
	
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Comment deleted successfully !! ",true), HttpStatus.OK);
    }
}
