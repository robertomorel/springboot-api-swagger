package br.com.treinaweb.springbootapi.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import br.com.treinaweb.springbootapi.entity.Pessoa;
import br.com.treinaweb.springbootapi.repository.PessoaRepository;

@RestController // A anotação @RestController permite definir um controller com características REST
public class PessoaController {

    // A anotação @Autowired delega ao Spring Boot a inicialização do objeto
    @Autowired
    private PessoaRepository _pessoaRepository;

    // Especificando os códigos e as mensagens de retorno diretamente no controller com as anotações @ApiResponses e @ApiResponse
    // A anotação @ApiOperation descreve o endpoint
    @ApiOperation(value = "Retorna uma lista de pessoas")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Retorna a lista de pessoa"),
        @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
        @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
    })
    // A anotação @RequestMapping permite definir uma rota. Caso não seja informado o método HTTP da rota,
    // ela será definida para todos os métodos
    @RequestMapping(value = "/pessoa", method = RequestMethod.GET, produces="application/json")
    public List<Pessoa> Get() {
        return _pessoaRepository.findAll();
    }

    // A anotação @PathVariable indica que o valor da variável virá de uma informação da rota
    @RequestMapping(value = "/pessoa/{id}", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<Pessoa> GetById(@PathVariable(value = "id") long id)
    {
        Optional<Pessoa> pessoa = _pessoaRepository.findById(id);
        if(pessoa.isPresent())
            return new ResponseEntity<Pessoa>(pessoa.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // A anotação @RequestBody indica que o valor do objeto virá do corpo da requisição
    // A anotação @Valid indica que os dados recebidos devem ser validados
    @RequestMapping(value = "/pessoa", method =  RequestMethod.POST, produces="application/json", consumes="application/json")
    public Pessoa Post(@Valid @RequestBody Pessoa pessoa)
    {
        return _pessoaRepository.save(pessoa);
    }

    @RequestMapping(value = "/pessoa/{id}", method =  RequestMethod.PUT, produces="application/json", consumes="application/json")
    public ResponseEntity<Pessoa> Put(@PathVariable(value = "id") long id, @Valid @RequestBody Pessoa newPessoa)
    {
        Optional<Pessoa> oldPessoa = _pessoaRepository.findById(id);
        if(oldPessoa.isPresent()){
            Pessoa pessoa = oldPessoa.get();
            pessoa.setNome(newPessoa.getNome());
            _pessoaRepository.save(pessoa);
            return new ResponseEntity<Pessoa>(pessoa, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/pessoa/{id}", method = RequestMethod.DELETE, produces="application/json")
    public ResponseEntity<Object> Delete(@PathVariable(value = "id") long id)
    {
        Optional<Pessoa> pessoa = _pessoaRepository.findById(id);
        if(pessoa.isPresent()){
            _pessoaRepository.delete(pessoa.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}