package com.jovandjumic.isap_travel_experiences_app.services;

import com.jovandjumic.isap_travel_experiences_app.entities.Comment;
import com.jovandjumic.isap_travel_experiences_app.entities.Experience;
import com.jovandjumic.isap_travel_experiences_app.repositories.CommentRepository;
import com.jovandjumic.isap_travel_experiences_app.repositories.ExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private RestTemplate restTemplate; // Dodaj RestTemplate za komunikaciju sa user-service


    public Comment addComment(Long experienceId, Comment comment, Long userId) {
        Experience experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new IllegalArgumentException("Experience not found"));

        comment.setUserId(userId);
        comment.setCommentDate(new Date());
        comment.setExperience(experience);

        experience.getComments().add(comment);

        return commentRepository.save(comment);
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public Comment updateComment(Long id, String content, Long userId) {
        return commentRepository.findById(id).map(existingComment -> {
            if (!existingComment.getUserId().equals(userId)) {
                throw new AccessDeniedException("You do not have permission to update this comment");
            }
            existingComment.setContent(content);
            return commentRepository.save(existingComment);
        }).orElseThrow(() -> new IllegalArgumentException("Comment not found"));
    }

    public void deleteComment(Long id, Long userId) {
        commentRepository.findById(id).ifPresent(comment -> {
            if (!comment.getUserId().equals(userId)) {
                throw new AccessDeniedException("You do not have permission to delete this comment");
            }
            commentRepository.deleteById(id);
        });
    }
}
