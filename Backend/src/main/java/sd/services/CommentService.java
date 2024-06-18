package sd.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sd.dtos.ExtractedDTO;
import sd.entities.AddedRecipe;
import sd.entities.Comment;
import sd.entities.ExtractedRecipe;
import sd.repositories.AddedRecipeRepository;
import sd.repositories.CommentRepository;
import sd.repositories.ExtractedRecipeRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentService {

    @Autowired
    private final CommentRepository commentRepository;

    @Autowired
    private final ExtractedRecipeService extractedRecipeService;
    @Autowired
    private final ExtractedRecipeRepository extractedRecipeRepository;

    @Autowired
    private final AddedRecipeRepository addedRecipeRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, ExtractedRecipeService extractedRecipeService, ExtractedRecipeRepository extractedRecipeRepository, AddedRecipeRepository addedRecipeRepository) {
        this.commentRepository = commentRepository;
        this.extractedRecipeService = extractedRecipeService;
        this.extractedRecipeRepository = extractedRecipeRepository;
        this.addedRecipeRepository = addedRecipeRepository;
    }

    public ResponseEntity<Comment> addComment(int recipeId, int userId, String username, String additionalPath, Comment comment) {
        comment.setUserId(userId);
        comment.setUsername(username);

        if(additionalPath.equals("dishgen")) {
            ExtractedRecipe extractedRecipe = extractedRecipeRepository.findById(recipeId).orElse(null);
            comment.setAddedRecipe(null);
            comment.setExtractedRecipe(extractedRecipe);

        } else if (additionalPath.equals("addedRecipe")) {
            AddedRecipe addedRecipe = addedRecipeRepository.findById(recipeId).orElse(null);
            comment.setAddedRecipe(addedRecipe);
            comment.setExtractedRecipe(null);
        } else {
            comment.setAddedRecipe(null);
            comment.setExtractedRecipe(null);
        }

        Comment newComment = commentRepository.save(comment);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

/*
    public List<Comment> getCommentsByRecipeId(int recipeId, String additionalPath) {
        System.out.println("DDDD");
        System.out.println("ID" + recipeId);
        System.out.println("additionalPath" + additionalPath);

        if (additionalPath.equals("dishgen")) {
            ExtractedDTO extractedDTO = extractedRecipeService.getDetailDisghen(recipeId);
            System.out.println("Extracted DTO: " + extractedDTO);

            if (extractedDTO != null) {
                return commentRepository.findByExtractedRecipe_Id(extractedDTO.getId());
            } else {
                // Handle the case where the recipe is not found
                return Collections.emptyList(); // or return null or another appropriate value
            }
        } else {
            // Handle the case where additionalPath is not "dishgen"
            return Collections.emptyList(); // or return null or another appropriate value
        }
        return null;
    }*/

    @Transactional
    public List<Comment> getCommentsByRecipeId(String username, int recipeId, String additionalPath) {
        List<Comment> comments;
        if (additionalPath.equals("dishgen")) {
            Optional<ExtractedRecipe> extractedRecipeOptional = extractedRecipeRepository.findById(recipeId);
            if (extractedRecipeOptional.isPresent()) {
                ExtractedRecipe extractedRecipe = extractedRecipeOptional.get();
                Hibernate.initialize(extractedRecipe.getComments());
                comments = extractedRecipe.getComments();
            } else {
                return null;
            }
        } else if (additionalPath.equals("added")) {
            Optional<AddedRecipe> addedRecipeOptional = addedRecipeRepository.findById(recipeId);
            if (addedRecipeOptional.isPresent()) {
                AddedRecipe addedRecipe = addedRecipeOptional.get();
                Hibernate.initialize(addedRecipe.getComments());
                comments = addedRecipe.getComments();
            } else {
                return null;
            }
        } else {
            return null;
        }
        return comments;
    }

}
