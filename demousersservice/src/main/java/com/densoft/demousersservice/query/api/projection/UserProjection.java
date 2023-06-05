package com.densoft.demousersservice.query.api.projection;

import com.densoft.democommonservice.model.CardDetails;
import com.densoft.democommonservice.model.User;
import com.densoft.democommonservice.queries.GetUserPaymentDetailsQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserProjection {
    @QueryHandler
    public User getUserPaymentDetails(GetUserPaymentDetailsQuery getUserPaymentDetailsQuery) {
        //ideally get the details from the DB
        CardDetails cardDetails = CardDetails.builder()
                .name("john doe")
                .validUntilYear(2024)
                .validUntilMonth(01)
                .cardNumber("1234567890")
                .cvv(123)
                .build();

        return User.builder()
                .userId(getUserPaymentDetailsQuery.getUserId())
                .firstName("john")
                .lastName("doe")
                .cardDetails(cardDetails)
                .build();
    }
}
