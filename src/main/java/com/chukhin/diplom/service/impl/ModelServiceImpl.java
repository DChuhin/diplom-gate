package com.chukhin.diplom.service.impl;

import com.chukhin.diplom.model.AccountDTO;
import com.chukhin.diplom.model.PaymentDTO;
import com.chukhin.diplom.model.UserDTO;
import com.chukhin.diplom.service.ModelService;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.health.model.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import javax.jnlp.UnavailableServiceException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModelServiceImpl implements ModelService {

    private static final String PAYMENT_SERVICE = "PAYMENT-SERVICE";
    private static final String ACCOUNT_SERVICE = "ACCOUNT-SERVICE";
    private static final String USER_SERVICE = "USER-SERVICE";
    private static final String USER_URI = "/api/users";
    private static final String ACCOUNT_URI = "/api/accounts";
    private static final String PAYMENT_URI = "/api/payments";
    private static final String HTTP_PREFIX = "http://";

    private final ConsulClient consulClient;
    private final RestOperations restOperations;

    @Autowired
    public ModelServiceImpl(ConsulClient consulClient, RestOperations restOperations) {
        this.consulClient = consulClient;
        this.restOperations = restOperations;
    }

    @Override
    public List<UserDTO> getUsers(boolean includeAccounts, boolean includePayments) throws UnavailableServiceException {
        Map<String, Object> params = new HashMap<>();
        params.put("includeAccounts", includeAccounts);
        params.put("includePayments", includePayments);
        return getModel(UserDTO.class, USER_SERVICE, USER_URI, params);
    }

    @Override
    public List<AccountDTO> getAccounts(boolean includePayments) throws UnavailableServiceException {
        Map<String, Object> params = Collections.singletonMap("includePayments", includePayments);
        return getModel(AccountDTO.class, ACCOUNT_SERVICE, ACCOUNT_URI, params);
    }

    @Override
    public List<PaymentDTO> getPayments() throws UnavailableServiceException {
        return getModel(PaymentDTO.class, PAYMENT_SERVICE, PAYMENT_URI, Collections.emptyMap());
    }

    private <T> List<T> getModel(Class<T> tClass, String serviceName, String url, Map<String, Object> params) throws UnavailableServiceException {
        List<HealthService> paymentNodes = consulClient.getHealthServices(serviceName, true, QueryParams.DEFAULT).getValue();
        if (paymentNodes.isEmpty()) {
            throw new UnavailableServiceException("Payment service is unavailable");
        }
        HealthService.Service paymentService = paymentNodes.get(0).getService();
        String fullurl = HTTP_PREFIX + paymentService.getAddress() + ":" + paymentService.getPort() + url;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(fullurl);
        params.entrySet().forEach(entry -> builder.queryParam(entry.getKey(), entry.getValue()));
        URI uri = builder.build().encode().toUri();
        ResponseEntity<List<T>> responseEntity = restOperations.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<T>>() {
        });
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            throw new UnavailableServiceException("Payment service is unavailable");
        }

    }
}
