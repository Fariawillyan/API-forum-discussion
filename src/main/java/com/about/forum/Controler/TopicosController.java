package com.about.forum.Controler;

import java.net.URI;
import java.util.Optional;
import com.about.forum.Controler.Form.TopicoForm.AtualizacaoTopicoForm;
import com.about.forum.Controler.Form.TopicoForm.TopicoForm;
import com.about.forum.Controler.dto.DetalhesDoTopocoDto;
import com.about.forum.Repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.about.forum.Controler.dto.TopicoDto;
import com.about.forum.Model.Topico;
import com.about.forum.Repository.TopicoRepository;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;

/**
 * # @RestController ---> é o responsável por controlar as requisições indicando quem deve receber e responder.
 * # @RequestMapping("/topicos") ----> evitar repetir a URL em todos os métodos.
 * # @Autowired ----> fornece controle sobre onde e como a ligação entre os beans deve ser realizada.
 * # @RequestParam é um paramentro de url, spring considera que é obrigatório.
 * # @PageableDefault pode ser usado quando cliente da API nao enviar informacoes.
 * # Page ---> Ao utilizar o objeto Page, além de devolver os registros, o Spring devolve informações sobre paginação
 * # @Requesbody ----> Indica ao Spring que os parâmetros enviados no corpo da requisição devem ser atribuídos. *
 * # URI uri ----> Que a boa prática para métodos que cadastram informações é devolver o código HTTP 201, "created"
 * ao invés do código 200;
 * # Optional ----> para verificar a entrada do usuario e depois retornar 404 caso nao encontrar.
 * # @Transactional ---> Ao finalizar, efetuará o commit automático, caso nenhuma exception tenha sido lançada. *
 */

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    @Cacheable(value = "listaDeTopicos")
    public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso, Pageable paginacao) {

        if (nomeCurso == null) {

            Page<Topico> topicos = topicoRepository.findAll(paginacao);
            return TopicoDto.converter(topicos);
        } else {
            Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
            return TopicoDto.converter(topicos);
        }
    }

    @PostMapping
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {

        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesDoTopocoDto> detalhar(@PathVariable Long id) {

        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            return ResponseEntity.ok(new DetalhesDoTopocoDto(topico.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {

        Optional<Topico> optional = topicoRepository.findById(id);
        if (optional.isPresent()) {
            Topico topico = form.atualizar(id, topicoRepository);
            return ResponseEntity.ok(new TopicoDto(topico));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {

        Optional<Topico> optional = topicoRepository.findById(id);
        if (optional.isPresent()) {
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
