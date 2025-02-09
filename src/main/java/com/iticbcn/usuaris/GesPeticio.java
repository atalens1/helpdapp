package com.iticbcn.usuaris;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.crypto.spec.PBEKeySpec;

import org.hibernate.SessionFactory;

import com.iticbcn.usuaris.DAO.PeticioDAO;
import com.iticbcn.usuaris.Model.Peticio;
import com.iticbcn.usuaris.Model.Usuari;

public class GesPeticio {

    public static void LlistarPeticions(SessionFactory sf) throws Exception {

        PeticioDAO petdao = new PeticioDAO(sf);

        for (Peticio p:petdao.LlistarPeticions()) {
            System.out.println("------------------------------------");
            System.out.println("Id Petició: " +  p.getIdPeticio());
            System.out.println("Desc Petició: " +  p.getDescPeticio());
            System.out.println("Data Inici Petició: " + p.getDataIniPeticio());
            System.out.println("Estat petició: " +  p.getEstatPeticio());
            for (Usuari u:p.getUsuaris()) {
                System.out.println("Id Usuari: " + u.getIdUsuari());
                System.out.println("Nom Usuari: " + u.getNomUsuari());
                System.out.println("DNI Usuari: " + u.getDniUsuari());
                System.out.println("Rol Usuari: " + u.getRolUsuari());
            }
            System.out.println("------------------------------------");
        }
    }
    
    public static void NovaPeticioUsuari(BufferedReader bf,SessionFactory sf) throws Exception {

        Peticio p = new Peticio();
        Boolean nouUser = true;
        String opt ;

        System.out.println("INSERIR NOVA PETICIO i USUARI");
        System.out.print("Introduir una descripcio: ");
        p.setDescPeticio(LecturaEntrada(bf));
        p.setDataIniPeticio(LocalDateTime.now());
        p.setEstatPeticio("Activa");

        while (nouUser) {
            p.addUsuari(NouUsuari(bf));
            System.out.print("Vols introduir un altre usuari? (N per no introduir) ");
            opt = LecturaEntrada(bf);

            if (opt.equals("N")){
                nouUser = false;
            }
        }

        PeticioDAO petdao = new PeticioDAO(sf);
        petdao.PersistirPeticio(p);
}

    public static Usuari NouUsuari(BufferedReader bf) {
        Usuari u = new Usuari();

        System.out.print("Nom de l'usuari: ");
        u.setNomUsuari(LecturaEntrada(bf));
        System.out.print("DNI de l'usuari: ");
        u.setDniUsuari(LecturaEntrada(bf));
        System.out.print("Rol de l'usuari: ");
        u.setRolUsuari(LecturaEntrada(bf));

        return u;
    }
    
    public static String LecturaEntrada(BufferedReader bf) {
        String str1 = null;
        try {
            str1 = bf.readLine();
        } catch (IOException e) {
            System.err.println("Error d'entrada: " + e.getMessage());
        } catch (Exception ex) {
            System.err.println("Error general: " + ex.getMessage());
        }

        return str1;
    }
}
