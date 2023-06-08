package com.example.pizarrainteractivacourtcoach;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.HashMap;

public class MainActivity3 extends AppCompatActivity {

    private LinearLayout linearLayoutBanquillo;
    private ConstraintLayout constraintLayoutPista;
    private CardView[] cardViewsEnPista, cardViewsBanquillo; //Array de CardViews para almacenar todos los CardViews de los jugadores
    private HashMap<String, int[]> statsJugadoresMap = new HashMap<>();
    int numJugadoresConvocados = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        linearLayoutBanquillo = findViewById(R.id.linearLayoutJugadores);
        constraintLayoutPista = findViewById(R.id.constraintLayoutQuinteto);

        Button button_banquillo = findViewById(R.id.buttonBanquillo);

        //Array de CardViews para asignar cada CardView a cada jugador y marcador
        cardViewsEnPista = new CardView[5];
        cardViewsEnPista[0] = findViewById(R.id.cardViewBase);
        cardViewsEnPista[1] = findViewById(R.id.cardViewEscolta);
        cardViewsEnPista[2] = findViewById(R.id.cardViewAlero);
        cardViewsEnPista[3] = findViewById(R.id.cardViewAlaPivot);
        cardViewsEnPista[4] = findViewById(R.id.cardViewPivot);

        int numJugadoresBanquillo = numJugadoresConvocados - 5;
        cardViewsBanquillo = new CardView[numJugadoresBanquillo];

        inicializarStatsJugadores(numJugadoresConvocados);
        darTags();

