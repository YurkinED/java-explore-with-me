package ru.practicum.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.compilation.model.Compilation;


@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query(value = "select c " +
            "from Compilation as c " +
            "WHERE c.pinned in ?1")
    Page<Compilation> searchCompilations(Boolean pinned, Pageable page);
}
