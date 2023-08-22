package Luis.JuegoDados.excepciones;

public class EmptyPlayersListException extends RuntimeException{
    public EmptyPlayersListException(){
        super("Lista de jugadores vacía");
    }
}
