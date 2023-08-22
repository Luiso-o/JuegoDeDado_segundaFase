package Luis.JuegoDados.excepciones;

public class PlayerNotFoundException extends RuntimeException{
    public PlayerNotFoundException(long id){
        super("No se encontró el jugador con el ID: " + id);
    }
}
