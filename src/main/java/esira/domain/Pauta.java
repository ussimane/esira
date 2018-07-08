/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "pauta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pauta.findAll", query = "SELECT p FROM Pauta p")})
public class Pauta implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PautaPK pautaPK;
    @Column(name = "descricao", length = 64)
    private String descricao;
    @Column(name = "formula", length = 2147483647)
    private String formula;
    @Column(name = "obs", length = 2147483647)
    private String obs;
    @Column(name = "publicada")
    private Boolean publicada;
    @Column(name = "valido")
    private Short valido;
    @Column(name = "tipo")
    private Short tipo;
    @Column(name = "percent")
    private Integer percent;
    @JoinColumn(name = "iddisc", referencedColumnName = "id_disc", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Disciplina disciplina;
    @JoinColumn(name = "docente", referencedColumnName = "iddocente")
    @ManyToOne(fetch = FetchType.LAZY)
    private Docente docente;
    @JoinColumn(name = "funcionario", referencedColumnName = "id_funcionario")
    @ManyToOne(fetch = FetchType.LAZY)
    private Funcionario funcionario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pauta", fetch = FetchType.LAZY)
    private List<Notapauta> notapautaList;
    @Column(name = "turno")
    private Integer turno;
    @Column(name = "turma")
    private Integer turma;

    public Pauta() {
    }

    public Pauta(PautaPK pautaPK) {
        this.pautaPK = pautaPK;
    }

    public Pauta(long iddisc, int ano, short semestre, Date datap) {
        this.pautaPK = new PautaPK(iddisc, ano, semestre, datap);
    }

    public PautaPK getPautaPK() {
        return pautaPK;
    }

    public void setPautaPK(PautaPK pautaPK) {
        this.pautaPK = pautaPK;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Integer getTurno() {
        return turno;
    }

    public void setTurno(Integer turno) {
        this.turno = turno;
    }

    public Integer getTurma() {
        return turma;
    }

    public void setTurma(Integer turma) {
        this.turma = turma;
    }
    
    

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }
    
    

    public Boolean getPublicada() {
        return publicada;
    }

    public void setPublicada(Boolean publicada) {
        this.publicada = publicada;
    }

    public Short getValido() {
        return valido;
    }

    public void setValido(Short valido) {
        this.valido = valido;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Short getTipo() {
        return tipo;
    }

    public void setTipo(Short tipo) {
        this.tipo = tipo;
    }
    
    

    @XmlTransient
    public List<Notapauta> getNotapautaList() {
        return notapautaList;
    }

    public void setNotapautaList(List<Notapauta> notapautaList) {
        this.notapautaList = notapautaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pautaPK != null ? pautaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pauta)) {
            return false;
        }
        Pauta other = (Pauta) object;
        if ((this.pautaPK == null && other.pautaPK != null) || (this.pautaPK != null && !this.pautaPK.equals(other.pautaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Pauta[ pautaPK=" + pautaPK + " ]";
    }
    
}
