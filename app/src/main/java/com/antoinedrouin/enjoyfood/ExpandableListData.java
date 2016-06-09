package com.antoinedrouin.enjoyfood;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cdsm04 on 03/06/2016.
 */
public class ExpandableListData {

    public static void getData(Context context) {
        // Recherche les catégories et les consommables de l'établissement grâce à son id
        Bundle extras = Etablissement.getInstance().getIntent().getExtras();
        String idEt = extras.getString(context.getString(R.string.extraEtabId), "");

        ServerSide getMenu = new ServerSide(context);
        getMenu.execute(context.getString(R.string.getMenu), context.getString(R.string.read), idEt);
    }

    public static void assembleData(String[][] menu, Context context) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<>();
        List<String> categorie = new ArrayList<>();
        String actualCateg = "";

        for (int i = 0; i < menu.length; i++) {
            Log.i("marquage", "Menu : " + menu[i][1] + " " + menu[i][5]);

            // Si le manager a supprimé la catégorie mais qu'il restait des consommables dedans, on les affiches
            // dans une catégorie générique
            if (menu[i][5].equals("null"))
                menu[i][5] = context.getString(R.string.noCateg);

            // Initialise la catégorie
            if (actualCateg.equals("")) {
                actualCateg = menu[i][5];
            }

            // Si le consommable appartient à la même catégorie, on l'ajoute à la liste
            if (menu[i][5].equals(actualCateg)) {
                categorie.add(menu[i][1]);
            }
            // Sinon on passe à la catégorie suivante
            else {
                expandableListDetail.put(actualCateg, categorie);
                categorie = new ArrayList<>();
                categorie.add(menu[i][1]);
                actualCateg = menu[i][5];
            }

            // Dernier tour de boucle, on ajoute la catégorie
            if (i == menu.length - 1) {
                expandableListDetail.put(actualCateg, categorie);
            }
        }

        Menu.getInstance().fillExpLstMenu(expandableListDetail);
    }
}
