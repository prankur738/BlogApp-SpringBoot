package io.mountblue.blogapp.service;

import io.mountblue.blogapp.entity.Post;
import io.mountblue.blogapp.entity.Tag;
import io.mountblue.blogapp.entity.User;
import io.mountblue.blogapp.repository.PostRepository;
import io.mountblue.blogapp.repository.TagRepository;
import io.mountblue.blogapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    @Autowired
    PostServiceImpl(PostRepository postRepository, TagRepository tagRepository, UserRepository userRepository){
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<Post> getPosts(List<String> authorList, List<String> tagList,
                               String search, String sortField,
                               String order, Pageable pageable){
        Page<Post> posts = postRepository.findAll(pageable);

        if(!search.isEmpty()){
            posts = postRepository.getPostsBySearch(search, pageable);
        }
        else if(authorList.isEmpty() && tagList.isEmpty()){

                if(sortField.equals("author") && order.equals("asc")){
                    return postRepository.findAllByOrderByAuthorAsc(pageable);
                }
                else if(sortField.equals("author") && order.equals("desc")){
                    return postRepository.findAllByOrderByAuthorDesc(pageable);
                }
                else if(sortField.equals("publishedAt") && order.equals("asc")){
                    return postRepository.findAllByOrderByPublishedAtAsc(pageable);
                }
                else if(sortField.equals("publishedAt") && order.equals("desc")){
                    return postRepository.findAllByOrderByPublishedAtDesc(pageable);
                }
        }
        else{
            authorList = authorList.isEmpty() ? null : authorList;
            tagList = tagList.isEmpty() ? null : tagList;
            return postRepository.findByAuthorInAndTags_NameIn(authorList, tagList, sortField, order, pageable);
        }

        return  posts;
    }


    @Override
    public void savePost(Post post, String tagString) {
        Set<Tag> tags = getTagsFromString(tagString);
        post.setTags(tags);
        User user = userRepository.findByUsername(post.getAuthor());
        post.setUser(user);

        postRepository.save(post);
    }

    @Override
    public void updatePost(Post newPost, int postId, String tagString) {
        Post oldPost = findPostById(postId);
        Set<Tag> tags = getTagsFromString(tagString);

        newPost.setId(postId);
        newPost.setTags(tags);
        newPost.setComments(oldPost.getComments());
        newPost.setCreatedAt(oldPost.getCreatedAt());
        newPost.setPublishedAt(oldPost.getPublishedAt());
        newPost.setPublished(oldPost.isPublished());

        postRepository.save(newPost);
    }

    @Override
    public Post findPostById(int id) {
        Optional<Post> postOptional = postRepository.findById(id);
        return postOptional.orElse(null);
    }

    @Override
    public String getCommaSeperatedTags(int id) {
        Post post = findPostById(id);
        String tagString = "";

        for(Tag tag : post.getTags()){
            tagString += tag.getName()+", ";
        }
        return tagString;
    }

    @Override
    public void deletePostById(int id) {
        postRepository.deleteById(id);
    }
    private Set<Tag> getTagsFromString(String tagString){
        Set<Tag> tags = new HashSet<>();

        if(tagString != null){
            String[] tagNames = tagString.split(",");

            for (String tagName : tagNames) {

                String tag = tagName.trim();
                Optional<Tag> tagOptional = tagRepository.findByName(tag);

                if(tagOptional.isPresent()){
                    tags.add(tagOptional.get());
                }
                else{
                    Tag newTag = new Tag(tag);
                    tagRepository.save(newTag);
                    tags.add(newTag);
                }
            }
        }
        return tags;
    }
    @Override
    public Set<String> findDistinctAuthors() {

        List<Post> posts = postRepository.findAll();
        Set<String> authors = new HashSet<>();

        for(Post post : posts){
            authors.add(post.getAuthor());
        }

        return authors;
    }
}

