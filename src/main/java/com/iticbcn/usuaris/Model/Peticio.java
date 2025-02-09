package com.iticbcn.usuaris.Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.ForeignKey;

@Entity
@Table(name = "Peticio")
public class Peticio implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPeticio;
    @Column
    private String descPeticio;
    @Column
    private LocalDateTime dataIniPeticio;
    @Column
    private String estatPeticio;
    
    @ManyToMany(cascade=CascadeType.PERSIST,fetch=FetchType.EAGER)
    @JoinTable(name="Usuari_Peticio",
    joinColumns = { @JoinColumn(name="idPeticio",foreignKey = @ForeignKey(name="FK_UP_PETICIO")) },
    inverseJoinColumns = { @JoinColumn(name="idUsuari",foreignKey = @ForeignKey(name="FK_UP_USUARI")) } )
    private Set<Usuari> usuaris = new HashSet<>();

    public Set<Usuari> getUsuaris() {
        return usuaris;
    }
  
    public void addUsuari(Usuari u) {
        if (u != null && !this.usuaris.contains(u)) {
            this.usuaris.add(u);
            u.getPeticions().add(this); 
        }
    }
    
    public Peticio() {}

    public Peticio(String descPeticio, LocalDateTime dataIniPeticio, String estatPeticio) {
        this.descPeticio = descPeticio;
        this.dataIniPeticio = dataIniPeticio;
        this.estatPeticio = estatPeticio;
    }

    public int getIdPeticio() {
        return idPeticio;
    }

    public void setIdPeticio(int idPeticio) {
        this.idPeticio = idPeticio;
    }

    public String getDescPeticio() {
        return descPeticio;
    }

    public void setDescPeticio(String descPeticio) {
        this.descPeticio = descPeticio;
    }

    public LocalDateTime getDataIniPeticio() {
        return dataIniPeticio;
    }

    public void setDataIniPeticio(LocalDateTime dataIniPeticio) {
        this.dataIniPeticio = dataIniPeticio;
    }

    public String getEstatPeticio() {
        return estatPeticio;
    }

    public void setEstatPeticio(String estatPeticio) {
        this.estatPeticio = estatPeticio;
    }

    public void setUsuaris(Set<Usuari> usuaris) {
        this.usuaris = usuaris;
    }

    @Override
    public String toString() {
        return "Peticio [idPeticio=" + idPeticio + ", descPeticio=" + descPeticio + ", dataIniPeticio=" + dataIniPeticio
                + ", estatPeticio=" + estatPeticio + "]";
    }

    

}





