package com.jovandjumic.isap_travel_experiences_app.controllers;

import com.jovandjumic.isap_travel_experiences_app.entities.Comment;
import com.jovandjumic.isap_travel_experiences_app.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/{experienceId}")
    public ResponseEntity<Comment> addComment(@PathVariable Long experienceId, @RequestBody Comment comment, @RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);  // Dohvati userId putem tokena
        Comment createdComment = commentService.addComment(experienceId, comment, userId);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        Optional<Comment> comment = commentService.getCommentById(id);
        return comment.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Map<String, String> payload, @RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);  // Dohvati userId putem tokena
        String content = payload.get("content");  // Uzimamo "content" iz JSON-a
        Comment updatedComment = commentService.updateComment(id, content, userId);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);  // Dohvati userId putem tokena
        commentService.deleteComment(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Long getUserIdFromToken(String token) {
        String url = "http://3.85.169.58:8080/api/auth/me";  // Poziv prema user-service
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);  // Dodaj token u Authorization zaglavlje
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        Map<String, Object> responseBody = response.getBody();

        return Long.valueOf(responseBody.get("id").toString());  // Pretpostavka je da vraća userId ili id
    }

}
