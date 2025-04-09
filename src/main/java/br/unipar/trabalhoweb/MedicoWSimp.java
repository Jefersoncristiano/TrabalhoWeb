package br.unipar.trabalhoweb;

import br.unipar.trabalhoweb.domain.Medico;
import br.unipar.trabalhoweb.dto.MedicoInsertRequestDTO;
import br.unipar.trabalhoweb.exception.BusinessException;
import br.unipar.trabalhoweb.interfaces.MedicoWS;
import br.unipar.trabalhoweb.services.MedicoService;

import jakarta.jws.WebService;
import java.util.List;

@WebService(endpointInterface = "br.unipar.trabalhoweb.interfaces.MedicoWS")
public class MedicoWSimp implements MedicoWS {

    private MedicoService service = new MedicoService();

    @Override
    public Medico inserir(MedicoInsertRequestDTO medico) throws BusinessException {
        return service.inserir(medico);
    }

    @Override
    public Medico editar(Medico medico) throws BusinessException {
        return service.editar(medico);
    }

    @Override
    public List<Medico> buscarTodos() throws BusinessException {
        return service.buscarTodos();
    }

    @Override
    public void excluir(Integer crm) throws BusinessException {
        service.excluir(crm);
    }

    @Override
    public List<Medico> buscarPorNome(String nome) throws BusinessException {
        return service.buscarPorNome(nome);
    }
}
