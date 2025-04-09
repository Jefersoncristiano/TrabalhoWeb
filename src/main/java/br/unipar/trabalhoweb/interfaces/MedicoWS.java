package br.unipar.trabalhoweb.interfaces;

import br.unipar.trabalhoweb.domain.Medico;
import br.unipar.trabalhoweb.dto.MedicoInsertRequestDTO;
import br.unipar.trabalhoweb.exception.BusinessException;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

import java.util.List;

@WebService
public interface MedicoWS {
    @WebMethod
    Medico inserir(MedicoInsertRequestDTO medico) throws BusinessException;

    @WebMethod
    Medico editar(Medico medico) throws BusinessException;

    @WebMethod
    List<Medico> buscarTodos() throws BusinessException;

    @WebMethod
    void excluir(Integer crm) throws BusinessException;

    @WebMethod
    List<Medico> buscarPorNome(String nome) throws BusinessException;

    
}
