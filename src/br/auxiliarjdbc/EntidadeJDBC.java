/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.auxiliarjdbc;

import br.auxiliarjdbc.util.ExcecaoDao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author krisnamourtscf
 */
public abstract class EntidadeJDBC<T> {

    private HashMap<Integer, Object> campos = new HashMap();
    private Integer indiceCampo = 1;
    private Integer comandoEscolhido = -1421515;
    private HashMap<Integer, String> comandosSQL = new HashMap();
    private ExcecaoJDBC excecao;

    public abstract Connection getConexao();

    public abstract T consultar() throws ExcecaoDao;

    public abstract List listar() throws ExcecaoDao;

    public void incluir() throws ExcecaoDao {
        executaComando();
    }

    public void atualizar() throws ExcecaoDao {
        executaComando();
    }

    public void remover() throws ExcecaoDao {
        executaComando();
    }

    public void adicionaComandoSQL(Integer identificador, String consultaSQL) throws ExcecaoDao {
        if (verificaIndicesRepetidoOuNegativo(identificador)) {
            throw new ExcecaoDao("ExcecaoJDBC: Indice inválido!\nValores não podem ser repetidos ou negativos.");
        }
        comandosSQL.put(identificador, consultaSQL);
    }

    private Boolean verificaIndicesRepetidoOuNegativo(Integer identificador) {
        for (Integer i : comandosSQL.keySet()) {
            if (i == identificador || i < 0) {
                return true;
            }
        }
        return false;
    }

    public void escolherComandoSQL(Integer identificador) {
        comandoEscolhido = identificador;
    }

    public String getComandoSQL() throws ExcecaoDao {
        if (comandoEscolhido == -1421515) {
            throw new ExcecaoDao("ExcecaoJDBC: Sem comando a ser executado");
        }
        return comandosSQL.get(comandoEscolhido);
    }

    private void adicionaCampo(Object valor) {
        if (valor instanceof java.util.Date) {
            valor = new Date(((java.util.Date) valor).getTime());
        } else if (valor instanceof java.util.Calendar) {
            java.util.Date dataTmp = ((java.util.Calendar) valor).getTime();
            valor = new Date(dataTmp.getTime());
        }
        campos.put(indiceCampo, valor);
        indiceCampo++;
    }

    public void adicionaCampos(Object... valor) {
        for (Object o : valor) {
            adicionaCampo(o);
        }
    }

    public void removeCampos() {
        campos = new HashMap();
        indiceCampo = 1;
    }

    public void preencheComando() throws ExcecaoDao {
        try {
            PreparedStatement comando = getConexao().prepareCall(getComandoSQL());
            preencheValores(comando);
        } catch (SQLException ex) {
            throw new ExcecaoDao(ex.getMessage());
        }
    }

    public void preencheComando(PreparedStatement comando) throws ExcecaoDao {
        try {
            preencheValores(comando);
        } catch (SQLException ex) {
            throw new ExcecaoDao(ex.getMessage());
        }
    }

    public void preencheComando(String comandoSQL) throws ExcecaoDao {
        try {
            PreparedStatement comando = getConexao().prepareCall(comandoSQL);
            preencheValores(comando);
        } catch (SQLException ex) {
            throw new ExcecaoDao(ex.getMessage());
        }
    }

    public void preencheComando(Object... valores) throws ExcecaoDao {
        try {
            PreparedStatement comando = getConexao().prepareCall(getComandoSQL());
            adicionaCampos(valores);
            preencheValores(comando);
        } catch (SQLException ex) {
            throw new ExcecaoDao(ex.getMessage());
        }
    }

    public void preencheComando(PreparedStatement comando, Object... valores) throws ExcecaoDao {
        try {
            adicionaCampos(valores);
            preencheValores(comando);
        } catch (SQLException ex) {
            throw new ExcecaoDao(ex.getMessage());
        }
    }

    public void preencheComando(String comandoSQL, Object... valores) throws ExcecaoDao {
        try {
            PreparedStatement comando = getConexao().prepareCall(comandoSQL);
            adicionaCampos(valores);
            preencheValores(comando);
        } catch (SQLException ex) {
            throw new ExcecaoDao(ex.getMessage());
        }
    }

    private void preencheValores(PreparedStatement cmd) throws SQLException {
        if (campos.isEmpty()) {
            return;
        }
        for (int i = 1; i < indiceCampo; i++) {
            cmd.setObject(i, campos.get(i));
        }
    }

    public void executaComando() throws ExcecaoDao {
        Executor exec = new ExecutorSQL();
        exec.execucaoModificacao(this);
    }

    public ResultSet executaConsulta() throws ExcecaoDao, SQLException {
        Executor exec = new ExecutorSQL();
        return exec.execucaoConsulta(this);
    }

    public void finalizaConexaoComSeguranca(Connection con) throws ExcecaoDao {
        try {
            if (con != null) {
                con.close();
            }
        } catch (Exception ex) {
            throw new ExcecaoDao(this.getClass().getName() + ": " + ex.getMessage());
        }
    }

    public void finalizaConexaoComSeguranca(Connection con, PreparedStatement cmd) throws ExcecaoDao {
        try {
            if (con != null) {
                con.close();
            }
            if (cmd != null) {
                cmd.close();
            }
        } catch (Exception ex) {
            throw new ExcecaoDao(this.getClass().getName() + ": " + ex.getMessage());
        }
    }

    /**
     * @return the excecao
     */
    public ExcecaoJDBC getExcecao() {
        return excecao;
    }

    /**
     * @param excecao the excecao to set
     */
    public void setExcecao(ExcecaoJDBC excecao) {
        this.excecao = excecao;
    }

}
