package org.isolution.nexus.invoker;

import org.springframework.ws.WebServiceException;

/**
 * User: agwibowo
 * Date: 19/04/11
 * Time: 11:24 PM
 */
public class InvocationException extends WebServiceException {

    public InvocationException(String errorMessage) {
        super(errorMessage);
    }

}
