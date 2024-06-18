package sd.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "premium_users")
public class PremiumUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}

