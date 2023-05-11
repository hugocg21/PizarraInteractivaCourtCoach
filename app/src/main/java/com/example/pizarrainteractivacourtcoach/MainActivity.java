package com.example.pizarrainteractivacourtcoach;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private ImageButton imageButton_Play, imageButton_Stop; //Botontes de Play y Stop del marcador
    private TextView textView_tiempo, textView_puntosLocal, textView_puntosVisitante, textView_periodo, textView_faltasLocal, textView_faltasVisitante; //Atributos del marcador
    private TextView textView_puntosJugadorCardView, textView_rebotesJugadorCardView, textView_asistenciasJugadorCardView; //Estadisticas en el CardView
    private CountDownTimer timer; //Timer para el timepo de cuarto
    private boolean tiempoCorriendo = false; //Define si el tiempo esta en corriendo o parado
    private long tiempoCuarto = 600000; //10 minutos en milisegundos
    private CardView cardView_base, cardView_escolta, cardView_alero, cardView_alapivot, cardView_pivot; //Los diferentes CardViews de todos los jugadores en pista
    private ImageView imageView_triple_anotado, imageView_triple_fallado, imageView_dos_anotado, imageView_dos_fallado, imageView_libre_anotado, imageView_libre_fallado,
            imageView_rebote_defensivo, imageView_rebote_ofensivo, imageView_asistencia, imageView_robo, imageView_tapon, imageView_perdida, imageView_falta_recibida,
            imageView_falta_cometida; //Todos los ImageView de las estadísticas
    private int currentPoints;
    private int originalX, originalY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Todos los TextView del marcador
        textView_puntosLocal = findViewById(R.id.textViewPuntosLocal);
        textView_puntosVisitante = findViewById(R.id.textViewPuntosVisitante);
        textView_periodo = findViewById(R.id.textViewNumeroPeriodo);
        textView_faltasLocal = findViewById(R.id.textViewNumeroFaltasLocal);
        textView_faltasVisitante = findViewById(R.id.textViewNumeroFaltasVisitante);
        textView_tiempo = findViewById(R.id.textViewTiempo);

        //Todos los TextView del CardView
        textView_puntosJugadorCardView = findViewById(R.id.textViewPuntosJugador);
        textView_rebotesJugadorCardView = findViewById(R.id.textViewRebotesJugador);
        textView_asistenciasJugadorCardView = findViewById(R.id.textViewAsistenciasJugador);

        //Botones de control de tiempo del marcador
        imageButton_Play = findViewById(R.id.imageButtonPlay);
        imageButton_Stop = findViewById(R.id.imageButtonStop);

        //Todos los CardView de los jugadores
        cardView_base = findViewById(R.id.cardViewBase);
        cardView_escolta = findViewById(R.id.cardViewEscolta);
        cardView_alero = findViewById(R.id.cardViewAlero);
        cardView_alapivot = findViewById(R.id.cardViewAlaPivot);
        cardView_pivot = findViewById(R.id.cardViewPivot);

        //ImageViews de las estadísticas
        imageView_triple_anotado = findViewById(R.id.imageViewTripleMetido);
        imageView_triple_fallado = findViewById(R.id.imageViewTresFallado);
        imageView_dos_anotado = findViewById(R.id.imageViewDosMetido);
        imageView_dos_fallado = findViewById(R.id.imageViewDosFallado);
        imageView_libre_anotado = findViewById(R.id.imageViewUnoMetido);
        imageView_libre_fallado = findViewById(R.id.imageViewUnoFallado);
        imageView_rebote_defensivo = findViewById(R.id.imageViewReboteDefensivo);
        imageView_rebote_ofensivo = findViewById(R.id.imageViewReboteOfensivo);
        imageView_asistencia = findViewById(R.id.imageViewAsistencia);
        imageView_robo = findViewById(R.id.imageViewRobo);
        imageView_tapon = findViewById(R.id.imageViewTapon);
        imageView_perdida = findViewById(R.id.imageViewPerdida);
        imageView_falta_recibida = findViewById(R.id.imageViewFaltaRecibida);
        imageView_falta_cometida = findViewById(R.id.imageViewFaltaCometida);

        //Asignamos el movimiento a todos los ImageView
        imageView_triple_anotado.setOnTouchListener(this);
        imageView_triple_fallado.setOnTouchListener(this);
        imageView_dos_anotado.setOnTouchListener(this);
        imageView_dos_fallado.setOnTouchListener(this);
        imageView_libre_anotado.setOnTouchListener(this);
        imageView_libre_fallado.setOnTouchListener(this);
        imageView_rebote_defensivo.setOnTouchListener(this);
        imageView_rebote_ofensivo.setOnTouchListener(this);
        imageView_asistencia.setOnTouchListener(this);
        imageView_robo.setOnTouchListener(this);
        imageView_tapon.setOnTouchListener(this);
        imageView_perdida.setOnTouchListener(this);
        imageView_falta_recibida.setOnTouchListener(this);
        imageView_falta_cometida.setOnTouchListener(this);

        //Método que controla el flujo del tiempo del marcador
        actualizarTextoTimer();

        //Si se le da click al botón de Play, dependiendo de si el tiempo esta corriendo o parado, se pausa o reanuda el timer
        imageButton_Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tiempoCorriendo) {
                    pauseTimer(); //Si la variable que define si el tiempo esta parado o corriedo cambia a true, se para el timer
                } else
                    startTimer(); //Como la variable que define si el tiempo esta parado o corriendo está de forma predeterminada a false, se inicia el timer
            }
        });

        //Se termina el timer y se reinicia a 10 minutos
        imageButton_Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer(); //Se reinicia el timer
            }
        });

        //Método que muesta el Dialog con las estadísticas del Base en pista
        cardView_base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo();
            }
        });

        cardView_escolta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo();
            }
        });

        cardView_alero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo();
            }
        });

        cardView_alapivot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo();
            }
        });

        cardView_pivot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo();
            }
        });
    }

    //Método que controla el comienzo del timer
    private void startTimer() {
        timer = new CountDownTimer(tiempoCuarto, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempoCuarto = millisUntilFinished;
                actualizarTextoTimer();
            }

            //Método que controla el final del timer
            @Override
            public void onFinish() {
                tiempoCorriendo = false;
                imageButton_Play.setImageResource(R.drawable.play); //Asignamos al botón de Play la imagen Play
                textView_tiempo.setText("00:00"); //Ponemos el TextView del tiempo a 0
                Toast.makeText(getApplicationContext(), "Cuarto finalizado!", Toast.LENGTH_SHORT).show(); //Mostramos un mensaje emergente que indica que el timer a finalizado
            }
        }.start(); //Iniciamos el timer

        //Actualizamos la variable a true, que indica que el tiempo esta corriendo
        tiempoCorriendo = true;

        //Cambiamos la imagen del botón Play a Pause
        imageButton_Play.setImageResource(R.drawable.pause);
    }

    //Método para terminar el timer
    @SuppressLint("SetTextI18n")
    private void stopTimer() {
        timer.cancel(); //Cancelamos el timer, para que no cuente el tiempo parado
        tiempoCorriendo = false; //Actualizamos la variable a false, que indica que el tiempo esta parado
        imageButton_Play.setImageResource(R.drawable.play);
        tiempoCuarto = 600000; // Reiniciar el tiempo a 10 minutos
        actualizarTextoTimer(); //Llamamos al método que actualiza el TextView del marcador para que lo actualize

        //Almacenamos el dato del TextView en un String
        String periodoSTR = textView_periodo.getText().toString();

        //Comprobamos si el String del TextView contiene la cadena "PR"
        if (!periodoSTR.contains("PR ")) {
            int periodo = Integer.parseInt(periodoSTR); //Como no lo contiene, transformamos el String en un valor int
            periodo++; //Incrementamos en uno el periodo

            String periodoStr = String.valueOf(periodo); //Transformamos el int en String para poder asignarselo al TextView
            textView_periodo.setText(periodoStr); //Asignamos el String al TextView con el periodo actualizado

            if (periodo <= 4) { //Comprobamos si el periodo es menor o igual a 4, los cuartos que tiene un partido de baloncesto
                periodo = Integer.parseInt(textView_periodo.getText().toString()); //Transformamos el String en un valor int
                periodoStr = String.valueOf(periodo); //Transformamos el int en un String para poder asignarlo al TextView
                textView_periodo.setText(periodoStr); //Asignamos el String al TextView con el periodo actualizado
            } else if (!hayProrroga()) { //Comprobamos si al finalizar los 4 periodos hay prórroga
                imageButton_Play.setEnabled(false); //Desactivamos el botón de Play
                imageButton_Stop.setEnabled(false); //Desactivamos el botón de Stop
                Toast.makeText(this, "El partido ha terminado.", Toast.LENGTH_SHORT).show(); //Mostramos un mensaje emergente de que el partido a terminado
            } else if (hayProrroga()) { //Comprobamos si hay prórroga
                periodoStr = "PR " + (periodo - (periodo - 1)); //Al haber prórroga, cambiamos el formato del TextView para indiar que el periodo pertenece a la prórroga
                textView_periodo.setText(periodoStr); //Asignamos el nuevo String con el formato de la prórroga al TextView
            }
            //Comprobamos si el String del TextView contiene la cadena "PR"
        } else if (periodoSTR.contains("PR")) {
            //Comprobamos si hay prórroga después de finalizar el periodo anterior
            if (hayProrroga()) {
                //Dividimos el String que obtenemos del TextView en dos partes, tomando como divisor un espacio, ya que el formato de un periodo de la prórroga es: "PR 0"
                String[] periodoPartes = textView_periodo.getText().toString().split(" ");

                //Asignamos el número del periodo de la prórroga a un int, tomando como valor la segunda parte del String
                int periodoNumero = Integer.parseInt(periodoPartes[1]);

                String periodoStr = "PR " + (periodoNumero + 1); //Creamos un String aplicando el formato de la prórroga, sumando un periodo más
                textView_periodo.setText(periodoStr); //Asignamos el String al TextView
            } else {
                //Mostramos un mensaje emergente de que el partido ha terminado
                Toast.makeText(this, "El partido ha terminado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Método para pausar el timer
    private void pauseTimer() {
        timer.cancel(); //Cancelamos el timer, para que no cuente el tiempo que está parado
        tiempoCorriendo = false; //Actualizamos el estado de la variable y le asignamos false, que indica que el tiempo esta parado
        imageButton_Play.setImageResource(R.drawable.play); //Cambiamos la imagen del botón Play a Play
    }

    //Método que actualiza el texto del TextView del marcador
    private void actualizarTextoTimer() {
        int minutes = (int) (tiempoCuarto / 1000) / 60; //Creamos la variable de los minutos
        int seconds = (int) (tiempoCuarto / 1000) % 60; //Creamos la varibale de los segundos
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds); //Creamos la variable del tiempo, personalizandola para que tenga el formato de minutos:segundos
        textView_tiempo.setText(timeLeftFormatted); //Asignamos el tiempo al TextView del marcador
    }

    //Método que muesta el Dialog con las estadísticas del jugador en cuestión
    private void mostrarDialogo() {
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

        //Método que muestra el Dialog por pantalla
        dialog.show();
    }

    private boolean hayProrroga() {
        int puntosLocal = Integer.parseInt(textView_puntosLocal.getText().toString());
        int puntosVisitante = Integer.parseInt(textView_puntosVisitante.getText().toString());

        if (puntosLocal == puntosVisitante) {
            return true;
        }

        return false;
    }

    private class MyOnTouchListener implements View.OnTouchListener {

        private int initialX, initialY;
        private float initialTouchX, initialTouchY;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                initialX = view.getLeft();
                initialY = view.getTop();
                initialTouchX = motionEvent.getRawX();
                initialTouchY = motionEvent.getRawY();
                return true;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                int dx = (int) (motionEvent.getRawX() - initialTouchX);
                int dy = (int) (motionEvent.getRawY() - initialTouchY);
                view.layout(initialX + dx, initialY + dy,
                        initialX + dx + view.getWidth(),
                        initialY + dy + view.getHeight());
                return true;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                boolean droppedOnCardView = false;
                for (int i = 0; i < 5; i++) {
                    CardView cardView = cardViews[i];
                    if (isViewOverlapping(view, cardView)) {
                        droppedOnCardView = true;
                        String player = (String) cardView.getTag();
                        // Update player stats for the dropped ImageView
                        updatePlayerStats(player, view);
                        break;
                    }
                }
                if (!droppedOnCardView) {
                    view.layout(initialX, initialY,
                            initialX + view.getWidth(),
                            initialY + view.getHeight());
                }
                return true;
            }
            return false;
        }

        private void updatePlayerStats(CardView cardView, ImageView imageView) {
            // Get the tag of the card view to determine which player it represents
            String tag = (String) cardView.getTag();
            // Get the tag of the image view to determine which stat it represents
            String statTag = (String) imageView.getTag();

            // Get the current stats of the player
            int[] playerStats = playerStatsMap.get(tag);

            // Update the appropriate stat based on the image view that was dropped
            switch (statTag) {
                case "threePointerMade":
                    playerStats[0]++;
                    break;
                case "threePointerMissed":
                    playerStats[1]++;
                    break;
                case "twoPointerMade":
                    playerStats[2]++;
                    break;
                case "twoPointerMissed":
                    playerStats[3]++;
                    break;
                case "freeThrowMade":
                    playerStats[4]++;
                    break;
                case "freeThrowMissed":
                    playerStats[5]++;
                    break;
                case "defensiveRebound":
                    playerStats[6]++;
                    break;
                case "offensiveRebound":
                    playerStats[7]++;
                    break;
                case "assist":
                    playerStats[8]++;
                    break;
                case "steal":
                    playerStats[9]++;
                    break;
                case "block":
                    playerStats[10]++;
                    break;
                case "turnover":
                    playerStats[11]++;
                    break;
                case "foulReceived":
                    playerStats[12]++;
                    break;
                case "foulMade":
                    playerStats[13]++;
                    break;
                default:
                    // Do nothing if the image view doesn't represent a valid stat
                    return;
            }

            // Update the text views in the card view with the new stats
            TextView threePointerMadeTextView = cardView.findViewById(R.id.three_pointer_made_text_view);
            threePointerMadeTextView.setText(String.valueOf(playerStats[0]));

            TextView threePointerMissedTextView = cardView.findViewById(R.id.three_pointer_missed_text_view);
            threePointerMissedTextView.setText(String.valueOf(playerStats[1]));

            TextView twoPointerMadeTextView = cardView.findViewById(R.id.two_pointer_made_text_view);
            twoPointerMadeTextView.setText(String.valueOf(playerStats[2]));

            TextView twoPointerMissedTextView = cardView.findViewById(R.id.two_pointer_missed_text_view);
            twoPointerMissedTextView.setText(String.valueOf(playerStats[3]));

            TextView freeThrowMadeTextView = cardView.findViewById(R.id.free_throw_made_text_view);
            freeThrowMadeTextView.setText(String.valueOf(playerStats[4]));

            TextView freeThrowMissedTextView = cardView.findViewById(R.id.free_throw_missed_text_view);
            freeThrowMissedTextView.setText(String.valueOf(playerStats[5]));

            TextView defensiveReboundTextView = cardView.findViewById(R.id.defensive_rebound_text_view);
            defensiveReboundTextView.setText(String.valueOf(playerStats[6]));

            TextView offensiveReboundTextView = cardView.findViewById(R.id.offensive_rebound_text_view);
            offensiveReboundTextView.setText(String.valueOf(playerStats[7]));

            TextView assistTextView = cardView.findViewById(R.id.assist_text_view);
            assistTextView.setText(String.valueOf(playerStats[8]));

            TextView stealTextView = cardView.findViewById(R.id.steal_text_view);
            stealTextView.setText(String.valueOf(playerStats[9]));

            TextView blockTextView = cardView.findViewById(R.id.block_text_view);
            blockTextView.setText(String.valueOf(playerStats[10]));

            TextView turnoverTextView = cardView.findViewById(R.id.turnover_text_view);
            turnoverTextView.setText(String.valueOf(playerStats[11]));

            TextView foulReceivedTextView = cardView.findViewById(R.id.foul_received_text_view);
            foulReceivedTextView.setText(String.valueOf(playerStats[12]));

            TextView foulMadeTextView = cardView.findViewById(R.id.foul_made_text_view);
            foulMadeTextView.setText(String.valueOf(playerStats[13]));

            // Update the player stats map with the new stats
            playerStatsMap.put(tag, playerStats);
        }

        private boolean isViewOverlapping(View view1, View view2) {
            int[] view1Location = new int[2];
            int[] view2Location = new int[2];
            view1.getLocationOnScreen(view1Location);
            view2.getLocationOnScreen(view2Location);
            Rect rect1 = new Rect(view1Location[0], view1Location[1],
                    view1Location[0] + view1.getWidth(), view1Location[1] + view1.getHeight());
            Rect rect2 = new Rect(view2Location[0], view2Location[1],
                    view2Location[0] + view2.getWidth(), view2Location[1] + view2.getHeight());
            return rect1.intersect(rect2);
        }
    }
}

