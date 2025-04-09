package br.unipar.trabalhoweb.repositories;

import br.unipar.trabalhoweb.domain.Paciente;
import br.unipar.trabalhoweb.infrastructure.ConnectionFactory;
import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteRepository {

    private static final String INSERT =
            "INSERT INTO paciente (nome, telefone, email, cpf, logradouro, numero, complemento, bairro, cidade, uf, cep, ativo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, true)";

    private static final String UPDATE =
            "UPDATE paciente SET nome = ?, telefone = ?, logradouro = ?, numero = ?, complemento = ?, bairro = ?, cidade = ?, uf = ?, cep = ? " +
                    "WHERE cpf = ?";

    private static final String SELECT_ALL =
            "SELECT * FROM paciente WHERE ativo = true ORDER BY nome";

    private static final String DELETE_BY_CPF =
            "UPDATE paciente SET ativo = false WHERE cpf = ?";

    private static final String SELECT_BY_NOME =
            "SELECT * FROM paciente WHERE nome ILIKE ? AND ativo = true ORDER BY nome";

    private static final String EXISTE_CPF =
            "SELECT 1 FROM paciente WHERE cpf = ?";

    public void inserir(Paciente paciente) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = new ConnectionFactory().getConnection();
            pstmt = conn.prepareStatement(INSERT);
            pstmt.setString(1, paciente.getNome());
            pstmt.setString(2, paciente.getTelefone());
            pstmt.setString(3, paciente.getEmail());
            pstmt.setString(4, paciente.getCpf());
            pstmt.setString(5, paciente.getLogradouro());
            pstmt.setString(6, paciente.getNumero());
            pstmt.setString(7, paciente.getComplemento());
            pstmt.setString(8, paciente.getBairro());
            pstmt.setString(9, paciente.getCidade());
            pstmt.setString(10, paciente.getUf());
            pstmt.setString(11, paciente.getCep());
            pstmt.executeUpdate();

        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    public void editar(Paciente paciente) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = new ConnectionFactory().getConnection();
            pstmt = conn.prepareStatement(UPDATE);
            pstmt.setString(1, paciente.getNome());
            pstmt.setString(2, paciente.getTelefone());
            pstmt.setString(3, paciente.getLogradouro());
            pstmt.setString(4, paciente.getNumero());
            pstmt.setString(5, paciente.getComplemento());
            pstmt.setString(6, paciente.getBairro());
            pstmt.setString(7, paciente.getCidade());
            pstmt.setString(8, paciente.getUf());
            pstmt.setString(9, paciente.getCep());
            pstmt.setString(10, paciente.getCpf());
            pstmt.executeUpdate();

        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    public List<Paciente> buscarTodos() throws SQLException, NamingException {
        List<Paciente> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();
            pstmt = conn.prepareStatement(SELECT_ALL);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(montarPaciente(rs));
            }

        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }

        return lista;
    }

    public void excluirPorCpf(String cpf) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = new ConnectionFactory().getConnection();
            pstmt = conn.prepareStatement(DELETE_BY_CPF);
            pstmt.setString(1, cpf);
            pstmt.executeUpdate();

        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    public List<Paciente> buscarPorNome(String nome) throws SQLException, NamingException {
        List<Paciente> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();
            pstmt = conn.prepareStatement(SELECT_BY_NOME);
            pstmt.setString(1, "%" + nome + "%");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(montarPaciente(rs));
            }

        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }

        return lista;
    }

    public boolean existeCpf(String cpf) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();
            pstmt = conn.prepareStatement(EXISTE_CPF);
            pstmt.setString(1, cpf);
            rs = pstmt.executeQuery();

            return rs.next();

        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    private Paciente montarPaciente(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setTelefone(rs.getString("telefone"));
        p.setEmail(rs.getString("email"));
        p.setCpf(rs.getString("cpf"));
        p.setLogradouro(rs.getString("logradouro"));
        p.setNumero(rs.getString("numero"));
        p.setComplemento(rs.getString("complemento"));
        p.setBairro(rs.getString("bairro"));
        p.setCidade(rs.getString("cidade"));
        p.setUf(rs.getString("uf"));
        p.setCep(rs.getString("cep"));
        p.setAtivo(rs.getBoolean("ativo"));
        return p;
    }

    public Paciente buscarPorId(Integer id) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = new ConnectionFactory().getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM paciente WHERE id = ?");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Paciente paciente = new Paciente();
                paciente.setId(rs.getInt("id"));
                paciente.setNome(rs.getString("nome"));
                paciente.setEmail(rs.getString("email"));
                paciente.setTelefone(rs.getString("telefone"));
                paciente.setCpf(rs.getString("cpf"));
                paciente.setLogradouro(rs.getString("logradouro"));
                paciente.setNumero(rs.getString("numero"));
                paciente.setComplemento(rs.getString("complemento"));
                paciente.setBairro(rs.getString("bairro"));
                paciente.setCidade(rs.getString("cidade"));
                paciente.setUf(rs.getString("uf"));
                paciente.setCep(rs.getString("cep"));
                paciente.setAtivo(rs.getBoolean("ativo"));
                return paciente;
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }

        return null;
    }
}