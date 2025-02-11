package com.iticbcn.usuaris.Views;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.SessionFactory;

import com.iticbcn.usuaris.Controllers.PeticioController;
import com.iticbcn.usuaris.Controllers.UsuariController;
import com.iticbcn.usuaris.Model.Peticio;
import com.iticbcn.usuaris.Model.Usuari;

public class InputView {

        public static void MostrarMenu(BufferedReader bf, SessionFactory sf) {
        boolean continuar = true;
    
        while (continuar) {
            mostrarOpcions(); // Mostra el menú
    
            try {
                int opcio = Integer.parseInt(LecturaEntrada(bf)); // Llegeix l'opció
                
                switch (opcio) {
                    case 1:
                        PeticioController.AfegirPeticioUsuari(bf, sf);
                        break;
                    case 2:
                        UsuariController.AfegirUsuari(bf, sf);
                        break;
                    case 3:
                        MostrarPeticions(PeticioController.LlistarPeticions(sf));
                        break;
                    case 4:
                        PeticioController.ModificarPeticio(bf, sf);
                        break;
                    case 5:
                        PeticioController.EsborrarPeticio(bf, sf);
                        break;
                    case 6:
                        MostrarUsuaris(UsuariController.LlistarUsuaris(sf));
                        break;
                    case 7:
                        continuar = false; // Finalitza el bucle si l'usuari vol sortir
                        System.out.println("Adéu!");
                        break;
                    default:
                        System.out.print("Opció no vàlida! Prem 'S' per tornar a veure el menú: ");
                        if (!LecturaEntrada(bf).equalsIgnoreCase("s")) {
                            continuar = false; // Si l'usuari no prem 'S', surt del bucle
                        }
                }
            } catch (NumberFormatException ex) {
                System.out.println("Entrada no vàlida. Si us plau, introdueix un número.");
            } catch (Exception ex) {
                System.err.println("Error general: " + ex.getMessage());
            }
        }
    }
    
    private static void mostrarOpcions() {
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Crear Petició i Usuari");
        System.out.println("2. Crear Usuari");
        System.out.println("3. Llistar Peticions amb els seus Usuaris");
        System.out.println("4. Esmenar Petició");
        System.out.println("5. Esborrar Petició");
        System.out.println("6. Llistar Usuaris");
        System.out.println("7. Sortir");
        System.out.print("Tria una opció: ");
    }

    public static Peticio NovaPeticioUsuari(BufferedReader bf) {

        Peticio p = new Peticio();

        System.out.println("INSERIR NOVA PETICIO i USUARI");
        System.out.print("Introduir una descripcio: ");
        p.setDescPeticio(LecturaEntrada(bf));
        p.setDataIniPeticio(LocalDateTime.now());
        p.setEstatPeticio("Activa");

        return p;
    }

    public static String DemanarDNIUsuari(BufferedReader bf) {
        System.out.print("Introdueix el DNI de l'usuari: ");
        return LecturaEntrada(bf);

    }

    public static Usuari DemanarDadesUsuari(BufferedReader bf, String dni) throws Exception {

        Usuari usuari = null;

        System.out.println("Usuari no existent, creant un de nou...");
        System.out.print("Introdueix el nom: ");
        String nom = LecturaEntrada(bf);
        System.out.print("Introdueix el rol: ");
        String rol = LecturaEntrada(bf);
        usuari = new Usuari(nom, dni, rol);
                
        return usuari;
    }

    public static void MostrarPeticions(List<Peticio> peticions) {
        for (Peticio p : peticions) {
            System.out.println("------------------------------------");
            System.out.println("Id Petició: " + p.getIdPeticio());
            System.out.println("Desc Petició: " + p.getDescPeticio());
            System.out.println("Data Inici Petició: " + p.getDataIniPeticio());
            System.out.println("Estat petició: " + p.getEstatPeticio());
            for (Usuari u : p.getUsuaris()) {
                System.out.println("Usuari: " + u.getNomUsuari() + " - Doc. Identificació: " 
                + u.getDniUsuari() + " - Rol: " + u.getRolUsuari());
            }
            System.out.println("------------------------------------");
        }
    }

    public static void MostrarUsuaris(List<Usuari> usuaris) {
        for (Usuari u: usuaris) {
            System.out.println("------------------------------------");
            System.out.println("Id de l'usuari: " + u.getIdUsuari());
            System.out.println("DNI de l'usuari: " +  u.getDniUsuari());
            System.out.println("Nom de l'usuari: " +  u.getNomUsuari());
            System.out.println("Rol de l'usuari: " + u.getRolUsuari());
            System.out.println("------------------------------------");
        }
    }

    public static int DemanarIdPeticio(BufferedReader bf) {
        int IdP = 0;
        System.out.print("Quina és la id de la petició? : ");
        try {
            IdP = Integer.parseInt(LecturaEntrada(bf));
        } catch (NumberFormatException e) {
            System.out.println("ID no vàlida, introdueix un nombre enter.");
        } catch (Exception e) {
            System.err.println("Error inesperat: " + e.getMessage());
            throw e;  
        }

        return IdP;
    }

    public static String DemanarAccioModificarPeticio(BufferedReader bf) {
        System.out.println("Indiqueu l'acció a fer amb la petició");
        System.out.println("A. Modificar l'estat");
        System.out.println("B. Afegir usuaris");
        return LecturaEntrada(bf);
    }

    public static String ConfirmacioEsborrament(BufferedReader bf, String DescPet) {
        System.out.println("Aneu a esborrar la petició amb descripció: " + DescPet);
        System.out.print("Premeu S per confirmar: ");
        return LecturaEntrada(bf);
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
