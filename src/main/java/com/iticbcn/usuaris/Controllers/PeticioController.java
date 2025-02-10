package com.iticbcn.usuaris.Controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.SessionFactory;


import com.iticbcn.usuaris.DAO.PeticioDAO;
import com.iticbcn.usuaris.DAO.UsuariDAO;
import com.iticbcn.usuaris.Model.Peticio;
import com.iticbcn.usuaris.Model.Usuari;

public class PeticioController {

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
        Boolean addUser = true;
        Set<Usuari> users = new HashSet<Usuari>();
        String opt ;

        UsuariDAO udao = new UsuariDAO(sf);

        System.out.println("INSERIR NOVA PETICIO i USUARI");
        System.out.print("Introduir una descripcio: ");
        p.setDescPeticio(LecturaEntrada(bf));
        p.setDataIniPeticio(LocalDateTime.now());
        p.setEstatPeticio("Activa");

        while (addUser) {
            System.out.print("Introdueix el DNI de l'usuari: ");
            String dni = LecturaEntrada(bf);

        // Comprovar si ja existeix
            Usuari usuari = udao.getUsuariByDNI(dni);
                        
            if (usuari == null) {
                usuari = NouUsuari(bf,dni);
                p.addUsuari(usuari);
            } else {
                System.out.println("Usuari existent trobat: " + usuari.getNomUsuari());
                users.add(usuari);
            }



            System.out.print("Vols introduir un altre usuari? (N per no introduir) ");
            opt = LecturaEntrada(bf);

            if (opt.equalsIgnoreCase("N")){
                addUser = false;
            }
        }

        PeticioDAO petdao = new PeticioDAO(sf);
        petdao.PersistirPeticio(p);

        //Aquesta condició permetrà afegir aquells usuaris existents a la petició

        if (!users.isEmpty()) {
        //En aquest punt creem un set d'usuaris per emmagatzemar temporalment els usuaris ja afegits
            Set<Usuari> uset = p.getUsuaris();
        //recorrem per afegir els usuaris existents a uset
            for (Usuari u:users) {
                uset.add(u);
            }
        //canviem el set d'usuaris per l'uset, que conté ja tots els usuaris
            p.setUsuaris(uset);
        //Modifiquem petició
            petdao.ModificarPeticio(p);
        }
    }

    public static Usuari NouUsuari(BufferedReader bf, String dni) throws Exception {

        Usuari usuari = null;

        System.out.println("Usuari no existent, creant un de nou...");
        System.out.print("Introdueix el nom: ");
        String nom = LecturaEntrada(bf);
        System.out.print("Introdueix el rol: ");
        String rol = LecturaEntrada(bf);
        usuari = new Usuari(nom, dni, rol);
                
        return usuari;
    }

    public static void ModificarPeticio(BufferedReader bf, SessionFactory sf) throws Exception {
        int idPeticio;
        Peticio p = null;
        Set<Usuari> users = new HashSet<Usuari>();
        PeticioDAO petdao = new PeticioDAO(sf);
        UsuariDAO udao = new UsuariDAO(sf);
        boolean addUser = true;
    
        while (p == null) {  // Repetir fins que s'obtingui una petició vàlida
            try {
                System.out.print("Quina és la id de la petició? : ");
                idPeticio = Integer.parseInt(LecturaEntrada(bf));
                p = petdao.ObtenirPeticio(idPeticio);
    
                if (p == null) {
                    System.out.println("No existeix cap petició amb aquesta ID.");
                } 

            } catch (NumberFormatException e) {
                System.out.println("ID no vàlida, introdueix un nombre enter.");
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
            petdao.ModificarPeticio(p);
        } else if (entrada.equalsIgnoreCase("b")) {
            
            while (addUser) {
                System.out.print("Introdueix el DNI de l'usuari: ");
                String dni = LecturaEntrada(bf);
            // Comprovar si ja existeix
                Usuari usuari = udao.getUsuariByDNI(dni);

                if (usuari == null) {
                    usuari = NouUsuari(bf, dni);
                /*cal persistir l'usuari per separat, el mètode merge de petició no ho farà */
                    udao.PersistirUsuari(usuari);
                } else {
                    System.out.println("Usuari existent trobat: " + usuari.getNomUsuari());

                }
                
                users.add(usuari);

                System.out.print("Vols introduir un altre usuari? (N per no introduir) ");

                if (LecturaEntrada(bf).equalsIgnoreCase("N")){
                    addUser = false;
                }
            }

            if (!users.isEmpty()) {
                //En aquest punt creem un set d'usuaris per emmagatzemar temporalment els usuaris ja afegits
                    Set<Usuari> uset = p.getUsuaris();
                //recorrem per afegir els usuaris existents a uset
                    for (Usuari u:users) {
                        uset.add(u);
                    }
                //canviem el set d'usuaris per l'uset, que conté ja tots els usuaris
                    p.setUsuaris(uset);
                //Modifiquem petició
                    petdao.ModificarPeticio(p);
                }
        }

    }

    public static void EsborrarPeticio (BufferedReader bf, SessionFactory sf) throws Exception {

        int idPeticio;
        Peticio p = null;
        PeticioDAO petdao = new PeticioDAO(sf);


        //Esborrem sols Peticio, respectem els usuaris

        while (p == null) {  // Repetir fins que s'obtingui una petició vàlida
            try {
                System.out.print("Quina és la id de la petició? : ");
                idPeticio = Integer.parseInt(LecturaEntrada(bf));
                p = petdao.ObtenirPeticio(idPeticio);
    
                if (p == null) {
                    System.out.println("No existeix cap petició amb aquesta ID.");
                } 

            } catch (NumberFormatException e) {
                System.out.println("ID no vàlida, introdueix un nombre enter.");
            } catch (Exception e) {
                System.err.println("Error inesperat: " + e.getMessage());
                throw e;  
            }
        }

        System.out.println("Aneu a esborrar la petició amb descripció: " + p.getDescPeticio());
        System.out.print("Premeu S per confirmar: ");

        String entrada = LecturaEntrada(bf); 

        if (entrada.equalsIgnoreCase("s")) {
            petdao.EsborrarPeticio(p);
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
