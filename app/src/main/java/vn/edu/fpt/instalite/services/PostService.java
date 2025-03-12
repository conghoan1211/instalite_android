package vn.edu.fpt.instalite.services;

import android.content.Context;

import java.util.List;

import vn.edu.fpt.instalite.dto.PostDTO;
import vn.edu.fpt.instalite.dto.UploadPostDTO;
import vn.edu.fpt.instalite.models.Post;
import vn.edu.fpt.instalite.repositories.PostRepository;

public class PostService {
    private PostRepository postRepository;

    public PostService(Context context) {
        postRepository = new PostRepository(context);
    }

    public boolean insertPost(UploadPostDTO post) {
        if (post.getUserId() == null) {
            throw new IllegalArgumentException("Input userId is not valid");
        }
        if (post.getContent() == null || post.getContent().isEmpty()) {
            throw new IllegalArgumentException("Input caption is not valid");
        }
        if (post.getImageUrl() == null || post.getImageUrl().isEmpty() || post.getImageUrl().length() <= 0) {
            throw new IllegalArgumentException("Input image is not valid");
        }
        return postRepository.addPost(post);
    }

    public List<PostDTO> getAllPosts() {
        return postRepository.getAllPosts();
    }

    public boolean deletePost(String postId) {
        if (postId == null || postId.isEmpty()) {
            throw new IllegalArgumentException("Input postId is not valid");
        }
       return postRepository.deletePost(postId);
    }

    public PostDTO getPostById(String postId) {
        PostDTO postDTO = null;
        if (postId == null || postId.isEmpty()) {
            throw new IllegalArgumentException("Input postId is not valid");
        }
        return postRepository.getPostById(postId);
    }

    public List<PostDTO> getPostsByUserId(String userId) {
        List<PostDTO>  postDTO = null;
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Input userId is not valid");
        }
        return postRepository.getPostsByUserId(userId);
    }
}