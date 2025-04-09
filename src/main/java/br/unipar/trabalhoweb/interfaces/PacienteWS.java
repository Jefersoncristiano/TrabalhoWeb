package br.unipar.trabalhoweb.interfaces;

import br.unipar.trabalhoweb.domain.Paciente;
import br.unipar.trabalhoweb.dto.PacienteInsertRequestDTO;
import br.unipar.trabalhoweb.exception.BusinessException;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

import java.util.List;

@WebService
public interface PacienteWS {

    @WebMethod
    Paciente inserir(PacienteInsertRequestDTO paciente) throws BusinessException;

    @WebMethod
    Paciente editar(Paciente paciente) throws BusinessException;

    @WebMethod
    List<Paciente> buscarTodos() throws BusinessException;

    @WebMethod
    void excluir(String cpf) throws BusinessException;

    @WebMethod
    List<Paciente> buscarPorNome(String nome) throws BusinessException;
}