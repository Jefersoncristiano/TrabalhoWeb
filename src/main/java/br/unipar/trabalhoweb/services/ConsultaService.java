package br.unipar.trabalhoweb.services;

import br.unipar.trabalhoweb.domain.Consulta;
import br.unipar.trabalhoweb.domain.Medico;
import br.unipar.trabalhoweb.domain.Paciente;
import br.unipar.trabalhoweb.dto.ConsultaInsertRequestDTO;
import br.unipar.trabalhoweb.exception.BusinessException;
import br.unipar.trabalhoweb.repositories.ConsultaRepository;
import br.unipar.trabalhoweb.repositories.MedicoRepository;
import br.unipar.trabalhoweb.repositories.PacienteRepository;


import br.unipar.trabalhoweb.domain.Consulta;
import br.unipar.trabalhoweb.domain.Medico;
import br.unipar.trabalhoweb.domain.Paciente;
import br.unipar.trabalhoweb.dto.ConsultaInsertRequestDTO;
import br.unipar.trabalhoweb.exception.BusinessException;
import br.unipar.trabalhoweb.repositories.ConsultaRepository;
import br.unipar.trabalhoweb.repositories.MedicoRepository;
import br.unipar.trabalhoweb.repositories.PacienteRepository;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    public ConsultaService() {
        consultaRepository = new ConsultaRepository();
        medicoRepository = new MedicoRepository();
        pacienteRepository = new PacienteRepository();
    }

    public Consulta inserir(ConsultaInsertRequestDTO dto) throws BusinessException {
        try {
            return consultaRepository.inserir(dto);
        } catch (BusinessException e) {
            throw e;
        }
    }

    public List<Consulta> buscarTodos() throws BusinessException {
        try {
            return consultaRepository.buscarTodos();
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            throw new BusinessException("Erro ao buscar consultas.");
        }
    }

    public Consulta buscarPorId(Integer id) throws BusinessException {
        try {
            return consultaRepository.buscarPorId(id);
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            throw new BusinessException("Erro ao buscar consulta por ID.");
        }
    }

    public void cancelar(Integer idConsulta, String motivo) throws BusinessException {
        try {
            Consulta consulta = consultaRepository.buscarPorId(idConsulta);

            if (consulta == null) {
                throw new BusinessException("Consulta não encontrada.");
            }

            LocalDateTime agora = LocalDateTime.now();
            LocalDateTime dataConsulta = consulta.getDataConsulta();

            if (Duration.between(agora, dataConsulta).toHours() < 24) {
                throw new BusinessException("Consulta só pode ser cancelada com no mínimo 24 horas de antecedência.");
            }

            consulta.setMotivoCancelamento(motivo);
            consulta.setCancelada(true);

            consultaRepository.cancelar(consulta);

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            throw new BusinessException("Erro ao cancelar a consulta.");
        }
    }
}
