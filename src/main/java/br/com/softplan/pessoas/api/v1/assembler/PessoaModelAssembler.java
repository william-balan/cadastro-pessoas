package br.com.softplan.pessoas.api.v1.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import br.com.softplan.pessoas.api.v1.controller.PessoaController;
import br.com.softplan.pessoas.api.v1.model.PessoaModel;
import br.com.softplan.pessoas.domain.model.Pessoa;

@Component
public class PessoaModelAssembler extends RepresentationModelAssemblerSupport<Pessoa, PessoaModel> {

    @Autowired
    private ModelMapper modelMapper;

    public PessoaModelAssembler() {
        super(PessoaController.class, PessoaModel.class);
    }

    @Override
    public PessoaModel toModel(Pessoa pessoa) {

        PessoaModel pessoaModel = this.createModelWithId(pessoa.getId(), pessoa);
        System.out.println(pessoaModel);

        this.modelMapper.map(pessoa, pessoaModel);
        System.out.println(pessoaModel);

        pessoaModel.add(linkTo(PessoaController.class)
                        .withRel("pessoas"));

        return pessoaModel;
    }

    @Override
    public CollectionModel<PessoaModel> toCollectionModel(Iterable<? extends Pessoa> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(PessoaController.class)
                		.withRel(IanaLinkRelations.SELF));
    }
}
