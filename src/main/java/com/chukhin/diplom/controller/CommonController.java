package com.chukhin.diplom.controller;

import com.chukhin.diplom.model.AccountDTO;
import com.chukhin.diplom.model.PaymentDTO;
import com.chukhin.diplom.model.UserDTO;
import com.chukhin.diplom.service.ModelService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.jnlp.UnavailableServiceException;
import java.util.List;

@RestController
public class CommonController {

    private final ModelService modelService;

    @Autowired
    public CommonController(ModelService modelService) {
        this.modelService = modelService;
    }

    @ApiOperation(value = "Get users list", nickname = "getUsers")
    @ApiResponses(value = {
            @ApiResponse(code = HttpStatus.SC_OK, message = "Success, users extracted", response = UserDTO.class),
            @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Failed to get users",
                    response = UserDTO.class)
    })
    @RequestMapping(path = "/api/users", method = RequestMethod.GET)
    public List<UserDTO> getUsers(boolean includeAccounts, boolean includePayments) throws UnavailableServiceException {
        return modelService.getUsers(includeAccounts, includePayments);
    }

    @ApiOperation(value = "Get accounts list", nickname = "getAccounts")
    @ApiResponses(value = {
            @ApiResponse(code = HttpStatus.SC_OK, message = "Success, accounts extracted", response = AccountDTO.class),
            @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Failed to get accounts",
                    response = AccountDTO.class)
    })
    @RequestMapping(path = "/api/accounts")
    public List<AccountDTO> getAccounts(boolean includePayments) throws UnavailableServiceException {
        return modelService.getAccounts(includePayments);
    }

    @ApiOperation(value = "Get payments list", nickname = "getPayments")
    @ApiResponses(value = {
            @ApiResponse(code = HttpStatus.SC_OK, message = "Success, payments extracted", response = PaymentDTO.class),
            @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Failed to get payments",
                    response = PaymentDTO.class)
    })
    @RequestMapping(path = "/api/payments")
    public List<PaymentDTO> getPayments() throws UnavailableServiceException {
        return modelService.getPayments();
    }

    @RequestMapping(path = "/health")
    public String health() {
        return "I'm healthy gate";
    }

}
