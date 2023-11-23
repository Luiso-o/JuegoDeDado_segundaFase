package Luis.JuegoDados.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Información de una partida")
@Table(name = "Partidas")
public class PartidaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_partida" ,unique = true)
    private Long id;

    @Column(name = "Fecha partida")
    private LocalDate fecha;

    @Column(name = "Victorias")
    private int victorias;

    @Column(name = "Derrotas")
    private int derrotas;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "jugador_id")
    private JugadorEntity jugador;

}
