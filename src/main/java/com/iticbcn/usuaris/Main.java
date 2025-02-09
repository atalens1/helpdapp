package com.iticbcn.usuaris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

import org.hibernate.SessionFactory;

import com.iticbcn.usuaris.DAO.PeticioDAO;
import com.iticbcn.usuaris.Model.Peticio;
import com.iticbcn.usuaris.Model.Usuari;

public class Main {
    public static void main(String[] args) throws Exception {

        SessionFactory sf = HibernateUtil.getSessionFactory();

        try (BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Gestio de Peticions i Usuaris");
            System.out.println("=============================");
            MostrarMenu(bf,sf);

        } catch (IOException ioe) {
            System.err.println("Error d'entrada");
        }



 /*        Peticio p1 = new Peticio("Rentar els plats",LocalDateTime.now(),"Activa");
        Usuari u1 = new Usuari("Ramon","99999999","SysAdmin");
        Usuari u2 = new Usuari("Marta","77777777","Developer");
        p1.addUsuari(u1);
        p1.addUsuari(u2);

        PeticioDAO petdao = new PeticioDAO(sf);

        petdao.PersistirPeticio(p1);
      */

    }

    public static void MostrarMenu(BufferedReader bf, SessionFactory sf) {
        int opcio = 0;
        
        System.out.println("Triar una opció del menú");
        System.out.println("1. Crear Peticio i usuari");
        System.out.println("2. Crear Usuari");
        System.out.println("3. Llistar Peticions i usuaris");
        System.out.println("4. Esmenar Peticio");
        System.out.println("5. Esborrar Peticio");
        System.out.println("6. Sortir");

        try {
            opcio = Integer.parseInt(LecturaEntrada(bf));
            if (GestioMenu(opcio, bf, sf)) {
                MostrarMenu(bf, sf);
            } else {
                System.out.println("Adéu");
            }

        } catch (Exception ex) {
            System.out.println("Opcio no vàlida");
            MostrarMenu(bf, sf);
        } 
    }

    public static Boolean GestioMenu(int opcio, BufferedReader bf, SessionFactory sf) throws Exception {
        Boolean continuar = true;
        String indAccio;

        switch(opcio) {
            case 1:
                GesPeticio.NovaPeticioUsuari(bf,sf);
                break;
            case 2:
                break;
            case 3:
                GesPeticio.LlistarPeticions(sf);
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                continuar = false;
                break;
            default:
        }

        if (continuar == true) {
            System.out.print("Voleu fer cap altra acció? Marqueu N per sortir: ");
            indAccio = LecturaEntrada(bf);
            if (indAccio.equalsIgnoreCase("n")) {
                continuar = false;
            }
        }

        return continuar;
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