        for (CardView cardView : cardViewsEnPista) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mostrarDialogo((String) cardView.getTag());
                }
            });
        }

        for (CardView cardView : cardViewsBanquillo) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mostrarDialogo((String) cardView.getTag());
                }
            });
        }

        mostrarEstadisticasJugadorPistaInicial();

        button_banquillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarBanquillo();
            }
        });
    }

    private void mostrarEstadisticasJugadorPistaInicial() {
        int[] jugadorStatsBase = obtenerStatsJugador("Jugador 1"); // Implementa esta función según tus datos
        statsJugadoresMap.put("Jugador 1", jugadorStatsBase);

        int[] jugadorStatsEscolta = obtenerStatsJugador("Jugador 2"); // Implementa esta función según tus datos
        statsJugadoresMap.put("Jugador 2", jugadorStatsEscolta);

        int[] jugadorStatsAlero = obtenerStatsJugador("Jugador 3"); // Implementa esta función según tus datos
        statsJugadoresMap.put("Jugador 3", jugadorStatsAlero);

        int[] jugadorStatsAlaPivot = obtenerStatsJugador("Jugador 4"); // Implementa esta función según tus datos
        statsJugadoresMap.put("Jugador 4", jugadorStatsAlaPivot);

        int[] jugadorStatsPivot = obtenerStatsJugador("Jugador 5"); // Implementa esta función según tus datos
        statsJugadoresMap.put("Jugador 5", jugadorStatsPivot);

        mostrarEstadisticasJugador(cardViewsEnPista[0], "Jugador 1");
        mostrarEstadisticasJugador(cardViewsEnPista[1], "Jugador 2");
        mostrarEstadisticasJugador(cardViewsEnPista[2], "Jugador 3");
        mostrarEstadisticasJugador(cardViewsEnPista[3], "Jugador 4");
        mostrarEstadisticasJugador(cardViewsEnPista[4], "Jugador 5");
    }

    private void mostrarBanquillo() {
        final Dialog dialog = new Dialog(MainActivity3.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);

        ImageView cerrarDialog = dialog.findViewById(R.id.imageViewCerrarDesplegable);
        LinearLayout jugadores = dialog.findViewById(R.id.linearLayoutJugadores);
        int numeroCardView = 0;

        for (int i = 6; i <= numJugadoresConvocados; i++) {
            CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.jugador, null);
            String tag = "Jugador " + i;
            cardView.setTag(tag);

            jugadores.addView(cardView);

            cardViewsBanquillo[numeroCardView] = cardView;
            numeroCardView++;

            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Obtener el tag del jugador arrastrado
                    String tag = (String) v.getTag();

                    // Iniciar el arrastre del CardView
                    ClipData.Item item = new ClipData.Item(tag);
                    ClipData clipData = new ClipData(tag, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(clipData, shadowBuilder, v, 0);

                    return true;
                }
            });

            // Agregar los datos del jugador al HashMap
            int[] jugadorStats = obtenerStatsJugador(tag); // Implementa esta función según tus datos
            statsJugadoresMap.put(tag, jugadorStats);

            // Mostrar los datos de las estadísticas en el CardView
            mostrarEstadisticasJugador(cardView, tag);
        }

        cerrarDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cerramos el Dialog
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //Le ajustamos las dimensiones
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //Le cambiamos el color de fondo
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Le aplicamos las animaciones
        dialog.getWindow().setGravity(Gravity.BOTTOM); //Lo iniciamos en la parte inferior de la pantalla
    }

    private void mostrarEstadisticasJugador(CardView cardView, String tag) {
        // Obtener las referencias a los elementos de texto en el CardView
        TextView textViewNombreJugador = cardView.findViewById(R.id.textViewNombreJugador);
        TextView textViewPuntosJugador = cardView.findViewById(R.id.textViewPuntosJugador);
        TextView textViewAsistenciasJugador = cardView.findViewById(R.id.textViewAsistenciasJugador);
        TextView textViewRebotesJugador = cardView.findViewById(R.id.textViewRebotesJugador);
        TextView textViewFaltasJugador = cardView.findViewById(R.id.textViewFaltasJugador);

        // Obtener los datos de las estadísticas del jugador desde el HashMap
        int[] jugadorStats = statsJugadoresMap.get(tag);

        // Establecer los valores de los TextView con los datos del jugador
        textViewNombreJugador.setText(tag);
        textViewPuntosJugador.setText(String.valueOf(jugadorStats[0]));
        textViewAsistenciasJugador.setText(String.valueOf(jugadorStats[9]));
        textViewRebotesJugador.setText(String.valueOf(jugadorStats[10]));
        textViewFaltasJugador.setText(String.valueOf(jugadorStats[15]));
    }

    private void actualizarEstadisticasJugador(CardView cardView, String tag) {
        // Mostrar las estadísticas actualizadas del jugador en el CardView
        mostrarEstadisticasJugador(cardView, tag);
    }

    private void darTags() {
        cardViewsEnPista[0].setTag("Jugador 1");
        cardViewsEnPista[1].setTag("Jugador 2");
        cardViewsEnPista[2].setTag("Jugador 3");
        cardViewsEnPista[3].setTag("Jugador 4");
        cardViewsEnPista[4].setTag("Jugador 5");
    }

    private void inicializarStatsJugadores(int numJugadoresConvocados) {
        for (int i = 1; i <= numJugadoresConvocados; i++) {
            String tagJugador = "Jugador " + i;
            int[] statsJugador = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            statsJugadoresMap.put(tagJugador, statsJugador);
        }
    }

    private void mostrarDialogo(String tagJugador) {
        //Creamos un objeto MaterialAlertDialogBuilder, que crea un Dialog en forma de CardView
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);

        //Creamos el inflater para convertir el XML con el Dialog personalizado en una vista para el Activity Main
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View dialogView = inflater.inflate(R.layout.estadisticas_jugador, null);

        //Asignamos nuestro Dialog personalizado para que se muestre este y no el predeterminado de Android Studio
        builder.setView(dialogView);

        //Método que crea un botón de control de la ventana del Dialog, que lo llamamos "Cerrar", que sirve para cerrar el CardView
        //Este botón sirve para mostrar todas las estadísticas del jugador seleccionado, no se puede editar nada
        builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {

            //Método OnClick del botón
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //Método que crea la ventana emergente del Dialog
        AlertDialog dialog = builder.create();

        //Modificamos lo que muestra el Dialog cuando se carga en pantalla, en este caso actualizamos el Dialog con las estadísticas del jugador seleccionado
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                //Definimos todos los TextViews del Dialog
                TextView pointsTotalTextView = dialog.findViewById(R.id.textViewPuntosAnotadosDialog);
                TextView threePointerMadeTextView = dialog.findViewById(R.id.textViewTriplesAnotadosDialog);
                TextView threePointerTotalTextView = dialog.findViewById(R.id.textViewTriplesTotalesDialog);
                TextView twoPointerMadeTextView = dialog.findViewById(R.id.textViewDosAnotadosDialog);
                TextView twoPointerTotalTextView = dialog.findViewById(R.id.textViewDosTotalesDialog);
                TextView freeThrowMadeTextView = dialog.findViewById(R.id.textViewLibresAnotadosDialog);
                TextView freeThrowTotalTextView = dialog.findViewById(R.id.textViewLibresTotalesDialog);
                TextView defensiveReboundTextView = dialog.findViewById(R.id.textViewRebotesDefensivosCogidosDialog);
                TextView offensiveReboundTextView = dialog.findViewById(R.id.textViewRebotesOfensivosCogidosDialog);
                TextView totalRebounds = dialog.findViewById(R.id.textViewRebotesTotalesCogidosDialog);
                TextView assistsTextView = dialog.findViewById(R.id.textViewAsistenciasDadasDialog);
                TextView stealTextView = dialog.findViewById(R.id.textViewRobosRealizadosDialog);
                TextView blockTextView = dialog.findViewById(R.id.textViewTaponesRealizadosDialog);
                TextView turnoverTextView = dialog.findViewById(R.id.textViewPerdidasAnotadasDialog);
                TextView receivedFoulTextView = dialog.findViewById(R.id.textViewFaltasRecibidasAnotadasDialog);
                TextView madeFoulTextView = dialog.findViewById(R.id.textViewFaltasCometidasAnotadasDialog);
                TextView valoracionTextView = dialog.findViewById(R.id.textViewValoracionConseguidaDialog);

                //Definimos un Array de ints con las estadísticas dependiendo del jugador seleccionado
                int[] playerStats = statsJugadoresMap.get(tagJugador);

                //Definimos que estadística se corresponde con cada posición del Array
                pointsTotalTextView.setText(String.valueOf(playerStats[0]));
                threePointerMadeTextView.setText(String.valueOf(playerStats[1]));
                threePointerTotalTextView.setText(String.valueOf(playerStats[2]));
                twoPointerMadeTextView.setText(String.valueOf(playerStats[3]));
                twoPointerTotalTextView.setText(String.valueOf(playerStats[4]));
                freeThrowMadeTextView.setText(String.valueOf(playerStats[5]));
                freeThrowTotalTextView.setText(String.valueOf(playerStats[6]));
                defensiveReboundTextView.setText(String.valueOf(playerStats[7]));
                offensiveReboundTextView.setText(String.valueOf(playerStats[8]));
                totalRebounds.setText(String.valueOf(playerStats[9]));
                assistsTextView.setText(String.valueOf(playerStats[10]));
                stealTextView.setText(String.valueOf(playerStats[11]));
                blockTextView.setText(String.valueOf(playerStats[12]));
                turnoverTextView.setText(String.valueOf(playerStats[13]));
                receivedFoulTextView.setText(String.valueOf(playerStats[14]));
                madeFoulTextView.setText(String.valueOf(playerStats[15]));
                valoracionTextView.setText(String.valueOf(playerStats[16]));
            }
        });

        //Método que muestra el Dialog por pantalla
        dialog.show();
    }

    private int[] obtenerStatsJugador(String tag) {
        // Implementa la lógica para obtener las estadísticas del jugador según el tag o cualquier otra fuente de datos
        // Aquí se muestra un ejemplo estático
        if (tag.equals("Jugador 1")) {
            return new int[]{3, 1, 6, 4, 8, 2, 7, 9, 0, 5, 10, 3, 7, 1, 2, 6, 4}; // Estadísticas del jugador 1
        } else if (tag.equals("Jugador 2")) {
            return new int[]{9, 4, 5, 1, 6, 3, 2, 0, 8, 10, 7, 2, 6, 3, 1, 4, 5}; // Estadísticas del jugador 2
        } else if (tag.equals("Jugador 3")) {
            return new int[]{1, 8, 7, 5, 0, 6, 4, 10, 2, 9, 3, 1, 7, 6, 4, 3, 5}; // Estadísticas del jugador 3
        } else if (tag.equals("Jugador 4")) {
            return new int[]{4, 7, 10, 3, 6, 2, 8, 0, 5, 9, 1, 7, 3, 4, 2, 5, 6}; // Estadísticas del jugador 4
        } else if (tag.equals("Jugador 5")) {
            return new int[]{5, 0, 2, 6, 1, 3, 7, 9, 4, 8, 10, 5, 1, 6, 3, 4, 2}; // Estadísticas del jugador 5
        } else if (tag.equals("Jugador 6")) {
            return new int[]{7, 3, 8, 5, 1, 4, 6, 2, 9, 0, 10, 7, 3, 1, 6, 4, 5}; // Estadísticas del base
        } else if (tag.equals("Jugador 7")) {
            return new int[]{2, 5, 0, 3, 6, 4, 8, 7, 1, 9, 10, 2, 4, 6, 5, 3, 1}; // Estadísticas del escolta
        } else if (tag.equals("Jugador 8")) {
            return new int[]{10, 4, 6, 1, 8, 5, 2, 7, 3, 9, 0, 6, 1, 3, 4, 2, 5}; // Estadísticas del alero
        } else if (tag.equals("Jugador 9")) {
            return new int[]{6, 2, 3, 0, 9, 1, 8, 4, 5, 10, 7, 2, 6, 4, 1, 3, 5}; // Estadísticas del ala pivot
        } else if (tag.equals("Jugador 10")) {
            return new int[]{2, 4, 8, 7, 0, 5, 1, 3, 9, 6, 10, 2, 4, 6, 1, 3, 5}; // Estadísticas del pivot
        } else if (tag.equals("Jugador 11")) {
            return new int[]{1, 7, 0, 9, 6, 4, 5, 8, 3, 10, 2, 4, 3, 5, 1, 6, 2}; // Estadísticas del pivot
        } else if (tag.equals("Jugador 12")) {
            return new int[]{5, 9, 6, 4, 0, 7, 1, 3, 8, 2, 10, 5, 6, 1, 4, 3, 2}; // Estadísticas del pivot
        }

        return new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // Si el tag no coincide, devuelve datos vacíos
    }
}