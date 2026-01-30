package com.sherwin.fintrac.infrastructure.inbound.controller;

import com.sherwin.fintrac.application.useCase.PostTransactionUseCase;
import com.sherwin.fintrac.application.useCase.model.PostTransactionUseCaseResponse;
import com.sherwin.fintrac.domain.common.model.CreationResult;
import com.sherwin.fintrac.domain.common.model.FieldError;
import com.sherwin.fintrac.infrastructure.inbound.common.dto.Error;
import com.sherwin.fintrac.infrastructure.inbound.model.PostTransactionRequest;
import com.sherwin.fintrac.infrastructure.inbound.model.PostTransactionResponse;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class PostTransactionController {
    public static final String PATH = "/api/transactions";
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
                    handleSuccess(response);
            case CreationResult.Failure<PostTransactionUseCaseResponse>(
                            List<FieldError> validationErrors) ->
                    handleFailure(validationErrors);
        };
    }

    private ResponseEntity<?> handleSuccess(PostTransactionUseCaseResponse response) {
        return ResponseEntity.created(URI.create(PATH + "/" + response.transactionId()))
                .body(PostTransactionResponse.fromUseCase(response));
    }

    private ResponseEntity<?> handleFailure(List<FieldError> errors) {
        return Optional.ofNullable(errors)
                .map(Collection::stream)
                .map(fieldErrorStream -> fieldErrorStream.map(Error::fromDomain).toList())
                .map(responseErrors -> ResponseEntity.badRequest().body(responseErrors))
                .orElseThrow(
                        () -> new IllegalArgumentException("Invalid state in error processing"));
    }
}
