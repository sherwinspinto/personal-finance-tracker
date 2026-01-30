package com.sherwin.fintrac.domain.common;

import com.sherwin.fintrac.domain.common.model.CreationResult;
import com.sherwin.fintrac.domain.common.model.FieldError;
import java.util.List;
import java.util.Optional;

public class Utils {
    public static Optional<List<FieldError>> getListOfValidationErrorsFromListOfCreationResults(
            List<CreationResult<?>> creationResults) {
        return Optional.of(
                        creationResults.stream()
                                .filter(CreationResult::failure)
                                .map(CreationResult::getValidationErrors)
                                .flatMap(List::stream)
                                .toList())
                .filter(errors -> !errors.isEmpty());
    }
}
