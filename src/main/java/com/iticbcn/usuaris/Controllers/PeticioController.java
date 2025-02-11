package com.iticbcn.usuaris.Controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;

import com.iticbcn.usuaris.DAO.PeticioDAO;
import com.iticbcn.usuaris.DAO.UsuariDAO;
import com.iticbcn.usuaris.Model.Peticio;
import com.iticbcn.usuaris.Model.Usuari;
import com.iticbcn.usuaris.Views.InputView;

public class PeticioController {

    public static List<Peticio> LlistarPeticions(SessionFactory sf) throws Exception {

        PeticioDAO petdao = new PeticioDAO(sf);
        return petdao.LlistarPeticions();
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
            String dni = InputView.DemanarDNIUsuari(bf);

        // Comprovar si ja existeix
            Usuari usuari = udao.getUsuariByDNI(dni);
                        
            if (usuari == null) {
                usuari = InputView.DemanarDadesUsuari(bf,dni);
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

    public static void ModificarPeticio(BufferedReader bf, SessionFactory sf) throws Exception {
        int idPeticio;
        Peticio p = null;
        Set<Usuari> users = new HashSet<Usuari>();
        PeticioDAO petdao = new PeticioDAO(sf);
        UsuariDAO udao = new UsuariDAO(sf);
        boolean addUser = true;
    
        while (p == null) {  // Repetir fins que s'obtingui una petició vàlida
            idPeticio = InputView.DemanarIdPeticio(bf);
            p = petdao.ObtenirPeticio(idPeticio);
        }
            
        String entrada = InputView.DemanarAccioModificarPeticio(bf);

        if (entrada.equalsIgnoreCase("a")) {
            System.out.print("Indiqueu el nou estat (Tancada) o (En progrés): ");
            p.setEstatPeticio(LecturaEntrada(bf));
            petdao.ModificarPeticio(p);
        } else if (entrada.equalsIgnoreCase("b")) {
            
            while (addUser) {
                String dni = InputView.DemanarDNIUsuari(bf);
            // Comprovar si ja existeix
                Usuari usuari = udao.getUsuariByDNI(dni);

                if (usuari == null) {
                    usuari = InputView.DemanarDadesUsuari(bf, dni);
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
            idPeticio = InputView.DemanarIdPeticio(bf);
            p = petdao.ObtenirPeticio(idPeticio);
        }

        String entrada = InputView.ConfirmacioEsborrament(bf, p.getDescPeticio()); 

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
