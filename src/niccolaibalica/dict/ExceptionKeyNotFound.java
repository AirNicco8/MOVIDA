package src.niccolaibalica.dict;

import src.commons.MovidaFileException;

public class ExceptionKeyNotFound extends RuntimeException {

    public String getMessage() {
        return "Key not found";
    }
}
