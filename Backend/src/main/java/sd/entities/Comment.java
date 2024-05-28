package sd.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "extracted_id")
    @JsonIgnore
    private ExtractedRecipe extractedRecipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_id")
    @JsonIgnore
    private AddedRecipe addedRecipe;


}

