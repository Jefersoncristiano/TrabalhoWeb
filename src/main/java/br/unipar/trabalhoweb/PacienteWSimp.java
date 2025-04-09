package br.unipar.trabalhoweb;

import br.unipar.trabalhoweb.domain.Paciente;
import br.unipar.trabalhoweb.dto.PacienteInsertRequestDTO;
import br.unipar.trabalhoweb.exception.BusinessException;
import br.unipar.trabalhoweb.interfaces.PacienteWS;
import br.unipar.trabalhoweb.services.PacienteService;
import jakarta.jws.WebService;
import java.util.List;

@WebService(endpointInterface = "br.unipar.trabalhoweb.interfaces.PacienteWS")
public class PacienteWSimp implements PacienteWS {

    private final PacienteService service = new PacienteService();

    @Override
    public Paciente inserir(PacienteInsertRequestDTO paciente) throws BusinessException {
        return service.inserir(paciente);
    }

    @Override
    public Paciente editar(Paciente paciente) throws BusinessException {
        return service.editar(paciente);
    }

    @Override
    public List<Paciente> buscarTodos() throws BusinessException {
        return service.buscarTodos();
    }

    @Override
    public void excluir(String cpf) throws BusinessException {
        service.excluir(cpf);
    }

    @Override
    public List<Paciente> buscarPorNome(String nome) throws BusinessException {
        return service.buscarPorNome(nome);
    }
}