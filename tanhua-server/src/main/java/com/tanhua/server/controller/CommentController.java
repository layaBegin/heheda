package com.tanhua.server.controller;

import com.tanhua.domain.mongo.vo.CommentVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.server.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<Object> getCommentList(
            String movementId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pagesize){
        PageResult pageResult = commentService.getCommentPage(movementId, page, pagesize);

        return ResponseEntity.ok(pageResult);
    }

    @PostMapping
    public ResponseEntity<Object> commitComment(@RequestBody Map<String,String> paramMap){
        String movementId = paramMap.get("movementId");
        String comment = paramMap.get("comment");
        return commentService.commitComment(movementId,comment);
    }

    ///comments/:id/like
    @GetMapping("/{id}/like")
    public ResponseEntity<Object> likeComment(@PathVariable("id") String id){
        return commentService.likeComment(id);
    }

    @GetMapping("/{id}/dislike")
    public ResponseEntity<Object> dislikeComment(@PathVariable("id") String id){

        return commentService.dislikeComment(id);
    }
}
