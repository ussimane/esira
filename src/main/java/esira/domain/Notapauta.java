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
import javax.persistence.JoinColumns;
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
@Table(name = "notapauta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Notapauta.findAll", query = "SELECT n FROM Notapauta n")})
public class Notapauta implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NotapautaPK notapautaPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "nota", precision = 8, scale = 8)
    private Float nota;
    @Column(name = "classif", length = 45)
    private String classif;
    @Column(name = "publicado")
    private Boolean publicado;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "idestudante", referencedColumnName = "id_estudante", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estudante estudante;
    @JoinColumns({
        @JoinColumn(name = "iddisc", referencedColumnName = "iddisc", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "ano", referencedColumnName = "ano", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "semestre", referencedColumnName = "semestre", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "datap", referencedColumnName = "datap", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Pauta pauta;
//    @Column(name = "descricao", length = 64)
//    private String descricao;

    public Notapauta() {
    }

    public Notapauta(NotapautaPK notapautaPK) {
        this.notapautaPK = notapautaPK;
    }

    public Notapauta(long iddisc, int ano, short semestre, Date datap, long idestudante) {
        this.notapautaPK = new NotapautaPK(iddisc, ano, semestre, datap, idestudante);
    }

    public NotapautaPK getNotapautaPK() {
        return notapautaPK;
    }

    public void setNotapautaPK(NotapautaPK notapautaPK) {
        this.notapautaPK = notapautaPK;
    }

    public Float getNota() {
        return nota;
    }

    public void setNota(Float nota) {
        this.nota = nota;
    }

    public String getClassif() {
        return classif;
    }

    public void setClassif(String classif) {
        this.classif = classif;
    }

    public Boolean getPublicado() {
        return publicado;
    }

    public void setPublicado(Boolean publicado) {
        this.publicado = publicado;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public void setEstudante(Estudante estudante) {
        this.estudante = estudante;
    }

    public Pauta getPauta() {
        return pauta;
    }

    public void setPauta(Pauta pauta) {
        this.pauta = pauta;
    }

//    public String getDescricao() {
//        return descricao;
//    }
//
//    public void setDescricao(String descricao) {
//        this.descricao = descricao;
//    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notapautaPK != null ? notapautaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notapauta)) {
            return false;
        }
        Notapauta other = (Notapauta) object;
        if ((this.notapautaPK == null && other.notapautaPK != null) || (this.notapautaPK != null && !this.notapautaPK.equals(other.notapautaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Notapauta[ notapautaPK=" + notapautaPK + " ]";
    }
    
}
