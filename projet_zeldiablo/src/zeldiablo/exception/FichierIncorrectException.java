package zeldiablo.exception;

/**
 * Exception levee lorsque le fichier de labyrinthe a un format incorrect.
 */
public class FichierIncorrectException extends RuntimeException {
    /**
     * Cree une nouvelle exception avec le message specifie.
     *
     * @param message le message de l'exception
     */
    public FichierIncorrectException(String message) {
        super(message);
    }
}
