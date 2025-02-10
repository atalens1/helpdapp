package com.iticbcn.usuaris.Controllers;

import java.io.BufferedReader;
import java.io.IOException;

import org.hibernate.SessionFactory;

import com.iticbcn.usuaris.DAO.PeticioDAO;
import com.iticbcn.usuaris.DAO.UsuariDAO;
import com.iticbcn.usuaris.Model.Peticio;
import com.iticbcn.usuaris.Model.Usuari;

public class UsuariController {

    public static void AfegirUsuari(BufferedReader bf,SessionFactory sf) throws Exception {

        Usuari u = new Usuari();
        UsuariDAO udao = new UsuariDAO(sf);

        System.out.println("INSERIR NOU USUARI");
        System.out.print("Introdueix el DNI de l'usuari: ");
        String dni = LecturaEntrada(bf);

    // Comprovar si ja existeix
        u = udao.getUsuariByDNI(dni);

        if (u == null) {
            u = NouUsuari(bf, dni);
            udao.PersistirUsuari(u);
        } else {
            System.out.println("Usuari existent trobat: " + u.getNomUsuari());
            System.out.println("Tornant a menú principal");
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

    public static void LlistarUsuaris(SessionFactory sf) throws Exception {

        UsuariDAO udao = new UsuariDAO(sf);

        for (Usuari u:udao.LlistarUsuaris()) {
            System.out.println("------------------------------------");
            System.out.println("Id de l'usuari: " + u.getIdUsuari());
            System.out.println("DNI de l'usuari: " +  u.getDniUsuari());
            System.out.println("Nom de l'usuari: " +  u.getNomUsuari());
            System.out.println("Rol de l'usuari: " + u.getRolUsuari());
            System.out.println("------------------------------------");
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
