package ca.ulaval.glo4002.billing.service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse
{
    @JsonProperty("errors")
    private final List<WrappedErrorResponse> errors;

    public ErrorResponse(String error, String description, String entity)
    {
        this.errors = new ArrayList<>();
        this.errors.add(new WrappedErrorResponse(error, description, entity));
    }

    private class WrappedErrorResponse
    {
        @JsonProperty("error")
        final String error;
        @JsonProperty("description")
        final String description;
        @JsonProperty("entity")
        final String entity;

        private WrappedErrorResponse(String error, String description, String entity)
        {
            this.error = error;
            this.description = description;
            this.entity = entity;
        }
    }
}