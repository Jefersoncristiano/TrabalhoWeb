package br.unipar.trabalhoweb.services;

import br.unipar.trabalhoweb.domain.Paciente;
import br.unipar.trabalhoweb.dto.PacienteInsertRequestDTO;
import br.unipar.trabalhoweb.exception.BusinessException;
import br.unipar.trabalhoweb.repositories.PacienteRepository;
import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

public class PacienteService {

    private final PacienteRepository repository;

    public PacienteService() {
        repository = new PacienteRepository();
    }

    public Paciente inserir(PacienteInsertRequestDTO dto) throws BusinessException {

        if (dto.getNome() == null || dto.getNome().isEmpty())
            throw new BusinessException("Nome do paciente é obrigatório");

        if (dto.getEmail() == null || dto.getEmail().isEmpty())
            throw new BusinessException("E-mail do paciente é obrigatório");

        if (dto.getTelefone() == null || dto.getTelefone().isEmpty())
            throw new BusinessException("Telefone do paciente é obrigatório");

        if (dto.getCpf() == null || dto.getCpf().isEmpty())
            throw new BusinessException("CPF do paciente é obrigatório");

        if (dto.getLogradouro() == null || dto.getLogradouro().isEmpty())
            throw new BusinessException("Logradouro do paciente é obrigatório");

        if (dto.getBairro() == null || dto.getBairro().isEmpty())
            throw new BusinessException("Bairro do paciente é obrigatório");

        if (dto.getCidade() == null || dto.getCidade().isEmpty())
            throw new BusinessException("Cidade do paciente é obrigatória");

        if (dto.getUf() == null || dto.getUf().isEmpty())
            throw new BusinessException("UF do paciente é obrigatória");

        if (dto.getCep() == null || dto.getCep().isEmpty())
            throw new BusinessException("CEP do paciente é obrigatório");

        Paciente paciente = new Paciente();
        paciente.setNome(dto.getNome());
        paciente.setEmail(dto.getEmail());
        paciente.setTelefone(dto.getTelefone());
        paciente.setCpf(dto.getCpf());
        paciente.setLogradouro(dto.getLogradouro());
        paciente.setNumero(dto.getNumero());
        paciente.setComplemento(dto.getComplemento());
        paciente.setBairro(dto.getBairro());
        paciente.setCidade(dto.getCidade());
        paciente.setUf(dto.getUf());
        paciente.setCep(dto.getCep());

        try {
            if (repository.existeCpf(dto.getCpf())) {
                throw new BusinessException("Já existe um paciente com este CPF");
            }

            repository.inserir(paciente);
            return paciente;

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("Erro ao inserir paciente. Entre em contato com o suporte do WebService");
        }
    }

    public Paciente editar(Paciente paciente) throws BusinessException {

        if (paciente.getId() == null)
            throw new BusinessException("Id do paciente é obrigatório para edição");

        if (paciente.getNome() == null || paciente.getNome().isEmpty())
            throw new BusinessException("Nome do paciente é obrigatório");

        if (paciente.getTelefone() == null || paciente.getTelefone().isEmpty())
            throw new BusinessException("Telefone do paciente é obrigatório");

        if (paciente.getLogradouro() == null || paciente.getLogradouro().isEmpty())
            throw new BusinessException("Logradouro do paciente é obrigatório");

        if (paciente.getBairro() == null || paciente.getBairro().isEmpty())
            throw new BusinessException("Bairro do paciente é obrigatório");

        if (paciente.getCidade() == null || paciente.getCidade().isEmpty())
            throw new BusinessException("Cidade do paciente é obrigatória");

        if (paciente.getUf() == null || paciente.getUf().isEmpty())
            throw new BusinessException("UF do paciente é obrigatória");

        if (paciente.getCep() == null || paciente.getCep().isEmpty())
            throw new BusinessException("CEP do paciente é obrigatório");

        try {
            repository.editar(paciente);
            return paciente;

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("Erro ao editar paciente. Entre em contato com o suporte do WebService");
        }
    }

    public List<Paciente> buscarTodos() throws BusinessException {
        try {
            return repository.buscarTodos();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            throw new BusinessException("Erro ao buscar pacientes. Entre em contato com o suporte do WebService");
        }
    }

    public void excluir(String cpf) throws BusinessException {
        if (cpf == null || cpf.isEmpty())
            throw new BusinessException("CPF do paciente é obrigatório para exclusão");

        try {
            repository.excluirPorCpf(cpf);
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            throw new BusinessException("Erro ao excluir paciente. Entre em contato com o suporte do WebService");
        }
    }

    public List<Paciente> buscarPorNome(String nome) throws BusinessException {
        if (nome == null || nome.isEmpty())
            throw new BusinessException("Nome do paciente é obrigatório para busca");

        try {
            return repository.buscarPorNome(nome);
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            throw new BusinessException("Erro ao buscar pacientes. Entre em contato com o suporte do WebService");
        }
    }
}