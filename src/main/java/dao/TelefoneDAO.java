package dao;

import dominio.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;
import java.util.List;

public class TelefoneDAO implements IDAO{
    /*public String salvar(EntidadeDominio entidade) {
    String sql = "INSERT INTO telefones(ddd, numero,tipo_telefone_id) VALUES (?,?,?)";
    Connection conn = null;
    PreparedStatement mysql = null;
    ResultSet rs = null;
    String telefone_id = null;

        try {
        // criar uma conexao com o banco de dados
        conn = Conexao.createConnectionToMySQL();

        // foi criada uma prepareStatement para executar uma Query
        mysql = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        // adicionar os valores que sao esperados pela query
        mysql.setString(1,((Telefone) entidade).getDdd());
        mysql.setString(2,((Telefone) entidade).getNumero());
        mysql.setInt(3,((Telefone) entidade).getTipo().getId());

        //	executar a query
        mysql.execute();

        rs = mysql.getGeneratedKeys();
        if (rs.next()) {
            telefone_id = String.valueOf(rs.getInt(1));
        }

        System.out.println("Telefone salvo com sucesso. ID: " + telefone_id);

    }catch(Exception e){
        e.printStackTrace();
    }finally {
        // fechar as conexoes
        try {
            if(mysql!=null) {
                mysql.close();
            }
            if(conn!=null) {
                conn.close();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
        return telefone_id;
    }*/

    public String salvar(Telefone telefone, Connection conn) throws SQLException {
        String sql = "INSERT INTO telefones(ddd, numero, tipo_telefone_id) VALUES (?,?,?)";
        PreparedStatement mysql = null;
        ResultSet rs = null;
        String telefone_id = null;

        try {
            // foi criada uma prepareStatement para executar uma Query
            mysql = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // adicionar os valores que sao esperados pela query
            mysql.setString(1, telefone.getDdd());
            mysql.setString(2, telefone.getNumero());
            mysql.setInt(3, telefone.getTipo().getId());

            // executar a query
            mysql.execute();

            rs = mysql.getGeneratedKeys();
            if (rs.next()) {
                telefone_id = String.valueOf(rs.getInt(1));
            }

            System.out.println("Telefone salvo com sucesso. ID: " + telefone_id);

        } finally {
            // fechar as conexoes
            if (rs != null) rs.close();
            if (mysql != null) mysql.close();
        }
        return telefone_id;
    }

    @Override
    public String salvar(EntidadeDominio entidade, Connection conn) {
        return null;
    }

    public String alterar(EntidadeDominio entidade) {
        return "Telefone alterado com sucesso";
        // TODO Auto-generated method stub

    }

    public List<EntidadeDominio> consultar(EntidadeDominio entidade) {
        // TODO Auto-generated method stub
        return null;
    }

    public String excluir(EntidadeDominio entidade) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EntidadeDominio selecionar(EntidadeDominio entidade) {
        // TODO Auto-generated method stub
        return null;
    }
}
