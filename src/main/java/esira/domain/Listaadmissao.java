/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "listaadmissao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Listaadmissao.findAll", query = "SELECT l FROM Listaadmissao l")})
public class Listaadmissao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idaluno", nullable = false)
    private Integer idaluno;
    @Column(name = "numero", length = 45)
    private String numero;
    @Column(name = "nome", length = 45)
    private String nome;
    @Column(name = "nr_b_i", length = 45)
    private String nrBI;
    @Column(name = "telefone", length = 45)
    private String telefone;
    @Column(name = "ano")
    private Integer ano;
    @Column(name = "tipoad", length = 2147483647)
    private String tipoad;
    @Column(name = "matriculado")
    private Boolean matriculado;
    @JoinColumn(name = "curso", referencedColumnName = "id_curso", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Curso curso;
    @JoinColumn(name = "id_estudante", referencedColumnName = "id_estudante")
    @ManyToOne(fetch = FetchType.LAZY)
    private Estudante idEstudante;
    @Column(name = "turno")
    private Integer turno;

    public Listaadmissao() {
    }

    public Listaadmissao(Integer idaluno) {
        this.idaluno = idaluno;
    }

    public Integer getIdaluno() {
        return idaluno;
    }

    public void setIdaluno(Integer idaluno) {
        this.idaluno = idaluno;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNrBI() {
        return nrBI;
    }

    public void setNrBI(String nrBI) {
        this.nrBI = nrBI;
    }

    public Integer getTurno() {
        return turno;
    }

    public void setTurno(Integer turno) {
        this.turno = turno;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getTipoad() {
        return tipoad;
    }

    public void setTipoad(String tipoad) {
        this.tipoad = tipoad;
    }

    public Boolean getMatriculado() {
        return matriculado;
    }

    public void setMatriculado(Boolean matriculado) {
        this.matriculado = matriculado;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Estudante getIdEstudante() {
        return idEstudante;
    }

    public void setIdEstudante(Estudante idEstudante) {
        this.idEstudante = idEstudante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idaluno != null ? idaluno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Listaadmissao)) {
            return false;
        }
        Listaadmissao other = (Listaadmissao) object;
        if ((this.idaluno == null && other.idaluno != null) || (this.idaluno != null && !this.idaluno.equals(other.idaluno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Listaadmissao[ idaluno=" + idaluno + " ]";
    }

}
