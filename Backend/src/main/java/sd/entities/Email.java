package sd.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "email")
public class Email implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "descriere", nullable = false)
    private String descriere;

    @Column(name = "subiect", nullable = false)
    private String subiect;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user_From", nullable = false)
    private User utilizator;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user_To", nullable = false)
    private User utilizator1;

}
