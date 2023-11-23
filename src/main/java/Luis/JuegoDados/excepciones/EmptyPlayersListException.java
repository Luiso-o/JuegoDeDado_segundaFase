package Luis.JuegoDados.excepciones;

public class EmptyPlayersListException extends RuntimeException{
    public EmptyPlayersListException(String message){
        super(message);
    }
}
