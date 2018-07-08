/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "planoavaliacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Planoavaliacao.findAll", query = "SELECT p FROM Planoavaliacao p")})
public class Planoavaliacao implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PlanoavaliacaoPK planoavaliacaoPK;
    @Column(name = "avaliacao", length = 2147483647)
    private String avaliacao;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "peso", precision = 8, scale = 8)
    private Float peso;
    @JoinColumn(name = "iddisc", referencedColumnName = "id_disc", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Disciplina disciplina;

    public Planoavaliacao() {
    }

    public Planoavaliacao(PlanoavaliacaoPK planoavaliacaoPK) {
        this.planoavaliacaoPK = planoavaliacaoPK;
    }

    public Planoavaliacao(long iddisc, int turno, int turma, Date dataavaliacao) {
        this.planoavaliacaoPK = new PlanoavaliacaoPK(iddisc, turno, turma, dataavaliacao);
    }

    public PlanoavaliacaoPK getPlanoavaliacaoPK() {
        return planoavaliacaoPK;
    }

    public void setPlanoavaliacaoPK(PlanoavaliacaoPK planoavaliacaoPK) {
        this.planoavaliacaoPK = planoavaliacaoPK;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Float getPeso() {
        return peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (planoavaliacaoPK != null ? planoavaliacaoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Planoavaliacao)) {
            return false;
        }
        Planoavaliacao other = (Planoavaliacao) object;
        if ((this.planoavaliacaoPK == null && other.planoavaliacaoPK != null) || (this.planoavaliacaoPK != null && !this.planoavaliacaoPK.equals(other.planoavaliacaoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Planoavaliacao[ planoavaliacaoPK=" + planoavaliacaoPK + " ]";
    }
    
}
