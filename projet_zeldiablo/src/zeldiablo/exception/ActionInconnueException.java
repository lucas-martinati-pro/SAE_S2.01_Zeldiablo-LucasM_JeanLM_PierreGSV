package zeldiablo.exception;

/**
 * Exception levee lorsqu'une action ou une commande n'est pas reconnue ou est impossible.
 */
public class ActionInconnueException extends RuntimeException {
    /**
     * Cree une nouvelle exception avec le message specifie.
     *
     * @param message le message de l'exception
     */
    public ActionInconnueException(String message) {
        super(message);
    }
}
