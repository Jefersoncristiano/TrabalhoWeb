package Exception;


import jakarta.xml.ws.WebFault;

@WebFault
public class BussinesException extends RuntimeException {
    public BussinesException(String message) {
        super(message);
    }
}
