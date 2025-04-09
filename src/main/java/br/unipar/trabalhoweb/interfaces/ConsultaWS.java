package br.unipar.trabalhoweb.interfaces;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import br.unipar.trabalhoweb.domain.Consulta;
import br.unipar.trabalhoweb.dto.ConsultaInsertRequestDTO;
import br.unipar.trabalhoweb.exception.BusinessException;
import java.util.List;

@WebService
public interface ConsultaWS {

    @WebMethod
    Consulta inserir(ConsultaInsertRequestDTO consulta) throws BusinessException;

    @WebMethod
    void cancelar(Integer idConsulta, String motivo) throws BusinessException;

    @WebMethod
    List<Consulta> listarTodas() throws BusinessException;

    @WebMethod
    Consulta buscarPorId(Integer id) throws BusinessException;
}
