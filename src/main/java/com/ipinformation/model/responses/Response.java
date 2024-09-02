package com.ipinformation.model.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ipinformation.model.objects.Error;
import org.springframework.data.annotation.Transient;

public class Response {
    @JsonIgnore
    @Transient
    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
