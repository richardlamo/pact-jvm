package au.com.dius.pact.consumer;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.exampleclients.ConsumerClient;
import au.com.dius.pact.model.RequestResponsePact;

public class PactDslJsonArrayTemplateTest extends ConsumerPactTestMk2 {
    @Override
    protected RequestResponsePact createPact(PactDslWithProvider builder) {
        DslPart personTemplate = new PactDslJsonBody()
                .id()
                .stringType("name")
                .date("dob");

        DslPart body = new PactDslJsonArray()
                .template(personTemplate, 3);

        RequestResponsePact pact = builder
                .uponReceiving("java test interaction with a DSL array body with templates")
                .path("/")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(body)
                .toPact();

        MatcherTestUtils.assertResponseMatcherKeysEqualTo(pact,
                "$.body[0].id",
                "$.body[0].name",
                "$.body[0].dob",
                "$.body[1].id",
                "$.body[1].name",
                "$.body[1].dob",
                "$.body[2].id",
                "$.body[2].name",
                "$.body[2].dob"
        );

        return pact;
    }

    @Override
    protected String providerName() {
        return "test_provider_array";
    }

    @Override
    protected String consumerName() {
        return "test_consumer_array";
    }

    @Override
    protected void runTest(MockServer mockServer) {
        try {
            new ConsumerClient(mockServer.getUrl()).getAsList("/");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
