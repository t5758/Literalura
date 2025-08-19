package com.alura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RespuestaApi {
    private List<LibroDto> results;

    public List<LibroDto> getResults() { return results; }
    public void setResults(List<LibroDto> results) { this.results = results; }
}
