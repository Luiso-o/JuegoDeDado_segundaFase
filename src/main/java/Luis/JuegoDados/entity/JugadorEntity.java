package Luis.JuegoDados.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "jugador")
public class JugadorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    @Column(name = "nombre_jugador")
    private String nombre;

    @Column(name = "porcentaje_exito %")
    private int porcentajeExito;

}
