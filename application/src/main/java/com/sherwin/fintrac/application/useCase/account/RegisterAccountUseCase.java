package com.sherwin.fintrac.application.useCase.account;

import com.sherwin.fintrac.application.useCase.account.model.RegisterAccountCommandUseCaseOutput;
import com.sherwin.fintrac.application.useCase.account.model.RegisterAccountUseCaseCommand;
import com.sherwin.fintrac.domain.common.model.CreationResult;

public interface RegisterAccountUseCase {
    CreationResult<RegisterAccountCommandUseCaseOutput> execute(
            RegisterAccountUseCaseCommand input);
}
