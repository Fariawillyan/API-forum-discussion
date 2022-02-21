package com.about.forum.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.about.forum.Model.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    List<Topico> findByCursoNome(String nomeCurso);

}
