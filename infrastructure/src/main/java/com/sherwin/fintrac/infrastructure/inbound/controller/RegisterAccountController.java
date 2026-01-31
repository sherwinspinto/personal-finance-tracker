package com.sherwin.fintrac.infrastructure.inbound.controller;

import static com.sherwin.fintrac.infrastructure.inbound.controller.RegisterAccountController.ACCOUNTS_BASE_PATH;

import com.sherwin.fintrac.application.useCase.account.RegisterAccountUseCase;
import com.sherwin.fintrac.application.useCase.account.model.RegisterAccountCommandUseCaseOutput;
import com.sherwin.fintrac.domain.common.model.CreationResult;
import com.sherwin.fintrac.domain.common.model.FieldError;
import com.sherwin.fintrac.infrastructure.inbound.common.Constants;
import com.sherwin.fintrac.infrastructure.inbound.common.Utils;
import com.sherwin.fintrac.infrastructure.inbound.model.RegisterAccountRequest;
import com.sherwin.fintrac.infrastructure.inbound.model.RegisterAccountResponse;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.BASE_PATH + ACCOUNTS_BASE_PATH)
public class RegisterAccountController {
    public static final String ACCOUNTS_BASE_PATH = "/accounts";

    private final RegisterAccountUseCase registerAccountUseCase;

    RegisterAccountController(RegisterAccountUseCase registerAccountUseCase) {
        this.registerAccountUseCase = registerAccountUseCase;
    }

    @PostMapping
    public ResponseEntity<?> registerAccount(@RequestBody RegisterAccountRequest request) {
        CreationResult<RegisterAccountCommandUseCaseOutput> creationResult =
                registerAccountUseCase.execute(request.toCommand());
        return switch (creationResult) {
            case CreationResult.Success(RegisterAccountCommandUseCaseOutput success) ->
                    ResponseEntity.created(
                                    URI.create(ACCOUNTS_BASE_PATH + "/" + success.accountId()))
                            .body(RegisterAccountResponse.fromUseCaseOutput(success));
            case CreationResult.Failure(List<FieldError> validationErrors) ->
                    Utils.handleFailure(validationErrors);
        };
    }
}