/*
public class MainActivity extends AppCompatActivity {
    private ImageView[] imageViews;
    private CardView[] cardViews;
    private HashMap<String, int[]> playerStatsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViews = new ImageView[14];
        imageViews[0] = findViewById(R.id.imageViewTripleMetido);
        imageViews[1] = findViewById(R.id.imageViewTresFallado);
        imageViews[2] = findViewById(R.id.imageViewDosMetido);
        imageViews[3] = findViewById(R.id.imageViewDosFallado);
        imageViews[4] = findViewById(R.id.imageViewUnoMetido);
        imageViews[5] = findViewById(R.id.imageViewUnoFallado);
        imageViews[6] = findViewById(R.id.imageViewReboteDefensivo);
        imageViews[7] = findViewById(R.id.imageViewReboteOfensivo);
        imageViews[8] = findViewById(R.id.imageViewAsistencia);
        imageViews[9] = findViewById(R.id.imageViewRobo);
        imageViews[10] = findViewById(R.id.imageViewTapon);
        imageViews[11] = findViewById(R.id.imageViewPerdida);
        imageViews[12] = findViewById(R.id.imageViewFaltaRecibida);
        imageViews[13] = findViewById(R.id.imageViewFaltaCometida);

        cardViews = new CardView[12];
        cardViews[0] = findViewById(R.id.cardViewBase);
        cardViews[1] = findViewById(R.id.cardViewEscolta);
        cardViews[2] = findViewById(R.id.cardViewAlero);
        cardViews[3] = findViewById(R.id.cardViewAlaPivot);
        cardViews[4] = findViewById(R.id.cardViewPivot);
        cardViews[5] = findViewById(R.id.cardViewPivot);
        cardViews[6] = findViewById(R.id.cardViewPivot);
        cardViews[7] = findViewById(R.id.cardViewPivot);
        cardViews[8] = findViewById(R.id.cardViewPivot);
        cardViews[9] = findViewById(R.id.cardViewPivot);
        cardViews[10] = findViewById(R.id.cardViewPivot);
        cardViews[11] = findViewById(R.id.cardViewPivot);

        darTags();
        inicializarStats();

        //Método que muesta el Dialog con las estadísticas del Base en pista
        cardViews[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("point_guard");
            }
        });

        //Método que muesta el Dialog con las estadísticas del Base en pista
        cardViews[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("shooting_guard");
            }
        });

        //Método que muesta el Dialog con las estadísticas del Base en pista
        cardViews[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("small_forward");
            }
        });

        //Método que muesta el Dialog con las estadísticas del Base en pista
        cardViews[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("power_forward");
            }
        });

        //Método que muesta el Dialog con las estadísticas del Base en pista
        cardViews[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("center");
            }
        });

        //Método que muesta el Dialog con las estadísticas del Base en pista
        cardViews[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("suplente1");
            }
        });

        //Método que muesta el Dialog con las estadísticas del Base en pista
        cardViews[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("suplente2");
            }
        });

        //Método que muesta el Dialog con las estadísticas del Base en pista
        cardViews[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("suplente3");
            }
        });

        //Método que muesta el Dialog con las estadísticas del Base en pista
        cardViews[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("suplente4");
            }
        });

        //Método que muesta el Dialog con las estadísticas del Base en pista
        cardViews[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("suplente5");
            }
        });

        //Método que muesta el Dialog con las estadísticas del Base en pista
        cardViews[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("suplente6");
            }
        });

        //Método que muesta el Dialog con las estadísticas del Base en pista
        cardViews[11].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("suplente7");
            }
        });

        for (ImageView imageView : imageViews) {
            imageView.setOnTouchListener(new MyOnTouchListener());
        }
    }

    private void darTags() {
        cardViews[0].setTag("point_guard");
        cardViews[1].setTag("shooting_guard");
        cardViews[2].setTag("small_forward");
        cardViews[3].setTag("power_forward");
        cardViews[4].setTag("center");
        cardViews[5].setTag("suplente1");
        cardViews[6].setTag("suplente2");
        cardViews[7].setTag("suplente3");
        cardViews[8].setTag("suplente4");
        cardViews[9].setTag("suplente5");
        cardViews[10].setTag("suplente6");
        cardViews[11].setTag("suplente7");

        imageViews[0].setTag("threePointerMade");
        imageViews[1].setTag("threePointerMissed");
        imageViews[2].setTag("twoPointerMade");
        imageViews[3].setTag("twoPointerMissed");
        imageViews[4].setTag("freeThrowMade");
        imageViews[5].setTag("freeThrowMissed");
        imageViews[6].setTag("defensiveRebound");
        imageViews[7].setTag("offensiveRebound");
        imageViews[8].setTag("assist");
        imageViews[9].setTag("steal");
        imageViews[10].setTag("block");
        imageViews[11].setTag("turnover");
        imageViews[12].setTag("foulReceived");
        imageViews[13].setTag("foulMade");
    }

    private void inicializarStats() {
        // Define initial stats for player 1
        int[] player1Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        // Add player 1 stats to the map
        playerStatsMap.put("point_guard", player1Stats);

        // Define initial stats for player 2
        int[] player2Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        // Add player 2 stats to the map
        playerStatsMap.put("shooting_guard", player2Stats);

        // Define initial stats for player 3
        int[] player3Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        // Add player 3 stats to the map
        playerStatsMap.put("small_forward", player3Stats);

        // Define initial stats for player 4
        int[] player4Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        // Add player 4 stats to the map
        playerStatsMap.put("power_forward", player4Stats);

        // Define initial stats for player 5
        int[] player5Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        // Add player 5 stats to the map
        playerStatsMap.put("center", player5Stats);

        // Define initial stats for player 6
        int[] player6Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        // Add player 6 stats to the map
        playerStatsMap.put("suplente1", player6Stats);

        // Define initial stats for player 7
        int[] player7Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        // Add player 7 stats to the map
        playerStatsMap.put("suplente2", player7Stats);

        // Define initial stats for player 8
        int[] player8Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        // Add player 8 stats to the map
        playerStatsMap.put("suplente3", player8Stats);

        // Define initial stats for player 9
        int[] player9Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        // Add player 9 stats to the map
        playerStatsMap.put("suplente4", player9Stats);

        // Define initial stats for player 10
        int[] player10Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        // Add player 10 stats to the map
        playerStatsMap.put("suplente5", player10Stats);

        // Define initial stats for player 11
        int[] player11Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        // Add player 11 stats to the map
        playerStatsMap.put("suplente6", player11Stats);

        // Define initial stats for player 12
        int[] player12Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        // Add player 12 stats to the map
        playerStatsMap.put("suplente7", player12Stats);
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

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
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

                int[] playerStats = playerStatsMap.get(tagJugador);
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

    private class MyOnTouchListener implements View.OnTouchListener {
        private int initialX, initialY;
        private float initialTouchX, initialTouchY;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                initialX = view.getLeft();
                initialY = view.getTop();
                initialTouchX = motionEvent.getRawX();
                initialTouchY = motionEvent.getRawY();
                return true;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                int dx = (int) (motionEvent.getRawX() - initialTouchX);
                int dy = (int) (motionEvent.getRawY() - initialTouchY);
                view.layout(initialX + dx, initialY + dy,
                        initialX + dx + view.getWidth(),
                        initialY + dy + view.getHeight());
                return true;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                boolean droppedOnCardView = false;
                for (int i = 0; i < 5; i++) {
                    CardView cardView = cardViews[i];
                    if (isViewOverlapping(view, cardView)) {
                        droppedOnCardView = true;
                        String image = (String) view.getTag();
                        // Update player stats for the dropped ImageView
                        updatePlayerStats(cardView, image);
                        view.layout(initialX, initialY,
                                initialX + view.getWidth(),
                                initialY + view.getHeight());
                        break;
                    }
                }
                if (!droppedOnCardView) {
                    view.layout(initialX, initialY,
                            initialX + view.getWidth(),
                            initialY + view.getHeight());
                }
                return true;
            }
            return false;
        }

        private boolean isViewOverlapping(View view1, View view2) {
            int[] view1Location = new int[2];
            int[] view2Location = new int[2];
            view1.getLocationOnScreen(view1Location);
            view2.getLocationOnScreen(view2Location);
            Rect rect1 = new Rect(view1Location[0], view1Location[1],
                    view1Location[0] + view1.getWidth(), view1Location[1] + view1.getHeight());
            Rect rect2 = new Rect(view2Location[0], view2Location[1],
                    view2Location[0] + view2.getWidth(), view2Location[1] + view2.getHeight());
            return rect1.intersect(rect2);
        }

        private void updatePlayerStats(CardView cardView, String statTag) {
            // Get the tag of the card view to determine which player it represents
            String tag = (String) cardView.getTag();

            // Get the current stats of the player
            int[] playerStats = playerStatsMap.get(tag);

            // Update the appropriate stat based on the image view that was dropped
            switch (statTag) {
                case "threePointerMade":
                    playerStats[1]++;
                    playerStats[2]++;
                    playerStats[0] += 3;
                    playerStats[16] += 3;
                    break;
                case "threePointerMissed":
                    playerStats[2]++;
                    playerStats[16]--;
                    break;
                case "twoPointerMade":
                    playerStats[3]++;
                    playerStats[4]++;
                    playerStats[0] += 2;
                    playerStats[16] += 2;
                    break;
                case "twoPointerMissed":
                    playerStats[4]++;
                    playerStats[16]--;
                    break;
                case "freeThrowMade":
                    playerStats[5]++;
                    playerStats[6]++;
                    playerStats[0] += 1;
                    playerStats[16] += 1;
                    break;
                case "freeThrowMissed":
                    playerStats[6]++;
                    playerStats[16]--;
                    break;
                case "defensiveRebound":
                    playerStats[7]++;
                    playerStats[9]++;
                    playerStats[16]++;
                    break;
                case "offensiveRebound":
                    playerStats[8]++;
                    playerStats[9]++;
                    playerStats[16]++;
                    break;
                case "assist":
                    playerStats[10]++;
                    playerStats[16]++;
                    break;
                case "steal":
                    playerStats[11]++;
                    playerStats[16]++;
                    break;
                case "block":
                    playerStats[12]++;
                    playerStats[16]++;
                    break;
                case "turnover":
                    playerStats[13]++;
                    playerStats[16]--;
                    break;
                case "foulReceived":
                    playerStats[14]++;
                    playerStats[16]++;
                    break;
                case "foulMade":
                    playerStats[15]++;
                    playerStats[16]--;
                    break;
                default:
                    // Do nothing if the image view doesn't represent a valid stat
                    return;
            }

            // Update the text views in the card view with the new stats
            TextView totalPointsTextView = cardView.findViewById(R.id.textViewPuntosJugador);
            totalPointsTextView.setText(String.valueOf(playerStats[0]));

            TextView totalReboundsTextView = cardView.findViewById(R.id.textViewRebotesJugador);
            totalReboundsTextView.setText(String.valueOf(playerStats[9]));

            TextView assistTextView = cardView.findViewById(R.id.textViewAsistenciasJugador);
            assistTextView.setText(String.valueOf(playerStats[10]));

            TextView foulMadeTextView = cardView.findViewById(R.id.textViewFaltasJugador);
            foulMadeTextView.setText(String.valueOf(playerStats[15]));

            // Update the player stats map with the new stats
            playerStatsMap.put(tag, playerStats);
        }
    }
}*/
