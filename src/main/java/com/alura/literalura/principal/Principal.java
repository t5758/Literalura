package com.alura.literalura.principal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.alura.literalura.dto.AutorDto;
import com.alura.literalura.dto.LibroDto;
import com.alura.literalura.dto.RespuestaApi;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ApiService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Principal implements CommandLineRunner {

    private final LibroRepository libroRepo;
    private final AutorRepository autorRepo;
    private final ApiService apiService;

    public Principal(LibroRepository libroRepo, AutorRepository autorRepo, ApiService apiService) {
        this.libroRepo = libroRepo;
        this.autorRepo = autorRepo;
        this.apiService = apiService;
    }

    @Override
    public void run(String... args) {
        Scanner teclado = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n📚 MENÚ LITERALURA 📚");
            System.out.println("1 - Buscar libro por título");
            System.out.println("2 - Listar todos los libros");
            System.out.println("3 - Listar autores");
            System.out.println("4 - Listar autores vivos en un año");
            System.out.println("5 - Cantidad de libros por idioma");
            System.out.println("0 - Salir");
            System.out.print("Elige una opción: ");
            try {
                opcion = Integer.parseInt(teclado.nextLine());
            } catch (Exception e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> buscarLibro(teclado);
                case 2 -> libroRepo.findAll().forEach(System.out::println);
                case 3 -> autorRepo.findAll().forEach(System.out::println);
                case 4 -> listarAutoresVivosEnAnio(teclado);
                case 5 -> contarLibrosPorIdioma(teclado);
                case 0 -> System.out.println("👋 Hasta Pronto!");
                default -> System.out.println("Opción no válida");
            }
        }
    }

    private void buscarLibro(Scanner teclado) {
        try {
            System.out.print("Escribe el título: ");
            String titulo = teclado.nextLine();
            String json = apiService.buscarLibro(titulo);

            ObjectMapper mapper = new ObjectMapper();
            RespuestaApi respuesta = mapper.readValue(json, RespuestaApi.class);

            if (respuesta.getResults() != null && !respuesta.getResults().isEmpty()) {
                LibroDto libroDto = respuesta.getResults().get(0);

                AutorDto autorDto = (libroDto.getAuthors() != null && !libroDto.getAuthors().isEmpty())
                        ? libroDto.getAuthors().get(0) : null;

                Autor autor = new Autor();
                if (autorDto != null) {
                    autor.setNombre(autorDto.getName());
                    autor.setNacimiento(autorDto.getBirth_year());
                    autor.setFallecimiento(autorDto.getDeath_year());
                } else {
                    autor.setNombre("Autor desconocido");
                }

                Libro libro = new Libro();
                libro.setTitulo(libroDto.getTitle());
                if (libroDto.getIdiomas() != null && !libroDto.getIdiomas().isEmpty()) {
                    libro.setIdioma(libroDto.getIdiomas().get(0)); // solo el primero
                }
                libro.setDescargas(libroDto.getDescargas());
                libro.setAutor(autor);

                libroRepo.save(libro);
                System.out.println("✅ Libro guardado: " + libro);
            } else {
                System.out.println("No se encontró el libro.");
            }
        } catch (Exception e) {
            System.out.println("Error en la búsqueda: " + e.getMessage());
        }
    }

    private void listarAutoresVivosEnAnio(Scanner teclado) {
        System.out.print("Ingresa un año: ");
        try {
            int year = Integer.parseInt(teclado.nextLine());
            autorRepo
                    .findByNacimientoLessThanEqualAndFallecimientoGreaterThanEqual(year, year)
                    .forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Año inválido.");
        }
    }

    private void contarLibrosPorIdioma(Scanner teclado) {
        System.out.print("Idioma (ej: en, es): ");
        String idioma = teclado.nextLine();
        var libros = libroRepo.findByIdioma(idioma);

        System.out.println("\n📚 Libros en idioma '" + idioma + "': " + libros.size());
        if (!libros.isEmpty()) {
            libros.forEach(libro ->
                    System.out.println("- " + libro.getTitulo() + " — " + libro.getAutor().getNombre())
            );
        } else {
            System.out.println("⚠️ No hay libros en este idioma.");
        }
    }
}
