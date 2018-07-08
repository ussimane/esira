/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Ussimane
 */
@Embeddable
public class NotapautaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "iddisc", nullable = false)
    private long iddisc;
    @Basic(optional = false)
    @Column(name = "ano", nullable = false)
    private int ano;
    @Basic(optional = false)
    @Column(name = "semestre", nullable = false)
    private short semestre;
    @Basic(optional = false)
    @Column(name = "datap", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date datap;
    @Basic(optional = false)
    @Column(name = "idestudante", nullable = false)
    private long idestudante;

    public NotapautaPK() {
    }

    public NotapautaPK(long iddisc, int ano, short semestre, Date datap, long idestudante) {
        this.iddisc = iddisc;
        this.ano = ano;
        this.semestre = semestre;
        this.datap = datap;
        this.idestudante = idestudante;
    }

    public long getIddisc() {
        return iddisc;
    }

    public void setIddisc(long iddisc) {
        this.iddisc = iddisc;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public short getSemestre() {
        return semestre;
    }

    public void setSemestre(short semestre) {
        this.semestre = semestre;
    }

    public Date getDatap() {
        return datap;
    }

    public void setDatap(Date datap) {
        this.datap = datap;
    }

    public long getIdestudante() {
        return idestudante;
    }

    public void setIdestudante(long idestudante) {
        this.idestudante = idestudante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) iddisc;
        hash += (int) ano;
        hash += (int) semestre;
        hash += (datap != null ? datap.hashCode() : 0);
        hash += (int) idestudante;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotapautaPK)) {
            return false;
        }
        NotapautaPK other = (NotapautaPK) object;
        if (this.iddisc != other.iddisc) {
            return false;
        }
        if (this.ano != other.ano) {
            return false;
        }
        if (this.semestre != other.semestre) {
            return false;
        }
        if ((this.datap == null && other.datap != null) || (this.datap != null && !this.datap.equals(other.datap))) {
            return false;
        }
        if (this.idestudante != other.idestudante) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.NotapautaPK[ iddisc=" + iddisc + ", ano=" + ano + ", semestre=" + semestre + ", datap=" + datap + ", idestudante=" + idestudante + " ]";
    }
    
}
