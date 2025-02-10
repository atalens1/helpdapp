package com.iticbcn.usuaris;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;

import org.hibernate.SessionFactory;


import com.iticbcn.usuaris.DAO.PeticioDAO;
import com.iticbcn.usuaris.DAO.UsuariDAO;
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
            p.addUsuari(NouUsuari(bf,sf));
            System.out.print("Vols introduir un altre usuari? (N per no introduir) ");
            opt = LecturaEntrada(bf);

            if (opt.equalsIgnoreCase("N")){
                nouUser = false;
            }
        }

        PeticioDAO petdao = new PeticioDAO(sf);
        petdao.PersistirPeticio(p);
    }

    public static Usuari NouUsuari(BufferedReader bf, SessionFactory sf) throws Exception {
            System.out.print("Introdueix el DNI de l'usuari: ");
            String dni = LecturaEntrada(bf);

        // Comprovar si ja existeix
            UsuariDAO udao = new UsuariDAO(sf);
            Usuari usuari = udao.getUsuariByDNI(dni);
                        
            if (usuari == null) {
                System.out.println("Usuari no existent, creant un de nou...");
                System.out.print("Introdueix el nom: ");
                String nom = LecturaEntrada(bf);
                System.out.print("Introdueix el rol: ");
                String rol = LecturaEntrada(bf);
                usuari = new Usuari(nom, dni, rol);
            } else {
                System.out.println("Usuari existent trobat: " + usuari.getNomUsuari());
            }
            
            return usuari;
    }

    public static void ModificarPeticio(BufferedReader bf, SessionFactory sf) throws Exception {
        int idPeticio;
        Peticio p = null;
        PeticioDAO petdao = new PeticioDAO(sf);
        UsuariDAO udao = new UsuariDAO(sf);
    
        while (p == null) {  // Repetir fins que s'obtingui una petició vàlida
            try {
                System.out.print("Quina és la id de la petició? : ");
                idPeticio = Integer.parseInt(LecturaEntrada(bf));
                p = petdao.ObtenirPeticio(idPeticio);
    
                if (p == null) {
                    System.out.println("No existeix cap petició amb aquesta ID.");
                } 

            } catch (NumberFormatException e) {
                System.out.println("ID no vàlida, introdueix un número enter.");
            } catch (Exception e) {
                System.err.println("Error inesperat: " + e.getMessage());
                throw e;  
            }
        }
    
        System.out.println("Indiqueu l'acció a fer amb la petició");
        System.out.println("A. Modificar l'estat");
        System.out.println("B. Afegir usuaris");

        String entrada = LecturaEntrada(bf); 

        if (entrada.equalsIgnoreCase("a")) {
            System.out.print("Indiqueu el nou estat (Tancada) (En progrés)");
            p.setEstatPeticio(LecturaEntrada(bf));
        } else if (entrada.equalsIgnoreCase("b")) {
            Boolean nouUser = true;
            while (nouUser) {
                Usuari u = NouUsuari(bf, sf);
                /*cal persistir l'usuari per separat, el mètode merge no ho farà */
                udao.PersistirUsuari(u);
                p.addUsuari(u);
                System.out.print("Vols introduir un altre usuari? (N per no introduir) ");
                if (LecturaEntrada(bf).equalsIgnoreCase("N")){
                    nouUser = false;
                }
            }
            petdao.ModificarPeticio(p);
        }
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
