package com.example.pizarrainteractivacourtcoach;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {

    private LinearLayout jugadores;
    private ConstraintLayout constraintLayoutPista;
    private HashMap<String, int[]> statsJugadoresMap;
    int numJugadoresConvocados = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //jugadores = findViewById(R.id.linearLayoutJugadores);
        constraintLayoutPista = findViewById(R.id.constraintLayoutQuinteto);

        Button button_banquillo = findViewById(R.id.buttonBanquillo);

        crearJugadores(numJugadoresConvocados);

        button_banquillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarBanquillo();
            }
        });
    }

    private void agregarCardViewsAlBanquillo() {
        //Crear y agregar los CardViews al banquillo (LinearLayout)
        for (int i = 1; i <= 5; i++) {
            CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.jugador, null);
            String tag = "Jugador " + i;
            cardView.setTag(tag);

            jugadores.addView(cardView);

            // Configurar el inicio de arrastre desde el banquillo
            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Iniciar el arrastre del CardView
                    ClipData.Item item = new ClipData.Item(tag);
                    ClipData clipData = new ClipData(tag, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(clipData, shadowBuilder, v, 0);

                    return true;
                }
            });

            // Mostrar los datos de las estadísticas en el CardView
            mostrarEstadisticasJugador(cardView, tag);
        }
    }

    public void mostrarBanquillo() {
        final Dialog dialog = new Dialog(MainActivity2.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);

        ImageView cerrarDialog = dialog.findViewById(R.id.imageViewCerrarDesplegable);
        LinearLayout jugadores = dialog.findViewById(R.id.linearLayoutJugadores);

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

    public void crearJugadores(int numJugadoresConvocados) {

        for (int i = 1; i <= numJugadoresConvocados; i++) {
            if (i <= 5) {
                String tagJugador = "Jugador" + i;
                View cardView = getLayoutInflater().inflate(R.layout.jugador, null);
                cardView.setTag(tagJugador);

                constraintLayoutPista.addView(cardView);
            } else {
                String tagJugador = "Jugador" + i;
                View cardView = getLayoutInflater().inflate(R.layout.jugador, null);
                cardView.setTag(tagJugador);

                jugadores.addView(cardView);
            }
        }
    }

    private void inicializarStatsJugadores(int numJugadoresConvocados) {
        for (int i = 1; i <= numJugadoresConvocados; i++) {
            String tagJugador = "Jugador " + i;
            int[] statsJugador = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            statsJugadoresMap.put(tagJugador, statsJugador);
        }
    }

    private int[] obtenerStatsJugador(String tag) {
        return statsJugadoresMap.get(tag); // Si el tag no coincide, devuelve datos vacíos
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
}



        /*linearLayout = findViewById(R.id.linearLayoutBanquillo);
        constraintLayoutPista = findViewById(R.id.constraintLayoutQuinteto);
        statsJugadoresMap = new HashMap<>();

        mostrarEstadisticasJugadorPistaInicial();

        // Agregar los CardViews al banquillo (LinearLayout)
        agregarCardViewsAlBanquillo();

        // Configurar el arrastre y soltar para los CardViews en la pista
        for (int i = 0; i < constraintLayoutPista.getChildCount(); i++) {
            CardView jugador = (CardView) constraintLayoutPista.getChildAt(i);
            jugador.setOnDragListener(new View.OnDragListener() {
                @SuppressLint("UseCompatLoadingForDrawables")
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case DragEvent.ACTION_DRAG_STARTED:
                        case DragEvent.ACTION_DRAG_EXITED:
                            v.setBackgroundColor(Color.GREEN); // Cambiar el color del fondo de la pista para indicar una posible posición de soltar
                            return true;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            v.setBackgroundColor(Color.BLUE); // Cambiar el color del fondo de la pista cuando el jugador se arrastra sobre ella
                            return true;
                        // Restaurar el color del fondo de la pista cuando el jugador sale de ella
                        case DragEvent.ACTION_DROP:
                            // Obtener el CardView arrastrado
                            CardView cardViewArrastrado = (CardView) event.getLocalState();

                            // Obtener el tag del CardView arrastrado
                            String tagArrastrado = (String) cardViewArrastrado.getTag();

                            // Obtener el tag del CardView de destino (Pista)
                            String tagDestino = (String) v.getTag();

                            // Obtener los datos del jugador arrastrado
                            int[] jugadorArrastradoStats = statsJugadoresMap.get(tagArrastrado);

                            // Obtener los datos del jugador en pista
                            int[] jugadorDestinoStats = statsJugadoresMap.get(tagDestino);

                            // Intercambiar los datos de estadísticas
                            statsJugadoresMap.put(tagArrastrado, jugadorDestinoStats);
                            statsJugadoresMap.put(tagDestino, jugadorArrastradoStats);

                            // Actualizar las estadísticas mostradas en los CardViews
                            actualizarEstadisticasJugador(cardViewArrastrado, tagDestino);
                            actualizarEstadisticasJugador((CardView) v, tagArrastrado);

                            v.setBackgroundColor(Color.WHITE); // Restaurar el color del fondo de la pista después de soltar
                            return true;
                        case DragEvent.ACTION_DRAG_ENDED:
                            v.setBackgroundColor(Color.WHITE); // Restaurar el color del fondo de la pista después de arrastrar
                            return true;
                        default:
                            break;
                    }
                    return true;
                }
            });
        }
    }

    private void mostrarEstadisticasJugadorPistaInicial() {
        CardView cardView_base = findViewById(R.id.cardViewBase);
        CardView cardView_escolta = findViewById(R.id.cardViewEscolta);
        CardView cardView_alero = findViewById(R.id.cardViewAlero);
        CardView cardView_alaPivot = findViewById(R.id.cardViewAlaPivot);
        CardView cardView_pivot = findViewById(R.id.cardViewPivot);

        cardView_base.setTag("Base");
        cardView_escolta.setTag("Escolta");
        cardView_alero.setTag("Alero");
        cardView_alaPivot.setTag("AlaPivot");
        cardView_pivot.setTag("Pivot");

        int[] jugadorStatsBase = obtenerStatsJugador("Base"); // Implementa esta función según tus datos
        statsJugadoresMap.put("Base", jugadorStatsBase);

        int[] jugadorStatsEscolta = obtenerStatsJugador("Escolta"); // Implementa esta función según tus datos
        statsJugadoresMap.put("Escolta", jugadorStatsEscolta);

        int[] jugadorStatsAlero = obtenerStatsJugador("Alero"); // Implementa esta función según tus datos
        statsJugadoresMap.put("Alero", jugadorStatsAlero);

        int[] jugadorStatsAlaPivot = obtenerStatsJugador("AlaPivot"); // Implementa esta función según tus datos
        statsJugadoresMap.put("AlaPivot", jugadorStatsAlaPivot);

        int[] jugadorStatsPivot = obtenerStatsJugador("Pivot"); // Implementa esta función según tus datos
        statsJugadoresMap.put("Pivot", jugadorStatsPivot);

        mostrarEstadisticasJugador(cardView_base, "Base");
        mostrarEstadisticasJugador(cardView_escolta, "Escolta");
        mostrarEstadisticasJugador(cardView_alero, "Alero");
        mostrarEstadisticasJugador(cardView_alaPivot, "AlaPivot");
        mostrarEstadisticasJugador(cardView_pivot, "Pivot");
    }



    private int[] obtenerStatsJugador(String tag) {
        // Implementa la lógica para obtener las estadísticas del jugador según el tag o cualquier otra fuente de datos
        // Aquí se muestra un ejemplo estático
        if (tag.equals("Jugador 1")) {
            return new int[]{10, 5, 3, 3}; // Estadísticas del jugador 1
        } else if (tag.equals("Jugador 2")) {
            return new int[]{8, 4, 2, 1}; // Estadísticas del jugador 2
        } else if (tag.equals("Jugador 3")) {
            return new int[]{6, 3, 1, 0}; // Estadísticas del jugador 3
        } else if (tag.equals("Jugador 4")) {
            return new int[]{4, 2, 0, 4}; // Estadísticas del jugador 4
        } else if (tag.equals("Jugador 5")) {
            return new int[]{2, 1, 0, 5}; // Estadísticas del jugador 5
        } else if (tag.equals("Base")) {
            return new int[]{4, 3, 5, 2}; // Estadísticas del base
        } else if (tag.equals("Escolta")) {
            return new int[]{1, 1, 3, 5}; // Estadísticas del escolta
        } else if (tag.equals("Alero")) {
            return new int[]{2, 4, 2, 1}; // Estadísticas del alero
        } else if (tag.equals("AlaPivot")) {
            return new int[]{5, 2, 3, 4}; // Estadísticas del ala pivot
        } else if (tag.equals("Pivot")) {
            return new int[]{1, 3, 4, 2}; // Estadísticas del pivot
        }

        return new int[]{0, 0, 0, 0}; // Si el tag no coincide, devuelve datos vacíos
    }*/