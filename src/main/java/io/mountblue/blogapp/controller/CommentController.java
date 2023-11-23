package io.mountblue.blogapp.controller;

import io.mountblue.blogapp.entity.Comment;
import io.mountblue.blogapp.entity.Post;
import io.mountblue.blogapp.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommentController {
    private final CommentService commentService;

    @Autowired
    CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping("/saveComment/{postId}")
    public String saveComment(@Valid @ModelAttribute("com") Comment comment, @PathVariable("postId") int postId){
            commentService.saveComment(comment, postId);
            return "redirect:/viewPost/"+postId;
    }

    @GetMapping("/editComment/{postId}/{commentId}")
    public String editComment(Model model, @PathVariable("commentId") int commentId,
                              @PathVariable("postId") int postId){
        Comment comment = commentService.getCommentById(commentId);
        model.addAttribute("com",comment);
        model.addAttribute("postId",postId);
        return "editComment";
    }

    @PostMapping("/updateComment/{postId}/{commentId}")
    public String updateComment(@Valid @ModelAttribute("com") Comment comment, @PathVariable("postId")int postId, @PathVariable("commentId") int commentId){
            comment.setId(commentId);
            commentService.updateComment(comment, postId);
            return "redirect:/viewPost/"+postId;
    }

    @GetMapping("/deleteComment/{postId}/{commentId}")
    public String deleteComment(@PathVariable("postId") int postId, @PathVariable("commentId") int commentId){
        commentService.deleteCommentById(commentId);
        return "redirect:/viewPost/"+postId;
    }

}
