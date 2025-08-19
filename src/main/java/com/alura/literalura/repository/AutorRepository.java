package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    // Autores vivos en un año: nacidos antes o en ese año, y fallecidos después o en ese año
    List<Autor> findByNacimientoLessThanEqualAndFallecimientoGreaterThanEqual(int year1, int year2);
}
