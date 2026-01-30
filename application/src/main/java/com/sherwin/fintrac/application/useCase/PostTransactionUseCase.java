package com.sherwin.fintrac.application.useCase;

import com.sherwin.fintrac.application.useCase.model.PostTransactionCommand;
import com.sherwin.fintrac.application.useCase.model.PostTransactionUseCaseResponse;
import com.sherwin.fintrac.domain.common.model.CreationResult;

public interface PostTransactionUseCase {
    CreationResult<PostTransactionUseCaseResponse> execute(PostTransactionCommand transaction);
}
