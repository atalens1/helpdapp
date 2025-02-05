package com.iticbcn.usuaris.Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ForeignKey;

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
    
    @ManyToMany(cascade=CascadeType.PERSIST,fetch=FetchType.LAZY)
    @JoinTable(name="UsuPeticio",
    joinColumns = { @JoinColumn(name="idUsuari",foreignKey = @ForeignKey(name="FK_UP_USUARI")) },
    inverseJoinColumns = { @JoinColumn(name="idPeticio",foreignKey = @ForeignKey(name="FK_UP_PETICIO")) })
    private Set<Usuari> usuaris = new HashSet<>();

    public Set<Usuari> getUsuaris() {
        return usuaris;
     }
  
    public void addUsuari(Usuari u) {
            
        if(!this.usuaris.contains(u)) {
              this.usuaris.add(u);
        }
        
           u.getPeticio().add(this);
    }

    public Peticio() {}


}





