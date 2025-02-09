package com.iticbcn.usuaris.Model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/*Aquesta classe es mapeja amb el fitxer Usuari.hbm.xml que hi ha a resources */

public class Usuari implements Serializable {

    private int idUsuari;

    private String nomUsuari;

    private String dniUsuari;

    private String rolUsuari;

    private Set<Peticio> peticions = new HashSet<>();
    
    public Set<Peticio> getPeticions() {
        return peticions;
    }

    public void addPeticio(Peticio p) {
        if (p != null && !this.peticions.contains(p)) {
            this.peticions.add(p);
            p.getUsuaris().add(this);  
        }
    }
        
    public void setPeticions(Set<Peticio> peticions) {
        this.peticions = peticions;
    }

    public Usuari() {}

    public Usuari(String nomUsuari, String dniUsuari, String rolUsuari) {
        this.nomUsuari = nomUsuari;
        this.dniUsuari = dniUsuari;
        this.rolUsuari = rolUsuari;
    }

    public int getIdUsuari() {
        return idUsuari;
    }

    public void setIdUsuari(int idUsuari) {
        this.idUsuari = idUsuari;
    }

    public String getNomUsuari() {
        return nomUsuari;
    }

    public void setNomUsuari(String nomUsuari) {
        this.nomUsuari = nomUsuari;
    }

    public String getDniUsuari() {
        return dniUsuari;
    }

    public void setDniUsuari(String dniUsuari) {
        this.dniUsuari = dniUsuari;
    }

    public String getRolUsuari() {
        return rolUsuari;
    }

    public void setRolUsuari(String rolUsuari) {
        this.rolUsuari = rolUsuari;
    }
}
