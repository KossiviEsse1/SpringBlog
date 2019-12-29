package org.koko.kokoprojectone.controllers;


import org.koko.kokoprojectone.models.*;
import org.koko.kokoprojectone.models.data.AnimePostDao;
import org.koko.kokoprojectone.models.data.CommentsDao;
import org.koko.kokoprojectone.models.data.LikesDao;
import org.koko.kokoprojectone.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    AnimePostDao animePostDao;

    @Autowired
    CommentsDao commentsDao;

    @Autowired
    UserDao userDao;

    @Autowired
    LikesDao likesDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String homePage(Model model){
        model.addAttribute("title", "Home");
        model.addAttribute("posts", animePostDao.findAll());
        return "pages/index";
    }

    @RequestMapping(value="add", method = RequestMethod.GET)
    public String displayAddPostForm(Model model){
        model.addAttribute("title", "Add Anime Post");
        model.addAttribute("anime", new AnimePost());
        model.addAttribute("titleerrors", "");
        model.addAttribute("descriptionerrors", "");
        model.addAttribute("snippeterrors", "");
        return "pages/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    //@ModelAttribute @Valid AnimePost newAnime,
    public String processAddPostForm(
            @RequestParam(value="title") String title,
            @RequestParam(value="description") String description,
            @RequestParam(value="postImage") MultipartFile file,
            @RequestParam(value="snippet") String snippet,
            Model model) throws IOException {
        String titleerrors = "";
        String descriptionerrors = "";
        String snippeterrors = "";
        int count=0;
        if (title.length() > 25 || title.length() < 3) {
            titleerrors = "Title must be between 3 and 25 Characters long";
            count++;
        }
        if (description.length() < 1 || description.length() > 100000) {
            descriptionerrors = "Description must be between 1 and 100000 Characters";
            count++;
        }
        if (snippet.length() > 50) {
            snippeterrors = "Must be less than 50 Characters";
            count++;
        }
        model.addAttribute("titleerrors", titleerrors);
        model.addAttribute("descriptionerrors", descriptionerrors);
        model.addAttribute("snippeterrors", snippeterrors);
        model.addAttribute("title", "Add Anime Post");
        if(count>0){
            AnimePost anime = new AnimePost();
            anime.setSnippet(snippet);
            anime.setDescription(description);
            anime.setTitle(title);
            model.addAttribute("anime", anime);
            return "pages/add";
        }
        byte [] img = file.getBytes();
        AnimePost newAnime = new AnimePost();
        newAnime.setTitle(title);
        newAnime.setDescription(description);
        newAnime.setPostImage(img);
        newAnime.setSnippet(snippet);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails = (MyUserDetails)authentication.getPrincipal();
        User user = userDao.findByUsername(myUserDetails.getUsername()).get();
        newAnime.setUser(user);
        newAnime.setDate(new Date());
        animePostDao.save(newAnime);
        return "redirect:";
    }

    @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
    public String viewIndividualPost(Model model, @PathVariable int id){
        AnimePost posst = animePostDao.findById(id).get();
        model.addAttribute("post", posst);
        model.addAttribute("comments", commentsDao.findAll());
        return "pages/viewPost";

    }

    @RequestMapping(value="comment", method = RequestMethod.POST)
    public String submitComment(@RequestParam(value="words") String words, @RequestParam(value="post_id") int post_id){
        Comments newComment = new Comments(words);
        newComment.setAnimePost(animePostDao.findById(post_id).get());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails = (MyUserDetails)authentication.getPrincipal();
        User user = userDao.findByUsername(myUserDetails.getUsername()).get();
        newComment.setUser(user);
        commentsDao.save(newComment);
        return "redirect:/view/"+post_id;

    }

    @RequestMapping(value="register", method = RequestMethod.GET)
    public String displayRegisterForm(Model model){
        model.addAttribute("title", "Register");
        model.addAttribute("user", new User());
        return "users/add";
    }

    @RequestMapping(value="register", method = RequestMethod.POST)
    public String processRegisterForm(@ModelAttribute @Valid User newUser, Errors errors, String verify, Model model){
        if(errors.hasErrors() || !verify.equals(newUser.getPassword())){
            model.addAttribute("userr", newUser);
            return "users/add";
        }
        else if(userDao.findByUsername(newUser.getUsername()).isPresent()){
            model.addAttribute("existerror", "User Already Exists");
            return "users/add";
        }
        else{
            String hashedPassword = passwordEncoder.encode(newUser.getPassword());
            newUser.setPassword(hashedPassword);
            newUser.setActive(true);
            newUser.setRole("ROLE_USER");
            //User newestUser = new User(newUser.getUsername(), hashedPassword, newUser.getEmail(), true, "ROLE_USER");
            userDao.save(newUser);
            return "redirect:/login";
        }
    }

    @RequestMapping(value="login", method = RequestMethod.GET)
        public String loginForm(){
            return "users/login";
        }

    @RequestMapping(value="edit/{postId}", method = RequestMethod.GET)
    public String displayEditForm(Model model, @PathVariable int postId){
        AnimePost thePost = animePostDao.findById(postId).get();
        model.addAttribute("thePost", thePost);
        model.addAttribute(new AnimePost());
        return "pages/edit";
    }

    @RequestMapping(value="edit", method=RequestMethod.POST)
    public String processEditForm(@RequestParam(value="post_id") int post_id, @RequestParam(value="title") String title, @RequestParam(value="description") String description, @RequestParam(value="postImage") MultipartFile file, @RequestParam(value="snippet") String snippet, Model model) throws IOException{
        AnimePost thePost = animePostDao.findById(post_id).get();
        if(!file.isEmpty()){
            byte [] img = file.getBytes();
            thePost.setTitle(title);
            thePost.setDescription(description);
            thePost.setSnippet(snippet);
            thePost.setPostImage(img);
            animePostDao.save(thePost);
        }
        thePost.setTitle(title);
        thePost.setDescription(description);
        thePost.setSnippet(snippet);
        animePostDao.save(thePost);
        return "redirect:";
    }

    @RequestMapping(value="delete/{postId}", method = RequestMethod.GET)
    public String deleteAnimePost(Model model, @PathVariable int postId){
        animePostDao.delete(animePostDao.findById(postId).get());
        model.addAttribute("title", "Home");
        model.addAttribute("posts", animePostDao.findAll());
        return "pages/index";
    }

    @RequestMapping(value="/search", method = RequestMethod.POST)
    public String search(@RequestParam(value="search") String search, Model model){
        List<AnimePost> posts = (List<AnimePost>) animePostDao.findAll();
        List<AnimePost> results = new ArrayList<>();
        for(AnimePost item: posts){
            if(item.getTitle().toLowerCase().contains(search.toLowerCase())){
                results.add(item);
            }
            else if(item.getSnippet().toLowerCase().contains(search.toLowerCase())){
                results.add(item);
            }
            else if(item.getDescription().toLowerCase().contains(search.toLowerCase())){
                results.add(item);
            }
        }
        model.addAttribute("title", "Search");
        search = "'"+search+"'";
        model.addAttribute("what", search);
        model.addAttribute("posts", results);
        return "pages/search";
    }

    @RequestMapping(value="/like", method = RequestMethod.POST)
    public String like(@RequestParam(value="postId") int postId, Model model){
        AnimePost post = animePostDao.findById(postId).get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails = (MyUserDetails)authentication.getPrincipal();
        User user = userDao.findByUsername(myUserDetails.getUsername()).get();

        for(Likes lik : post.getLikes()){
            if(lik.getAnimePost()== post && lik.getUser()==user){
                likesDao.delete(likesDao.findById(lik.getId()).get());
                model.addAttribute("title", "Home");
                model.addAttribute("posts", animePostDao.findAll());
                return "pages/index";
            }
        }

        Likes like = new Likes(user, post);
        likesDao.save(like);
        model.addAttribute("title", "Home");
        model.addAttribute("posts", animePostDao.findAll());
        return "pages/index";
    }




}
