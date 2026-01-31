package com.sherwin.fintrac.application.useCase.transaction;

import com.sherwin.fintrac.application.useCase.transaction.model.PostTransactionCommand;
import com.sherwin.fintrac.application.useCase.transaction.model.PostTransactionUseCaseResponse;
import com.sherwin.fintrac.domain.common.model.CreationResult;

public interface PostTransactionUseCase {
    CreationResult<PostTransactionUseCaseResponse> execute(PostTransactionCommand transaction);
}
