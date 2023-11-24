package Luis.JuegoDados.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "jugador")
public class JugadorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;

    @Column(name = "nombre_jugador")
    private String nombreJugador;

    @Column(name = "porcentaje_exito_%")
    private int porcentajeExito;

}
