package com.iticbcn.usuaris.Model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table
public class Usuari implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUsuari;
    @Column(unique=true)
    private String nomUsuari;
    @Column(unique=true)
    private String dniUsuari;
    @Column
    private String rolUsuari;

    @ManyToMany(cascade=CascadeType.PERSIST,fetch=FetchType.LAZY,mappedBy = "usuaris")
    private Set<Peticio> peticions = new HashSet<>();;
    
    public Set<Peticio> getPeticio() {
        return peticions;
    }

    public void addPeticio(Peticio p) {
        if(!this.peticions.contains(p)){
            peticions.add(p);
            }
        
        p.getUsuaris().add(this);
    }

    public Usuari() {}

}
