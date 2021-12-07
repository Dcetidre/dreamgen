package com.software.ddk.dreamgen.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.software.ddk.dreamgen.R;
import com.software.ddk.dreamgen.fragment.avatarview;

public class Act_Raw extends AppCompatActivity {

    private LinearLayout bt_raw_accept, bt_raw_share;

    private EditText et_raw_cej, et_raw_outfit, et_raw_bkg, et_raw_front, et_raw_hair1, et_raw_hair2, et_raw_hairacc1, et_raw_hairacc2,
    et_raw_bodyacc1, et_raw_bodyacc2, et_raw_faceacc1, et_raw_faceacc2, et_raw_backacc1, et_raw_backacc2;
    private EditText et_raw_front2, et_raw_hairacc3, et_raw_bodyacc3, et_raw_faceacc3, et_raw_backacc3;
    private EditText et_raw_eye1, et_raw_eye2, et_raw_pup1, et_raw_pup2, et_raw_tam, et_raw_skin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bt_raw_accept = findViewById(R.id.bt_raw_accept);
        bt_raw_share = findViewById(R.id.bt_raw_share);

        et_raw_outfit = findViewById(R.id.et_raw_outfit);
        et_raw_bkg = findViewById(R.id.et_raw_bkg);
        et_raw_front = findViewById(R.id.et_raw_front);
        et_raw_hair1 = findViewById(R.id.et_raw_hair1);
        et_raw_hair2 = findViewById(R.id.et_raw_hair2);
        et_raw_hairacc1 = findViewById(R.id.et_raw_hairacc1);
        et_raw_hairacc2 = findViewById(R.id.et_raw_hairacc2);
        et_raw_bodyacc1 = findViewById(R.id.et_raw_bodyacc1);
        et_raw_bodyacc2 = findViewById(R.id.et_raw_bodyacc2);
        et_raw_faceacc1 = findViewById(R.id.et_raw_faceacc1);
        et_raw_faceacc2 = findViewById(R.id.et_raw_faceacc2);
        et_raw_backacc1 = findViewById(R.id.et_raw_backacc1);
        et_raw_backacc2 = findViewById(R.id.et_raw_backacc2);
        et_raw_eye1 = findViewById(R.id.et_raw_eye1);
        et_raw_eye2 = findViewById(R.id.et_raw_eye2);
        et_raw_pup1 = findViewById(R.id.et_raw_pup1);
        et_raw_pup2 = findViewById(R.id.et_raw_pup2);
        et_raw_tam = findViewById(R.id.et_raw_tam);
        et_raw_skin = findViewById(R.id.et_raw_skin);
        et_raw_cej = findViewById(R.id.et_raw_cej);

        et_raw_front2 = findViewById(R.id.et_raw_front2);
        et_raw_faceacc3 = findViewById(R.id.et_raw_faceacc3);
        et_raw_bodyacc3 = findViewById(R.id.et_raw_bodyacc3);
        et_raw_hairacc3= findViewById(R.id.et_raw_hairacc3);
        et_raw_backacc3 = findViewById(R.id.et_raw_backacc3);


        //recibir datos para los edittext
        receive_main_data();

