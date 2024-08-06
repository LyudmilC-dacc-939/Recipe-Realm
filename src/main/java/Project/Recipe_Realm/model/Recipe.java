package Project.Recipe_Realm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "RECIPES")
@Table(name = "recipes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;
    private String title;
    private String description;
    private String ingredients;
    private String category;
    private Instant createdAt;
    private Long likes;
    private Long dislikes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "recipe",
    cascade = CascadeType.ALL,
    fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Comment> comments;

    @ManyToMany(mappedBy = "favorites", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("favorites")
    private Set<User> users = new HashSet<>();

    private Set<Long> usersIdLiked;
    private Set<Long> usersIdDisliked;
}
