package br.unipar.trabalhoweb.repositories;

import br.unipar.trabalhoweb.domain.Medico;
import br.unipar.trabalhoweb.infrastructure.ConnectionFactory;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoRepository {

    private static final String INSERT =
            "INSERT INTO medico (nome, telefone, email, crm, especialidade, logradouro, numero, complemento, bairro) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE medico SET nome = ?, telefone = ?, logradouro = ?, numero = ?, complemento = ?, bairro = ? WHERE crm = ?";

    private static final String SELECT_ALL =
            "SELECT * FROM medico WHERE ativo = true";

    private static final String DELETE_BY_CRM =
            "UPDATE medico SET ativo = false WHERE crm = ?";

    private static final String SELECT_BY_NOME =
            "SELECT * FROM medico WHERE nome ILIKE ? AND ativo = true";

    private static final String EXISTE_CRM =
            "SELECT 1 FROM medico WHERE crm = ?";

    private static final String SELECT_BY_CRM =
            "SELECT * FROM medico WHERE crm = ?";

    public void inserir(Medico medico) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = new ConnectionFactory().getConnection();
            pstmt = conn.prepareStatement(INSERT);
            pstmt.setString(1, medico.getNome());
            pstmt.setString(2, medico.getTelefone());
            pstmt.setString(3, medico.getEmail());
            pstmt.setInt(4, medico.getCRM());
            pstmt.setString(5, medico.getEspecialidade());
            pstmt.setString(6, medico.getLogradouro());
            pstmt.setString(7, medico.getNumero());
            pstmt.setString(8, medico.getComplemento());
            pstmt.setString(9, medico.getBairro());
            pstmt.executeUpdate();

        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    public void editar(Medico medico) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = new ConnectionFactory().getConnection();
            pstmt = conn.prepareStatement(UPDATE);
            pstmt.setString(1, medico.getNome());
            pstmt.setString(2, medico.getTelefone());
            pstmt.setString(3, medico.getLogradouro());
            pstmt.setString(4, medico.getNumero());
            pstmt.setString(5, medico.getComplemento());
            pstmt.setString(6, medico.getBairro());
            pstmt.setInt(7, medico.getCRM());
            pstmt.executeUpdate();

        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    public List<Medico> buscarTodos() throws SQLException, NamingException {
        List<Medico> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();
            pstmt = conn.prepareStatement(SELECT_ALL);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(montarMedico(rs));
            }

        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }

        return lista;
    }

    public void excluirPorCrm(Integer crm) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = new ConnectionFactory().getConnection();
            pstmt = conn.prepareStatement(DELETE_BY_CRM);
            pstmt.setInt(1, crm);
            pstmt.executeUpdate();

        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    public List<Medico> buscarPorNome(String nome) throws SQLException, NamingException {
        List<Medico> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();
            pstmt = conn.prepareStatement(SELECT_BY_NOME);
            pstmt.setString(1, "%" + nome + "%");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(montarMedico(rs));
            }

        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }

        return lista;
    }

    public boolean existeCrm(Integer crm) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();
            pstmt = conn.prepareStatement(EXISTE_CRM);
            pstmt.setInt(1, crm);
            rs = pstmt.executeQuery();

            return rs.next();

        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    public Medico buscarPorCrm(Integer crm) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();
            pstmt = conn.prepareStatement(SELECT_BY_CRM);
            pstmt.setInt(1, crm);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return montarMedico(rs);
            }

            return null;

        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    public Medico buscarPorId(Integer id) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();
            stmt = conn.prepareStatement("SELECT * FROM medico WHERE id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Medico medico = new Medico();
                medico.setId(rs.getInt("id"));
                medico.setNome(rs.getString("nome"));
                medico.setTelefone(rs.getString("telefone"));
                medico.setEmail(rs.getString("email"));
                medico.setCRM(rs.getInt("crm"));
                medico.setEspecialidade(rs.getString("especialidade"));
                medico.setAtivo(rs.getBoolean("ativo"));
                return medico;
            }

            return null;

        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    private Medico montarMedico(ResultSet rs) throws SQLException {
        Medico m = new Medico();
        m.setId(rs.getInt("id"));
        m.setNome(rs.getString("nome"));
        m.setTelefone(rs.getString("telefone"));
        m.setEmail(rs.getString("email"));
        m.setCRM(rs.getInt("crm"));
        m.setEspecialidade(rs.getString("especialidade"));
        m.setLogradouro(rs.getString("logradouro"));
        m.setNumero(rs.getString("numero"));
        m.setComplemento(rs.getString("complemento"));
        m.setBairro(rs.getString("bairro"));
        return m;
    }
}