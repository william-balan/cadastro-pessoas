package br.com.softplan.pessoas.api.v1.controller;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.softplan.pessoas.api.v1.assembler.PessoaInputDisassembler;
import br.com.softplan.pessoas.api.v1.assembler.PessoaModelAssembler;
import br.com.softplan.pessoas.api.v1.model.PessoaInput;
import br.com.softplan.pessoas.api.v1.model.PessoaModel;
import br.com.softplan.pessoas.api.v1.openapi.PessoaControllerOpenAPI;
import br.com.softplan.pessoas.domain.model.Pessoa;
import br.com.softplan.pessoas.domain.service.PessoaService;

@RestController
@RequestMapping("/v1/pessoas")
public class PessoaController implements PessoaControllerOpenAPI {

    private PessoaService pessoaService;

    private PessoaModelAssembler assembler;

    private PessoaInputDisassembler disassembler;

    public PessoaController(PessoaService pessoaService,
                            PessoaModelAssembler assembler,
                            PessoaInputDisassembler disassembler) {
        this.pessoaService = pessoaService;
        this.assembler = assembler;
        this.disassembler = disassembler;
    }

    @GetMapping
    public CollectionModel<PessoaModel> listar() {
        List<Pessoa> pessoas = this.pessoaService.obterTodos();

        return this.assembler.toCollectionModel(pessoas);
    }

    @GetMapping("/{id}")
    public PessoaModel buscarPorId(@PathVariable("id") Long id) {
        Pessoa pessoa = this.pessoaService.obterPorId(id);

        return this.assembler.toModel(pessoa);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PessoaModel salvar(@RequestBody @Valid PessoaInput pessoaInput) {

        Pessoa pessoa = this.disassembler.toDomainObject(pessoaInput);

        Pessoa pessoaSalva = this.pessoaService.salvar(pessoa);

        addUriInResponseHeader(pessoaSalva.getId());

        return this.assembler.toModel(pessoaSalva);
    }

    @PutMapping("/{id}")
    public PessoaModel atualizar(@PathVariable("id") Long id, @RequestBody @Valid PessoaInput pessoaInput) {

        Pessoa pessoa = this.disassembler.toDomainObject(pessoaInput);

        Pessoa pessoaAtualizada = this.pessoaService.atualizar(id, pessoa);

        return this.assembler.toModel(pessoaAtualizada);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable("id") Long id) {
        this.pessoaService.remover(id);
    }

    private void addUriInResponseHeader(Long id) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(id).toUri();

        HttpServletResponse response = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getResponse();

        response.setHeader(HttpHeaders.LOCATION, uri.toString());
    }
}