        bt_raw_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_main_data();
            }
        });

        bt_raw_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                on_share_text(avatarview.setname);
            }
        });

    }

    private void on_share_text(String setname){
        String text_body =
                          "-------------------------------------" + "\n" +
                          " Nombre: " + setname + "\n" +
                          "-------------------------------------" + "\n" +
                          "* Rostro y Cuerpo:" + "\n" +
                          "------------------" + "\n" +
                          "Ojo 1: " + et_raw_eye1.getText().toString() + "\n" +
                          "Ojo 2: " + et_raw_eye2.getText().toString() + "\n" +
                          "Pupila 1: " + et_raw_pup1.getText().toString() + "\n" +
                          "Pupila 2: " + et_raw_pup2.getText().toString() + "\n" +
                          "Altura: " + et_raw_tam.getText().toString() + "\n" +
                          "Tono de piel: " + et_raw_skin.getText().toString() + "\n" +
                          "Tipo de Bellos Faciales: " + et_raw_cej.getText().toString() + "\n" +
                          "-----------" + "\n" +
                          "* Conjunto:" + "\n" +
                          "-----------" + "\n" +
                          "Outfit: " + et_raw_outfit.getText().toString() + "\n" +
                          "Background: " + et_raw_bkg.getText().toString() + "\n" +
                          "Front: " + et_raw_front.getText().toString() + "\n" +
                          "Hair1: " + et_raw_hair1.getText().toString() + "\n" +
                          "Hair2: " + et_raw_hair2.getText().toString() + "\n" +
                          "Hair Accesory1: " + et_raw_hairacc1.getText().toString() + "\n" +
                          "Hair Accesory2: " + et_raw_hairacc2.getText().toString() + "\n" +
                          "Body Accesory1: " + et_raw_bodyacc1.getText().toString() + "\n" +
                          "Body Accesory2: " + et_raw_bodyacc2.getText().toString() + "\n" +
                          "Face Accesory1: " + et_raw_faceacc1.getText().toString() + "\n" +
                          "Face Accesory2: " + et_raw_faceacc2.getText().toString() + "\n" +
                          "Back Accesory1: " + et_raw_backacc1.getText().toString() + "\n" +
                          "Back Accesory2: " + et_raw_backacc2.getText().toString() + "\n" +
                          "-----------" + "\n" +
                          "* Extras:" + "\n" +
                          "-----------" + "\n" +
                          "Hair Accesory3: " + et_raw_hairacc3.getText().toString() + "\n" +
                          "Body Accesory3: " + et_raw_bodyacc3.getText().toString() + "\n" +
                          "Face Accesory3: " + et_raw_faceacc3.getText().toString() + "\n" +
                          "Back Accesory3: " + et_raw_backacc3.getText().toString() + "\n" +
                          "Front2: " + et_raw_front2.getText().toString() + "\n" +
                          "----------------------------------------\n";

        Intent shar_intent = new Intent(Intent.ACTION_SEND);
        shar_intent.setType("text/plain");
        shar_intent.putExtra(Intent.EXTRA_SUBJECT, "DreamGen Waifu IDs");
        shar_intent.putExtra(Intent.EXTRA_TEXT, text_body);

        startActivity(Intent.createChooser(shar_intent, "Compartir Via..."));

    }

    private void receive_main_data(){
        et_raw_outfit.setText(avatarview.outfit);
        et_raw_bkg.setText(avatarview.bkg);
        et_raw_front.setText(avatarview.front);
        et_raw_hair1.setText(avatarview.hair);
        et_raw_hair2.setText(avatarview.hair2);
        et_raw_hairacc1.setText(avatarview.hairacc1);
        et_raw_hairacc2.setText(avatarview.hairacc2);
        et_raw_bodyacc1.setText(avatarview.bodyacc1);
        et_raw_bodyacc2.setText(avatarview.bodyacc2);
        et_raw_faceacc1.setText(avatarview.faceacc1);
        et_raw_faceacc2.setText(avatarview.faceacc2);
        et_raw_backacc1.setText(avatarview.backacc1);
        et_raw_backacc2.setText(avatarview.backacc2);
        et_raw_eye1.setText(avatarview.face_eye1);
        et_raw_eye2.setText(avatarview.face_eye2);
        et_raw_pup1.setText(avatarview.face_pup1);
        et_raw_pup2.setText(avatarview.face_pup2);
        et_raw_tam.setText(avatarview.face_tam);
        et_raw_skin.setText(avatarview.face_skin);
        et_raw_cej.setText(avatarview.face_cej);

        et_raw_front2.setText(avatarview.front2);
        et_raw_hairacc3.setText(avatarview.hairacc3);
        et_raw_bodyacc3.setText(avatarview.bodyacc3);
        et_raw_faceacc3.setText(avatarview.faceacc3);
        et_raw_backacc3.setText(avatarview.backacc3);


    }

    private void send_main_data(){
        avatarview.outfit = et_raw_outfit.getText().toString();
        avatarview.bkg = et_raw_bkg.getText().toString();
        avatarview.front = et_raw_front.getText().toString();
        avatarview.hair = et_raw_hair1.getText().toString();
        avatarview.hair2 = et_raw_hair2.getText().toString();
        avatarview.hairacc1 = et_raw_hairacc1.getText().toString();
        avatarview.hairacc2 = et_raw_hairacc2.getText().toString();
        avatarview.bodyacc1 = et_raw_bodyacc1.getText().toString();
        avatarview.bodyacc2 = et_raw_bodyacc2.getText().toString();
        avatarview.faceacc1 = et_raw_faceacc1.getText().toString();
        avatarview.faceacc2 = et_raw_faceacc2.getText().toString();
        avatarview.backacc1 = et_raw_backacc1.getText().toString();
        avatarview.backacc2 = et_raw_backacc2.getText().toString();
        avatarview.face_eye1 = et_raw_eye1.getText().toString();
        avatarview.face_eye2 = et_raw_eye2.getText().toString();
        avatarview.face_pup1 = et_raw_pup1.getText().toString();
        avatarview.face_pup2 = et_raw_pup2.getText().toString();
        avatarview.face_tam = et_raw_tam.getText().toString();
        avatarview.face_skin = et_raw_skin.getText().toString();
        avatarview.face_cej = et_raw_cej.getText().toString();

        avatarview.front2 = et_raw_front2.getText().toString();
        avatarview.hairacc3 = et_raw_hairacc3.getText().toString();
        avatarview.bodyacc3 = et_raw_bodyacc3.getText().toString();
        avatarview.faceacc3 = et_raw_faceacc3.getText().toString();
        avatarview.backacc3 = et_raw_backacc3.getText().toString();

        setResult(RESULT_OK);
        finish();

    }

}
