package com.about.forum.Repository;


import com.about.forum.Model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
      Curso findByNome(String nome);
}
