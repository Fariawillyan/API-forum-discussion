package com.about.forum.Controler;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.about.forum.Controler.Form.TopicoForm.AtualizacaoTopicoForm;
import com.about.forum.Controler.Form.TopicoForm.TopicoForm;
import com.about.forum.Controler.dto.DetalhesDoTopocoDto;
import com.about.forum.Repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.about.forum.Controler.dto.TopicoDto;
import com.about.forum.Model.Topico;
import com.about.forum.Repository.TopicoRepository;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/topicos")//evitar repetir a URL em todos os métodos
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping //@requestParam é um paramentro de url, spring considera que é obrigatório.
    public Page<TopicoDto> lista(@RequestParam (required = false) String nomeCurso, Pageable paginacao) {
        //Ao utilizar o objeto Page, além de devolver os registros, o Spring também devolve informações sobre paginação
        //@PageableDefault pode ser usado quando cliente da API nao enviar informacoes

        if (nomeCurso == null) {

            Page<Topico> topicos = topicoRepository.findAll(paginacao);
            return TopicoDto.converter(topicos);
        } else {
            Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
            return TopicoDto.converter(topicos);
        }
    }

    @PostMapping//@requesbody Indica ao Spring que os parâmetros enviados no corpo da requisição devem ser atribuídos
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {

    Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);
    
        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
        // Que a boa prática para métodos que cadastram informações é devolver o código HTTP 201, "created"
        // ao invés do código 200;
    }

    @GetMapping("/{id}")//A parte dinâmica do path deve ser declarada entre chaves.
    public ResponseEntity<DetalhesDoTopocoDto> detalhar (@PathVariable Long id){

        //Optional para verificar a entrada do usuario e depois retornar 404 caso nao encontrar

        Optional <Topico> topico = topicoRepository.findById(id);
        if(topico.isPresent()) {
            return ResponseEntity.ok(new DetalhesDoTopocoDto(topico.get()));
        }
        return ResponseEntity.notFound().build();
    }


    @PutMapping("/{id}")
    @Transactional //Ao finalizar, efetuará o commit automático da transação, caso nenhuma exception tenha sido lançada.
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form){

        Optional <Topico> optional = topicoRepository.findById(id);
        if(optional.isPresent()) {
            Topico topico = form.atualizar(id, topicoRepository);
            return ResponseEntity.ok(new TopicoDto(topico));
        }
        return ResponseEntity.notFound().build();

        //pela jpa ja ta sendo gerenciado, nao precisa chamar para o DB, final do metodo o spring
        //sera comitada a transacao, jpa vai detectar o atributo alterado e ela vai fazer o update.
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id ) {


        Optional<Topico> optional = topicoRepository.findById(id);
        if (optional.isPresent()) {
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


}
