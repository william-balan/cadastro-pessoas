package br.com.softplan.pessoas.api.v1.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.softplan.pessoas.api.v1.openapi.RootEntryPointControllerOpenApi;


@RestController
@RequestMapping("/v1")
public class RootEntryPointController implements RootEntryPointControllerOpenApi {
    
    @GetMapping(value="")
    public RootEntryPointModel root() {

        var root = new RootEntryPointModel();

        root.add(linkTo(PessoaController.class).withRel("pessoas"));

        return root;
    }
    
    
    public static class RootEntryPointModel extends RepresentationModel<RootEntryPointModel> {
    }
}
