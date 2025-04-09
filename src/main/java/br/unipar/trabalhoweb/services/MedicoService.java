package br.unipar.trabalhoweb.services;

import br.unipar.trabalhoweb.domain.Medico;
import br.unipar.trabalhoweb.dto.MedicoInsertRequestDTO;
import br.unipar.trabalhoweb.exception.BusinessException;
import br.unipar.trabalhoweb.repositories.MedicoRepository;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

public class MedicoService {

    private MedicoRepository repository = new MedicoRepository();

    public Medico inserir(MedicoInsertRequestDTO dto) throws BusinessException {
        validarInsercao(dto);

        try {
            if (repository.existeCrm(dto.getCrm())) {
                throw new BusinessException("Já existe um médico com este CRM.");
            }

            Medico medico = new Medico();
            medico.setNome(dto.getNome());
            medico.setEmail(dto.getEmail());
            medico.setTelefone(dto.getTelefone());
            medico.setCRM(dto.getCrm());
            medico.setEspecialidade(dto.getEspecialidade());
            medico.setLogradouro(dto.getLogradouro());
            medico.setNumero(dto.getNumero());
            medico.setComplemento(dto.getComplemento());
            medico.setBairro(dto.getBairro());

            repository.inserir(medico);
            return medico;

        } catch (SQLException | NamingException e) {
            throw new BusinessException("Erro ao inserir médico: " + e.getMessage());
        }
    }

    public Medico editar(Medico medico) throws BusinessException {
        if (medico == null || medico.getCRM() == null) {
            throw new BusinessException("CRM do médico é obrigatório.");
        }

        try {
            Medico original = repository.buscarPorCrm(medico.getCRM());

            if (original == null) {
                throw new BusinessException("Médico não encontrado.");
            }

            if (!original.getEmail().equals(medico.getEmail())) {
                throw new BusinessException("E-mail do médico não pode ser alterado.");
            }
            if (!original.getEspecialidade().equals(medico.getEspecialidade())) {
                throw new BusinessException("Especialidade do médico não pode ser alterada.");
            }

            repository.editar(medico);
            return medico;

        } catch (SQLException | NamingException e) {
            throw new BusinessException("Erro ao editar médico: " + e.getMessage());
        }
    }

    public void excluir(Integer crm) throws BusinessException {
        if (crm == null) {
            throw new BusinessException("CRM é obrigatório.");
        }

        try {
            repository.excluirPorCrm(crm);
        } catch (SQLException | NamingException e) {
            throw new BusinessException("Erro ao excluir médico: " + e.getMessage());
        }
    }

    public List<Medico> buscarTodos() throws BusinessException {
        try {
            List<Medico> lista = repository.buscarTodos();

            lista.sort((m1, m2) -> m1.getNome().compareToIgnoreCase(m2.getNome()));

            return lista;

        } catch (SQLException | NamingException e) {
            throw new BusinessException("Erro ao buscar médicos: " + e.getMessage());
        }
    }

    public List<Medico> buscarPorNome(String nome) throws BusinessException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new BusinessException("Nome é obrigatório para busca.");
        }

        try {
            return repository.buscarPorNome(nome);
        } catch (SQLException | NamingException e) {
            throw new BusinessException("Erro ao buscar médicos por nome: " + e.getMessage());
        }
    }


    private void validarInsercao(MedicoInsertRequestDTO dto) throws BusinessException {
        if (dto == null)
            throw new BusinessException("Dados do médico não informados.");

        if (isNuloOuVazio(dto.getNome()))
            throw new BusinessException("Nome é obrigatório.");

        if (isNuloOuVazio(dto.getEmail()))
            throw new BusinessException("E-mail é obrigatório.");

        if (dto.getCrm() == null)
            throw new BusinessException("CRM é obrigatório.");

        if (isNuloOuVazio(dto.getEspecialidade()))
            throw new BusinessException("Especialidade é obrigatória.");

        if (isNuloOuVazio(dto.getLogradouro()))
            throw new BusinessException("Logradouro é obrigatório.");

        if (isNuloOuVazio(dto.getBairro()))
            throw new BusinessException("Bairro é obrigatório.");
    }

    private boolean isNuloOuVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
