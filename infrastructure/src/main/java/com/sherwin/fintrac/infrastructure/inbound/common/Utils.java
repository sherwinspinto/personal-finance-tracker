package com.sherwin.fintrac.infrastructure.inbound.common;

import com.sherwin.fintrac.domain.common.model.FieldError;
import com.sherwin.fintrac.infrastructure.inbound.common.dto.Error;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Utils {
    public static ResponseEntity<List<Error>> handleFailure(List<FieldError> errors) {
        return Optional.ofNullable(errors)
                .map(Collection::stream)
                .map(
                        fieldErrorStream ->
                                fieldErrorStream
                                        .map(
                                                com.sherwin.fintrac.infrastructure.inbound.common
                                                                .dto.Error
                                                        ::fromDomain)
                                        .toList())
                .map(
                        responseErrors ->
                                handleCheckedFailureHttpResponseType(errors).body(responseErrors))
                .orElseThrow(
                        () -> new IllegalArgumentException("Invalid state in error processing"));
    }

    static ResponseEntity.BodyBuilder handleCheckedFailureHttpResponseType(
            List<FieldError> errors) {
        List<FieldError> conflictErrors = errors.stream().filter(Utils::isConflictError).toList();
        if (conflictErrors.isEmpty()) {
            return ResponseEntity.badRequest();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT);
    }

    static boolean isConflictError(FieldError errors) {
        return errors instanceof FieldError.ConflictError;
    }
}
