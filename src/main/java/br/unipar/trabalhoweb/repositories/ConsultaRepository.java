package br.unipar.trabalhoweb.repositories;

import br.unipar.trabalhoweb.domain.Consulta;
import br.unipar.trabalhoweb.domain.Medico;
import br.unipar.trabalhoweb.domain.Paciente;
import br.unipar.trabalhoweb.dto.ConsultaInsertRequestDTO;
import br.unipar.trabalhoweb.exception.BusinessException;
import br.unipar.trabalhoweb.infrastructure.ConnectionFactory;

import javax.naming.NamingException;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConsultaRepository {

    private static final String INSERT =
            "INSERT INTO consulta (data_consulta, id_paciente, id_medico, observacoes) VALUES (?, ?, ?, ?)";

    private static final String SELECT_ALL =
            "SELECT * FROM consulta WHERE cancelada = false";

    private static final String SELECT_BY_ID =
            "SELECT * FROM consulta WHERE id = ?";

    private static final String UPDATE_CANCELAR =
            "UPDATE consulta SET cancelada = true, motivo_cancelamento = ? WHERE id = ?";

    private static final String EXISTS_CONSULTA_DIA =
            "SELECT 1 FROM consulta WHERE id_paciente = ? AND DATE(data_consulta) = ? AND cancelada = false";

    public Consulta inserir(ConsultaInsertRequestDTO dto) throws BusinessException {

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataConsulta = dto.getDataConsulta();

        if (Duration.between(agora, dataConsulta).toMinutes() < 30) {
            throw new BusinessException("A consulta deve ser agendada com pelo menos 30 minutos de antecedência.");
        }

        if (dataConsulta.getHour() < 7 || dataConsulta.getHour() > 19 || dataConsulta.getDayOfWeek().getValue() > 6) {
            throw new BusinessException("Consultas devem ser agendadas de segunda a sábado, das 07:00 às 19:00.");
        }

        Integer idPaciente = dto.getIdPaciente();
        Integer idMedico = dto.getIdMedico();

        try {
            PacienteRepository pacienteRepository = new PacienteRepository();
            MedicoRepository medicoRepository = new MedicoRepository();

            Paciente paciente = pacienteRepository.buscarPorId(idPaciente);

            if (paciente == null || !paciente.isAtivo()) {
                throw new BusinessException("Paciente inativo ou não encontrado.");
            }

            if (this.pacienteTemConsultaNoDia(idPaciente, dataConsulta.toLocalDate())) {
                throw new BusinessException("O paciente já possui uma consulta agendada nesse dia.");
            }

            if (idMedico == null) {
                List<Medico> disponiveis = medicoRepository.buscarTodos();

                disponiveis.removeIf(m -> {
                    try {
                        return !m.isAtivo() || this.medicoEstaOcupado(m.getId(), dataConsulta);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return true;
                    }
                });

                if (disponiveis.isEmpty()) {
                    throw new BusinessException("Não há médicos disponíveis para este horário.");
                }

                Random rand = new Random();
                idMedico = disponiveis.get(rand.nextInt(disponiveis.size())).getId();

            } else {
                Medico medico = medicoRepository.buscarPorId(idMedico);

                if (medico == null || !medico.isAtivo()) {
                    throw new BusinessException("Médico inativo ou não encontrado.");
                }

                if (this.medicoEstaOcupado(idMedico, dataConsulta)) {
                    throw new BusinessException("Este médico já possui uma consulta neste horário.");
                }
            }

            Consulta consulta = new Consulta();
            consulta.setDataConsulta(dataConsulta);
            consulta.setIdPaciente(idPaciente);
            consulta.setIdMedico(idMedico);
            consulta.setObservacoes(dto.getObservacoes());

            return this.inserirConsulta(consulta);

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            throw new BusinessException("Erro ao agendar consulta. Entre em contato com o suporte.");
        }
    }

    public Consulta inserirConsulta(Consulta consulta) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = new ConnectionFactory().getConnection();
            stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setTimestamp(1, Timestamp.valueOf(consulta.getDataConsulta()));
            stmt.setInt(2, consulta.getIdPaciente());
            stmt.setInt(3, consulta.getIdMedico());
            stmt.setString(4, consulta.getObservacoes());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                consulta.setId(rs.getInt(1));
            }

            return consulta;

        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    public List<Consulta> buscarTodos() throws SQLException, NamingException {
        List<Consulta> consultas = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();
            pstmt = conn.prepareStatement(SELECT_ALL);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Consulta c = montarConsulta(rs);
                consultas.add(c);
            }

            return consultas;

        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    public Consulta buscarPorId(Integer id) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();
            pstmt = conn.prepareStatement(SELECT_BY_ID);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return montarConsulta(rs);
            }

            return null;

        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    public void cancelar(Consulta consulta) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = new ConnectionFactory().getConnection();
            pstmt = conn.prepareStatement(UPDATE_CANCELAR);
            pstmt.setString(1, consulta.getMotivoCancelamento());
            pstmt.setInt(2, consulta.getId());
            pstmt.executeUpdate();

        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    public boolean medicoEstaOcupado(Integer idMedico, LocalDateTime dataConsulta) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();
            stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM consulta WHERE id_medico = ? AND data_consulta = ? AND cancelada = false"
            );
            stmt.setInt(1, idMedico);
            stmt.setTimestamp(2, Timestamp.valueOf(dataConsulta));

            rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    public boolean pacienteTemConsultaNoDia(Integer idPaciente, LocalDate data) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();
            stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM consulta WHERE id_paciente = ? AND DATE(data_consulta) = ? AND cancelada = false"
            );
            stmt.setInt(1, idPaciente);
            stmt.setDate(2, Date.valueOf(data));
            rs = stmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;

        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    public List<Consulta> buscarTodas() throws SQLException, NamingException {
        List<Consulta> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();
            stmt = conn.prepareStatement("SELECT * FROM consulta");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Consulta consulta = montarConsulta(rs);
                lista.add(consulta);
            }

        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return lista;
    }

    private Consulta montarConsulta(ResultSet rs) throws SQLException {
        Consulta consulta = new Consulta();
        consulta.setId(rs.getInt("id"));
        consulta.setDataConsulta(rs.getTimestamp("data_consulta").toLocalDateTime());
        consulta.setIdMedico(rs.getInt("id_medico"));
        consulta.setIdPaciente(rs.getInt("id_paciente"));
        consulta.setObservacoes(rs.getString("observacoes"));
        consulta.setMotivoCancelamento(rs.getString("motivo_cancelamento"));
        consulta.setCancelada(rs.getBoolean("cancelada"));
        return consulta;
    }
}