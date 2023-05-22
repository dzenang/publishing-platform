package com.spotlight.platform.userprofile.api.web.exceptionmappers;

import com.spotlight.platform.userprofile.api.core.exceptions.BadRequestException;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DropwizardExtensionsSupport.class)
class BadRequestExceptionMapperTest {

    private static final ResourceExtension EXT = ResourceExtension.builder()
            .addResource(new MockResource())
            .setRegisterDefaultExceptionMappers(false)
            .addProvider(new BadRequestExceptionMapper())
            .build();

    private Client client;

    @BeforeEach
    void setUp() {
        client = EXT.client();
    }

    @Test
    void badRequestOnPost_ResultsIn400() {
        Response response = client.target(MockResource.RESOURCE_URLS.THROW_EXCEPTION).request().post(Entity.json("{}"));

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        assertThat(response.readEntity(String.class)).isEqualTo("post-exception");
    }

    @Test
    void badRequestOnGet_ResultsIn400() {
        Response response = client.target(MockResource.RESOURCE_URLS.THROW_EXCEPTION).request().get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        assertThat(response.readEntity(String.class)).isEqualTo("get-exception");
    }

    @Path("/")
    public static class MockResource {
        public static class RESOURCE_URLS {
            public static final String THROW_EXCEPTION = "/throwBadRequestException";
        }

        @GET
        @Path(RESOURCE_URLS.THROW_EXCEPTION)
        public void throwExceptionOnGet() { throw new BadRequestException("get-exception"); }

        @POST
        @Path(RESOURCE_URLS.THROW_EXCEPTION)
        public void throwExceptionOnPost() {
            throw new BadRequestException("post-exception");
        }
    }
}