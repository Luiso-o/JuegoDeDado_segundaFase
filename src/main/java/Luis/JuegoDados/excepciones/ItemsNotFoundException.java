package Luis.JuegoDados.excepciones;

public class ItemsNotFoundException extends RuntimeException{
    public ItemsNotFoundException(){
        super("El jugador no tiene partidas que eliminar");
    }
}
