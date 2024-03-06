package sd.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "love_react")
public class LoveReact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int userId;

    @Column(nullable = false)
    private int recipeId;

    @Column(nullable = false)
    private String name;
}
