package com.antoinedrouin.enjoyfood.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.antoinedrouin.enjoyfood.Classes.GoogleLocation;
import com.antoinedrouin.enjoyfood.Classes.ServerSide;
import com.antoinedrouin.enjoyfood.Classes.Utilitaire;
import com.antoinedrouin.enjoyfood.R;

public class Compte extends AppCompatActivity {

    private Context context;
    private static Compte instCompte;

    private SharedPreferences pref;
    private SharedPreferences.Editor edit;

    private String script, methode, id, pseudo, newMdp1;

    private LinearLayout layoutMdp, layoutCoord;
    private EditText edtOldMdp, edtNewMdp1, edtNewMdp2, edtNewVille, edtNewCp, edtNewTel, edtNewAd;
    private RelativeLayout layoutLoading;

    private GoogleLocation googleLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte);

        context = getApplicationContext();
        instCompte = this;

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        id = pref.getString(getString(R.string.prefId), "");
        pseudo = pref.getString(getString(R.string.prefPseudo), "");
        String compte = pref.getString(getString(R.string.prefCompte), "");
        edit = pref.edit();

        layoutMdp = (LinearLayout) findViewById(R.id.layoutMdp);
        LinearLayout mainLayoutCoord = (LinearLayout) findViewById(R.id.mainLayoutCoord);
        layoutCoord = (LinearLayout) findViewById(R.id.layoutCoord);
        layoutLoading = (RelativeLayout) findViewById(R.id.loadingPanel);

        edtOldMdp = (EditText) findViewById(R.id.edtOldMdp);
        edtNewMdp1 = (EditText) findViewById(R.id.edtNewMdp1);
        edtNewMdp2 = (EditText) findViewById(R.id.edtNewMdp2);
        edtNewVille = (EditText) findViewById(R.id.edtNewVille);
        edtNewCp = (EditText) findViewById(R.id.edtNewCp);
        edtNewTel = (EditText) findViewById(R.id.edtNewTel);
        edtNewAd = (EditText) findViewById(R.id.edtNewAdresse);

        // Si le l'utilisateur est un manager, il n'a pas besoin, du layout de coordonnées
        if (compte.equals(getString(R.string.varGerant)))
            mainLayoutCoord.setVisibility(View.GONE);
        else {
            edtNewVille.setText(pref.getString(getString(R.string.prefVille), ""));
            edtNewCp.setText(pref.getString(getString(R.string.prefCp), ""));
            edtNewTel.setText(pref.getString(getString(R.string.prefTel), ""));
            edtNewAd.setText(pref.getString(getString(R.string.prefAdresse), ""));
        }
    }

    public void onClickDeco (View v) {
        deco();
        Toast.makeText(context, getString(R.string.disconnectionSuccess), Toast.LENGTH_SHORT).show();
    }

    public void onClickMdpChange(View v) {
        if (layoutMdp.getVisibility() == View.VISIBLE)
            layoutMdp.setVisibility(View.GONE);
        else
            layoutMdp.setVisibility(View.VISIBLE);
    }

    public void onClickMdpChangeValider(View v) {
        String oldMdp, newMdp2, error;

        error = "";
        oldMdp = edtOldMdp.getText().toString();
        newMdp1 = edtNewMdp1.getText().toString();
        newMdp2 = edtNewMdp2.getText().toString();

        // Test sur les champs
        if (oldMdp.length() < 3 || newMdp1.length() < 3 || newMdp2.length() < 3 )
            error = getString(R.string.errorMdpLength);
        else if (!newMdp1.equals(newMdp2))
            error = getString(R.string.errorMdpNotSame);
        else if (oldMdp.equals(newMdp1))
            error = getString(R.string.errorMdpSame);

        // Si tout est renseigné, lance la procédure de changemet de mot de passe
        if (!error.equals(""))
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
        else {
            if (!pref.getString(getString(R.string.prefMdp), "").equals(oldMdp))
                Toast.makeText(context, getString(R.string.errorOldMdp), Toast.LENGTH_SHORT).show();
            else {
                script = getString(R.string.changeMdp);
                methode = getString(R.string.write);

                ServerSide changePassword = new ServerSide(context);
                changePassword.execute(script, methode, pseudo, newMdp1);
            }
        }
    }

    public void onClickLocationAd(View v) {
        if (layoutLoading.getVisibility() == View.GONE) {
            layoutLoading.setVisibility(View.VISIBLE);
            googleLocation = new GoogleLocation(context, instCompte, 1);
        }
    }

    // Si trouve une localsiation correcte
    public void goodReturnLocation() {
        edtNewCp.setText(googleLocation.getCp());
        edtNewVille.setText(googleLocation.getCity());
        edtNewAd.setText(googleLocation.getAddress());
        layoutLoading.setVisibility(View.GONE);
    }

    // Si la localisation a échoué
    public void badReturnLocation(String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
        layoutLoading.setVisibility(View.GONE);
    }

    public void onClickCoordChange(View v) {
        if (layoutCoord.getVisibility() == View.VISIBLE)
            layoutCoord.setVisibility(View.GONE);
        else
            layoutCoord.setVisibility(View.VISIBLE);
    }

    public void onClickCoordChangeValider(View v) {
        String ville, cp, tel, ad;

        script = getString(R.string.changeCoordClient);
        methode = getString(R.string.write);
        ville = edtNewVille.getText().toString();
        cp = edtNewCp.getText().toString();
        tel = edtNewTel.getText().toString();
        ad = edtNewAd.getText().toString();

        ServerSide changeCoord = new ServerSide(context);
        changeCoord.execute(script, methode, id, ville, cp, tel, ad);
    }

    public void onClickSupprCompte(View v) {
        // Boite de dialogue pour confirmer la suppressino du compte
        new AlertDialog.Builder(this)
            .setMessage(getString(R.string.eraseCompteMessage))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.varNo), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(context, getString(R.string.eraseCompteFail), Toast.LENGTH_SHORT).show();
                }
            })
            .setPositiveButton(getString(R.string.varYes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    script = getString(R.string.eraseCompte);
                    methode = getString(R.string.write);

                    ServerSide eraseCompte = new ServerSide(context);
                    eraseCompte.execute(script, methode, pseudo);
                }
            })
            .show();
    }

    private void deco() {
        edit.clear();
        edit.apply();
        Tabs.getInstance().viewPager.setCurrentItem(0);

        // Création de la bdd si elle n'existe pas
        SQLiteDatabase dbEF = openOrCreateDatabase(getString(R.string.varDbName), Context.MODE_PRIVATE, null);
        // Création de la table si elle n'existe pas
        Utilitaire.createBaseCommande(dbEF);
        dbEF.execSQL("Delete From Commande");

        finish();
    }

    public void okMdp() {
        layoutMdp.setVisibility(View.GONE);
        edit.putString(getString(R.string.prefMdp), newMdp1);
        edit.apply();
        edtOldMdp.setText("");
        edtNewMdp1.setText("");
        edtNewMdp2.setText("");

        Toast.makeText(context, getString(R.string.changeMdpSuccess), Toast.LENGTH_SHORT).show();
    }

    public void okCoord() {
        layoutCoord.setVisibility(View.GONE);
        edit.putString(getString(R.string.prefVille), edtNewVille.getText().toString());
        edit.putString(getString(R.string.prefCp), edtNewCp.getText().toString());
        edit.putString(getString(R.string.prefTel), edtNewTel.getText().toString());
        edit.putString(getString(R.string.prefAdresse), edtNewAd.getText().toString());
        edit.apply();

        Toast.makeText(context, getString(R.string.changeCoordSuccess), Toast.LENGTH_SHORT).show();
    }

    public void okErase() {
        deco();
        Toast.makeText(context, getString(R.string.eraseCompteSuccess), Toast.LENGTH_SHORT).show();
    }

    public static Compte getInstance() {
        return instCompte;
    }

}
