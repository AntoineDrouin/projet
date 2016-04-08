package com.antoinedrouin.enjoyfood;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Coordonnees extends Fragment {

    Context context;
    static Coordonnees instCoord;
    View view;

    TextView txtCpEt, txtVilleEt;

    String etab, cp, ville, adresse, tel, desc, prixLivr, conges;

    public static Coordonnees newInstance() {
        Coordonnees fragment = new Coordonnees();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();
        instCoord = this;
        etab = ((TextView) Etablissement.getInstance().findViewById(R.id.txtNomEtab)).getText().toString();

        // Requête pour trouver les données

        ServerSide getEtabInfo = new ServerSide(context);
        getEtabInfo.execute(getString(R.string.getEtabByName), getString(R.string.read), etab);
    }

    public void getInfos(String cCp, String cVille, String cAdresse, String cTel, String cDesc, String cPrixLivr, String cConges) {
        cp = cCp;
        ville = cVille;
        adresse = cAdresse;
        tel = cTel;
        desc = cDesc;
        prixLivr = cPrixLivr;
        conges = cConges;
        setCompo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_coordonnees, container, false);

        txtCpEt = (TextView) view.findViewById(R.id.txtCodePostalEt);
        txtVilleEt = (TextView) view.findViewById(R.id.txtVilleEt);

        // Remplissage des champs
        setCompo();

        return view;
    }

    private void setCompo() {
        txtCpEt.setText(cp);
        txtVilleEt.setText(ville);
    }

    public static Coordonnees getInstance() {
        return instCoord;
    }
}
