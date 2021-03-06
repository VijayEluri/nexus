package org.isolution.nexus.routing.impl;

import org.apache.log4j.Logger;
import org.isolution.nexus.domain.Endpoint;
import org.isolution.nexus.domain.Service;
import org.isolution.nexus.domain.ServiceEndpoint;
import org.isolution.nexus.domain.dao.EndpointDAO;
import org.isolution.nexus.domain.dao.ServiceDAO;
import org.isolution.nexus.routing.InactiveServiceException;
import org.isolution.nexus.routing.NoActiveRouteException;
import org.isolution.nexus.routing.ServiceNotFoundException;
import org.isolution.nexus.routing.ServiceRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;


/**
 * User: Alex Wibowo
 * Date: 30/12/10
 * Time: 8:45 PM
 */
@org.springframework.stereotype.Service
public class DatabaseServiceRouter implements ServiceRouter{
    public static final Logger LOGGER = Logger.getLogger(DatabaseServiceRouter.class);

    private EndpointDAO endpointDAO;

    private ServiceDAO serviceDAO;

    @Autowired
    public DatabaseServiceRouter(ServiceDAO serviceDAO, EndpointDAO endpointDAO) {
        this.endpointDAO = endpointDAO;
        this.serviceDAO = serviceDAO;
    }

    @Transactional
    @Override
    public Endpoint findSingleActiveEndpoint(final String serviceURIString)
            throws ServiceNotFoundException, NoActiveRouteException, InactiveServiceException {
        LOGGER.debug(format("Finding active endpoint for service URI [%s]", serviceURIString));
        Service service = findActiveService(serviceURIString);

        for (ServiceEndpoint serviceEndpoint : service.getServiceEndpoints()) {
            if (serviceEndpoint.isRoutable()) {
                return serviceEndpoint.getEndpoint();
            }
        }
        throw NoActiveRouteException.noActiveEndpointForService(service);
    }

    @Transactional
    @Override
    public Service findActiveService(final String serviceURIString)
            throws InactiveServiceException, ServiceNotFoundException {
        Service service = getService(serviceURIString);
        if (!service.isActive()) {
            LOGGER.debug(format("Service found for service URI [%s], however it is inactive", serviceURIString));
            throw new InactiveServiceException(serviceURIString);
        }
        return service;
    }



    private Service getService(String serviceURIString) throws ServiceNotFoundException {
        Service service = null;
        try {
            service = serviceDAO.getEndpointByServiceURI(serviceURIString);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ServiceNotFoundException(serviceURIString);
        }
        return service;
    }
}
