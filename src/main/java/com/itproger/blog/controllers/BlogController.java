package com.itproger.blog.controllers;

import com.itproger.blog.models.Post;
import com.itproger.blog.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class BlogController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/blog")
    public String blogMain(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model) {
        return "blog-add";
    }

    @PostMapping("/blog/add")
    public String blogPostAdd(@RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model) {
        Post post = new Post(title, anons, full_text);
        postRepository.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable(value = "id") long postId, Model model) {
        if (!postRepository.existsById(postId)) {
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(postId);
        ArrayList<Post> postArrayList = new ArrayList<>();
        post.ifPresent(postArrayList :: add);
        model.addAttribute("post", postArrayList);
        return "blog-details";
    }

    @GetMapping("/blog/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") long postId, Model model) {
        if (!postRepository.existsById(postId)) {
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(postId);
        ArrayList<Post> postArrayList = new ArrayList<>();
        post.ifPresent(postArrayList :: add);
        model.addAttribute("post", postArrayList);
        return "blog-edit";
    }

    @PostMapping("/blog/{id}/edit")
    public String blogPostEdit(@RequestParam String title, @RequestParam String anons, @RequestParam String full_text, @PathVariable(value = "id") long postId, Model model) {
        Post post = postRepository.findById(postId).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        postRepository.save(post);
        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/remove")
    public String blogPostRemove(@PathVariable(value = "id") long postId, Model model) {
        Post post = postRepository.findById(postId).orElseThrow();
        postRepository.delete(post);
        return "redirect:/blog";
    }

}
