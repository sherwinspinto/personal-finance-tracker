package com.sherwin.fintrac.infrastructure.inbound.controller;

import static com.sherwin.fintrac.infrastructure.inbound.controller.PostTransactionController.TRANSACTIONS_BASE_PATH;

import com.sherwin.fintrac.application.useCase.transaction.PostTransactionUseCase;
import com.sherwin.fintrac.application.useCase.transaction.model.PostTransactionUseCaseResponse;
import com.sherwin.fintrac.domain.common.model.CreationResult;
import com.sherwin.fintrac.domain.common.model.FieldError;
import com.sherwin.fintrac.infrastructure.inbound.common.Constants;
import com.sherwin.fintrac.infrastructure.inbound.common.Utils;
import com.sherwin.fintrac.infrastructure.inbound.model.PostTransactionRequest;
import com.sherwin.fintrac.infrastructure.inbound.model.PostTransactionResponse;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.BASE_PATH + TRANSACTIONS_BASE_PATH)
public class PostTransactionController {
    public static final String TRANSACTIONS_BASE_PATH = "/transactions";
    private final PostTransactionUseCase postTransactionUseCase;

    @Autowired
    PostTransactionController(
            @Qualifier("postTransactionUseCase") PostTransactionUseCase postTransactionUseCase) {
        this.postTransactionUseCase = postTransactionUseCase;
    }

    @PostMapping
    public ResponseEntity<?> postTransaction(@RequestBody PostTransactionRequest request) {
        CreationResult<PostTransactionUseCaseResponse> creationResult =
                postTransactionUseCase.execute(request.toCommand());
        return switch (creationResult) {
            case CreationResult.Success(PostTransactionUseCaseResponse response) ->
                    ResponseEntity.created(
                                    URI.create(
                                            TRANSACTIONS_BASE_PATH
                                                    + "/"
                                                    + response.transactionId()))
                            .body(PostTransactionResponse.fromUseCase(response));
            case CreationResult.Failure<PostTransactionUseCaseResponse>(
                            List<FieldError> validationErrors) ->
                    Utils.handleFailure(validationErrors);
        };
    }
}
