package com.chukhin.diplom.service;

import com.chukhin.diplom.model.AccountDTO;
import com.chukhin.diplom.model.PaymentDTO;
import com.chukhin.diplom.model.UserDTO;

import javax.jnlp.UnavailableServiceException;
import java.util.List;

public interface ModelService {

    List<UserDTO> getUsers(boolean includeAccounts, boolean includePayments) throws UnavailableServiceException;

    List<AccountDTO> getAccounts(boolean includePayments) throws UnavailableServiceException;

    List<PaymentDTO> getPayments() throws UnavailableServiceException;

}
