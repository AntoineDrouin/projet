package com.antoinedrouin.enjoyfood.Classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.antoinedrouin.enjoyfood.R;

import java.math.BigDecimal;

/**
 * Created by cdsm04 on 14/09/2016.
 *
 * Classe contenant quelques méthodes utiles
 */

public class Utilitaire {

    public static void putUtilisateur(Context context, Utilisateur ut) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = pref.edit();

        edit.putString(context.getString(R.string.prefId), ut.getId());
        edit.putString(context.getString(R.string.prefPseudo), ut.getPseudo());
        edit.putString(context.getString(R.string.prefMdp), ut.getMdp());
        edit.putString(context.getString(R.string.prefCompte), ut.getCompte());
        edit.putString(context.getString(R.string.prefNom), ut.getNom());
        edit.putString(context.getString(R.string.prefPrenom), ut.getPrenom());

        if (ut.getCompte().equals(context.getString(R.string.varClient))) {
            edit.putString(context.getString(R.string.prefVille), ut.getVille());
            edit.putString(context.getString(R.string.prefCp), ut.getCp());
            edit.putString(context.getString(R.string.prefTel), ut.getTel());
            edit.putString(context.getString(R.string.prefAdresse), ut.getAdresse());
        }

        edit.apply();
    }

    public static String returnStringFromBool(Boolean bo) {
        if (bo)
            return "1";
        else
            return "0";
    }

    public static Boolean returnBoolFromString(String st) {
        return st.equals("1");
    }

    public static void createBasePanier(SQLiteDatabase dbEF) {
        dbEF.execSQL("CREATE TABLE IF NOT EXISTS Panier (idEt VARCHAR, nomEt VARCHAR, nomConso VARCHAR, qteConso INTEGER, prixConso DOUBLE)");
    }

    public static void createBaseEtab(SQLiteDatabase dbEF) {
        dbEF.execSQL("CREATE TABLE IF NOT EXISTS Etablissement (idEt VARCHAR, nomEt VARCHAR, adresseEt VARCHAR, villeEt VARCHAR, codePostalEt VARCHAR, telEt VARCHAR, prixLivrEt DOUBLE)");
    }

    public static void createBaseCommande(SQLiteDatabase dbEF) {
        dbEF.execSQL("CREATE TABLE IF NOT EXISTS Commande (idCom VARCHAR, idEt VARCHAR, etatCom VARCHAR, prixTotalCom VARCHAR)");
    }

    public static double round(double d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String returnFullAdressOrNull(Context context, SharedPreferences pref) {
       String adresse, cp, ville;

        adresse = pref.getString(context.getString(R.string.prefAdresse), "");
        cp = pref.getString(context.getString(R.string.prefCp), "");
        ville = pref.getString(context.getString(R.string.prefVille), "");

        if (adresse.equals("") || cp.equals("") || ville.equals(""))
            return "";
        else
            return adresse + " " + cp + " " + ville;
    }
}
