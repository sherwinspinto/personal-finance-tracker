package com.sherwin.fintrac.launcher.integrationTest;

import com.sherwin.fintrac.application.useCase.PostTransactionUseCase;
import com.sherwin.fintrac.application.useCase.model.PostTransactionCommand;
import com.sherwin.fintrac.application.useCase.model.PostTransactionUseCaseResponse;
import com.sherwin.fintrac.domain.common.model.CreationResult;
import com.sherwin.fintrac.launcher.config.CommonConfig;
import com.sherwin.fintrac.launcher.config.TransactionConfig;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {TransactionConfig.class, CommonConfig.class})
@ComponentScan(basePackages = {"com.sherwin.fintrac.infrastructure.outbound.db.repository"})
@EnableJpaRepositories(basePackages = {"com.sherwin.fintrac.infrastructure.outbound.db.repository"})
@EntityScan(basePackages = {"com.sherwin.fintrac.infrastructure.outbound.db.entity"})
@EnableAutoConfiguration
public class PostTransactionConfigUseCaseIntegrationTest {
  private final PostTransactionUseCase postTransactionUseCase;

  @Autowired
  public PostTransactionConfigUseCaseIntegrationTest(
      @Qualifier("postTransactionUseCase") PostTransactionUseCase postTransactionUseCase) {
    this.postTransactionUseCase = postTransactionUseCase;
  }

  @Test
  void test_postTransaction_success() {
    UUID accountId = UUID.randomUUID();
    PostTransactionCommand postTransactionCommand =
        new PostTransactionCommand(accountId, 100L, "USD", "CREDIT", "Test description");
    CreationResult<PostTransactionUseCaseResponse> caseResponseCreationResult =
        postTransactionUseCase.execute(postTransactionCommand);
    Assertions.assertNotNull(caseResponseCreationResult);
    switch (caseResponseCreationResult) {
      case CreationResult.Success(PostTransactionUseCaseResponse success) -> {
        Assertions.assertNotNull(success);
        Assertions.assertEquals(accountId, success.accountId());
        Assertions.assertEquals(100L, success.amount());
        Assertions.assertEquals("CREDIT", success.type());
        Assertions.assertEquals("Test description", success.description());
      }
      case CreationResult.Failure<PostTransactionUseCaseResponse> failure -> {
        Assertions.fail("Expected success, but got failure: " + failure.validationErrors());
      }
    }
  }
}
