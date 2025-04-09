package br.unipar.trabalhoweb;

import br.unipar.trabalhoweb.services.ConsultaService;
import br.unipar.trabalhoweb.domain.Consulta;
import br.unipar.trabalhoweb.dto.ConsultaInsertRequestDTO;
import br.unipar.trabalhoweb.exception.BusinessException;
import br.unipar.trabalhoweb.interfaces.ConsultaWS;

import jakarta.jws.WebService;
import java.util.List;

@WebService(endpointInterface = "br.unipar.trabalhoweb.interfaces.ConsultaWS")
public class ConsultaWSimp implements ConsultaWS {

    private ConsultaService service = new ConsultaService();

    @Override
    public Consulta inserir(ConsultaInsertRequestDTO consulta) throws BusinessException {
        return service.inserir(consulta);
    }

    @Override
    public void cancelar(Integer idConsulta, String motivo) throws BusinessException {
        service.cancelar(idConsulta, motivo);
    }

    @Override
    public List<Consulta> listarTodas() throws BusinessException {
        return service.buscarTodos();
    }

    @Override
    public Consulta buscarPorId(Integer id) throws BusinessException {
        return service.buscarPorId(id);
    }
}
