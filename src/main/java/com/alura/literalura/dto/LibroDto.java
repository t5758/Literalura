package com.alura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LibroDto {

    private String title;

    @JsonAlias("languages")
    private List<String> idiomas;

    @JsonAlias("download_count")
    private Integer descargas;

    private List<AutorDto> authors;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<String> getIdiomas() { return idiomas; }
    public void setIdiomas(List<String> idiomas) { this.idiomas = idiomas; }

    public Integer getDescargas() { return descargas; }
    public void setDescargas(Integer descargas) { this.descargas = descargas; }

    public List<AutorDto> getAuthors() { return authors; }
    public void setAuthors(List<AutorDto> authors) { this.authors = authors; }
}
