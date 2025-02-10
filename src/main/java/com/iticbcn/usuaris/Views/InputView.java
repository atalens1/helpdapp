package com.iticbcn.usuaris.Views;

import java.io.BufferedReader;
import java.io.IOException;

import org.hibernate.SessionFactory;

import com.iticbcn.usuaris.Controllers.PeticioController;
import com.iticbcn.usuaris.Controllers.UsuariController;

public class InputView {

        public static void MostrarMenu(BufferedReader bf, SessionFactory sf) {
        boolean continuar = true;
    
        while (continuar) {
            mostrarOpcions(); // Mostra el menú
    
            try {
                int opcio = Integer.parseInt(LecturaEntrada(bf)); // Llegeix l'opció
                
                switch (opcio) {
                    case 1:
                        PeticioController.NovaPeticioUsuari(bf, sf);
                        break;
                    case 2:
                        UsuariController.AfegirUsuari(bf, sf);
                        break;
                    case 3:
                        PeticioController.LlistarPeticions(sf);
                        break;
                    case 4:
                        PeticioController.ModificarPeticio(bf, sf);
                        break;
                    case 5:
                        PeticioController.EsborrarPeticio(bf, sf);
                        break;
                    case 6:
                        UsuariController.LlistarUsuaris(sf);
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

    private static String LecturaEntrada(BufferedReader bf) {
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
