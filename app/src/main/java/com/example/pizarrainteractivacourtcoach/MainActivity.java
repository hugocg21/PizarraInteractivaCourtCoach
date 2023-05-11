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

public class MainActivity extends AppCompatActivity{
    private ImageButton imageButton_Play, imageButton_Stop; //Botontes de Play y Stop del marcador
    private TextView textView_tiempo, textView_puntosLocal, textView_puntosVisitante, textView_periodo, textView_faltasLocal, textView_faltasVisitante; //Atributos del marcador
    private TextView textView_puntosJugadorCardView, textView_rebotesJugadorCardView, textView_asistenciasJugadorCardView, textView_faltasJugadorCardView; //Estadisticas en el CardView
    private CountDownTimer timer; //Timer para el timepo de cuarto
    private boolean tiempoCorriendo = false; //Define si el tiempo esta en corriendo o parado
    private long tiempoCuarto = 600000; //10 minutos en milisegundos
    private ImageView[] imageViews;
    private CardView[] cardViews;
    private HashMap<String, int[]> playerStatsMap = new HashMap<>();

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
        textView_faltasJugadorCardView = findViewById(R.id.textViewFaltasJugador);

        //Botones de control de tiempo del marcador
        imageButton_Play = findViewById(R.id.imageButtonPlay);
        imageButton_Stop = findViewById(R.id.imageButtonStop);

        //Array de ImageViews para asignar cada ImageView a cada estadística posible
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

        //Array de CardViews para asignar cada CardView a cada jugador
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

        //Método para asignar nombres a modo de Tags a cada ImageView y CardView
        darTags();

        //Método para inicializar todas las estadísticas de cada jugador a 0 al inicio de cada partido
        inicializarStats();

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
        cardViews[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("point_guard");
            }
        });

        //Método que muesta el Dialog con las estadísticas del Escolta en pista
        cardViews[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("shooting_guard");
            }
        });

        //Método que muesta el Dialog con las estadísticas del Alero en pista
        cardViews[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("small_forward");
            }
        });

        //Método que muesta el Dialog con las estadísticas del Alapivot en pista
        cardViews[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("power_forward");
            }
        });

        //Método que muesta el Dialog con las estadísticas del Pivot en pista
        cardViews[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("center");
            }
        });

        //Método que muesta el Dialog con las estadísticas del suplente 1 en pista
        cardViews[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("suplente1");
            }
        });

        //Método que muesta el Dialog con las estadísticas del suplente 2 en pista
        cardViews[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("suplente2");
            }
        });

        //Método que muesta el Dialog con las estadísticas del suplente 3 en pista
        cardViews[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("suplente3");
            }
        });

        //Método que muesta el Dialog con las estadísticas del suplente 4 en pista
        cardViews[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("suplente4");
            }
        });

        //Método que muesta el Dialog con las estadísticas del suplente 5 en pista
        cardViews[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("suplente5");
            }
        });

        //Método que muesta el Dialog con las estadísticas del suplente 6 en pista
        cardViews[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("suplente6");
            }
        });

        //Método que muesta el Dialog con las estadísticas del suplente 7 en pista
        cardViews[11].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogo("suplente7");
            }
        });

        //Asignamos el movimiento de los ImageViews con un bucle for
        for (ImageView imageView : imageViews) {
            imageView.setOnTouchListener(new MyOnTouchListener());
        }
    }

    //Método para dar Tags a cada CardView
    private void darTags() {
        cardViews[0].setTag("base");
        cardViews[1].setTag("escolta");
        cardViews[2].setTag("alero");
        cardViews[3].setTag("ala_pivot");
        cardViews[4].setTag("pivot");
        cardViews[5].setTag("suplente1");
        cardViews[6].setTag("suplente2");
        cardViews[7].setTag("suplente3");
        cardViews[8].setTag("suplente4");
        cardViews[9].setTag("suplente5");
        cardViews[10].setTag("suplente6");
        cardViews[11].setTag("suplente7");

        imageViews[0].setTag("tripleAnotado");
        imageViews[1].setTag("tripleFallado");
        imageViews[2].setTag("dosAnotado");
        imageViews[3].setTag("dosFallado");
        imageViews[4].setTag("libreAnotado");
        imageViews[5].setTag("libreFallado");
        imageViews[6].setTag("reboteDefensivo");
        imageViews[7].setTag("reboteOfensivo");
        imageViews[8].setTag("asistencia");
        imageViews[9].setTag("robo");
        imageViews[10].setTag("tapon");
        imageViews[11].setTag("perdida");
        imageViews[12].setTag("faltaRecibida");
        imageViews[13].setTag("faltaCometida");
    }

    //Método para inicializar todas las stats de todos los jugadores a 0
    private void inicializarStats() {
        //Define a 0 las stats del base
        int[] player1Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al base al Map de jugadores
        playerStatsMap.put("base", player1Stats);

        //Define a 0 las stats del escolta
        int[] player2Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al escolta al Map de jugadores
        playerStatsMap.put("escolta", player2Stats);

        //Define a 0 las stats del alero
        int[] player3Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al alero al Map de jugadores
        playerStatsMap.put("alero", player3Stats);

        //Define a 0 las stats del ala_pivot
        int[] player4Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al ala_pivot al Map de jugadores
        playerStatsMap.put("ala_pivot", player4Stats);

        //Define a 0 las stats del pivot
        int[] player5Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al pivot al Map de jugadores
        playerStatsMap.put("pivot", player5Stats);

        //Define a 0 las stats del suplente 1
        int[] player6Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al suplente 1 al Map de jugadores
        playerStatsMap.put("suplente1", player6Stats);

        //Define a 0 las stats del suplente 2
        int[] player7Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al suplente 2 al Map de jugadores
        playerStatsMap.put("suplente2", player7Stats);

        //Define a 0 las stats del suplente 3
        int[] player8Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al suplente 3 al Map de jugadores
        playerStatsMap.put("suplente3", player8Stats);

        //Define a 0 las stats del suplente 4
        int[] player9Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al suplente 4 al Map de jugadores
        playerStatsMap.put("suplente4", player9Stats);

        //Define a 0 las stats del suplente 5
        int[] player10Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al suplente 5 al Map de jugadores
        playerStatsMap.put("suplente5", player10Stats);

        //Define a 0 las stats del suplente 6
        int[] player11Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al suplente 6 al Map de jugadores
        playerStatsMap.put("suplente6", player11Stats);

        //Define a 0 las stats del suplente 7
        int[] player12Stats = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al suplente 7 al Map de jugadores
        playerStatsMap.put("suplente7", player12Stats);
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
                int[] playerStats = playerStatsMap.get(tagJugador);

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

    //Comprobamos comparando los dos resultados si hay prórroga o no
    private boolean hayProrroga() {
        //Recogemos en un int los puntos anotados por el equipo local
        int puntosLocal = Integer.parseInt(textView_puntosLocal.getText().toString());

        //Recogemos en un int los puntos anotados por el equipo visitante
        int puntosVisitante = Integer.parseInt(textView_puntosVisitante.getText().toString());

        //Si las dos anotaciones son iguales, devolvemos true, por lo tanto hay prórroga
        if (puntosLocal == puntosVisitante) {
            return true;
        }

        //Si no son iguales, devolvemos false y no hay prórroga
        return false;
    }

    //Clase que controla los eventos que se realizan al tocar los objetos de la pantalla
    private class MyOnTouchListener implements View.OnTouchListener {
        private int initialX, initialY; //Guardamos en estas variables la posición inicial de cada ImageView
        private float initialTouchX, initialTouchY; //Guardamos en estas variables la posición de los dedos al mover el ImageView por la pantalla

        //Método onTouch que controla los eventos al tocar los ImageViews
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            //Si la acción es ACTION_DOWN, es decir, cuando se coloca el dedo en la pantalla y se selecciona un ImageView para arrastrar
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                initialX = view.getLeft(); //Asignamos a la izquierda la variable de la posición inicial de la X
                initialY = view.getTop(); //Asignamos en la parte de arriba a la variable de la posición incial de la Y
                initialTouchX = motionEvent.getRawX(); //Asignamos la posición X en la que se encuentra el dedo en la pantalla
                initialTouchY = motionEvent.getRawY(); //Asignamos la posición Y en la que se encuentra el dedo en la pantalla
                return true;

                //Si la acción es ACTION_MOVE, es decir, cuando se mueve el dedo, y por ende, el ImageView por la pantalla
            } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                int dx = (int) (motionEvent.getRawX() - initialTouchX); //Creamos una variable de tipo int y calculamos el cambio de la coordenada X basandonos en la posición actual y la inicial
                int dy = (int) (motionEvent.getRawY() - initialTouchY); //Creamos una variable de tipo int y calculamos el cambio de la coordenada Y basandonos en la posición actual y la inicial

                //Actualizamos la vista del ImageView en la pantalla según se va moviendo con los calculos anteriores
                view.layout(initialX + dx, initialY + dy,
                        initialX + dx + view.getWidth(),
                        initialY + dy + view.getHeight());
                return true;

                //Si la acción es ACTION_UP, es decir, cuando se levanta el dedo de la pantalla y se suelta el ImageView
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                boolean droppedOnCardView = false; //Creamos una variable booleana que determina si el ImageView se dropeo sobre el CardView, y lo inicializamos a false
                //Recorremos los cinco CardViews que se encuentran en la pista para verificar lo anterior
                for (int i = 0; i < 5; i++) {
                    CardView cardView = cardViews[i]; //Comprobamos el CardView que toca según la i del for
                    //Llamamos al método isViewOverlapping para comprobar si está superpuesto un objeto de otro
                    if (isViewOverlapping(view, cardView)) {
                        droppedOnCardView = true; //Al entrar en el bucle, cambiamos el booleano a true ya qué significa que un objeto está superpuseto a otro
                        String image = (String) view.getTag(); //Recogemos el Tag del ImageView para saber que stat hay que sumar
                        updatePlayerStats(cardView, image); // Update player stats for the dropped ImageView

                        //Devolvemos el ImageView a su posición inicial, para poder volver a ser utilizado
                        view.layout(initialX, initialY,
                                initialX + view.getWidth(),
                                initialY + view.getHeight());
                        break;
                    }
                }

                //Si el ImageView no ha sido dropeado encima de un CardView
                if (!droppedOnCardView) {
                    //Devolvemos el ImageView a su posición inicial
                    view.layout(initialX, initialY,
                            initialX + view.getWidth(),
                            initialY + view.getHeight());
                }
                return true;
            }
            return false;
        }

        //Método para actualizar las estadísticas de los jugadores
        private void updatePlayerStats(CardView cardView, String statTag) {
            // Get the tag of the card view to determine which player it represents
            String tag = (String) cardView.getTag();

            // Get the current stats of the player
            int[] playerStats = playerStatsMap.get(tag);

            //Actualizamos las estadísticas correspondientes según que ImageView sea dropeado
            switch (statTag) {
                //El ImageView es el de triple anotado
                case "tripleAnotado":
                    playerStats[1]++; //Se suma uno a los triples metidos
                    playerStats[2]++; //Se suma uno a los triples tirados
                    playerStats[0] += 3; //se suman tres a los puntos totales
                    playerStats[16] += 3; //Se suma tres a la valoración
                    break;

                //El ImageView es el de triple fallado
                case "tripleFallado":
                    playerStats[2]++; //Se suma uno a los tiros tirados
                    playerStats[16]--; //Se resta uno a la valoración
                    break;

                //El ImageView es el de tiro de dos anotado
                case "dosAnotado":
                    playerStats[3]++; //Se suma uno a los tiros de dos anotados
                    playerStats[4]++; //Se suma uno a los tiros de dos tirados
                    playerStats[0] += 2; //Se suma dos a los puntos totales
                    playerStats[16] += 2; //Se suma dos a la valoración
                    break;

                //El ImageView es el de tiro de dos fallado
                case "dosFallado":
                    playerStats[4]++; //Se suma uno a los tiros de dos tirados
                    playerStats[16]--; //Se resta uno a la valoración
                    break;

                //El ImageView es el de tiro libre anotado
                case "libreAnotado":
                    playerStats[5]++; //Se suma uno a los tiros libres anotados
                    playerStats[6]++; //Se suma uno a los tiros libres tirados
                    playerStats[0] += 1; //Se suma uno a los puntos totales
                    playerStats[16] += 1; //Se suma uno a la valoración
                    break;

                //El ImageView es el de tiro libre fallado
                case "libreFallado":
                    playerStats[6]++; //Se suma uno a los tiros libres tirados
                    playerStats[16]--; //Se resta uno a la valoración
                    break;

                //El ImageView es el de rebote defensivo
                case "reboteDefensivo":
                    playerStats[7]++; //Se suma uno a rebote defensivo
                    playerStats[9]++; //Se suma uno a los rebotes totales
                    playerStats[16]++; //Se suma uno a la valoración
                    break;

                //El ImageView es el de rebote ofensivo
                case "reboteOfensivo":
                    playerStats[8]++; //Se suma uno a los rebotes ofensivos
                    playerStats[9]++; //Se suma uno a los rebotes totales
                    playerStats[16]++; //Se suma uno a la valoración
                    break;

                //El ImageView es el de asistencia
                case "asistencia":
                    playerStats[10]++; //Se suma uno a las asistencias
                    playerStats[16]++; //Se suma uno a la valoración
                    break;

                //El ImageView es el del robo
                case "robo":
                    playerStats[11]++; //Suma uno a los robos
                    playerStats[16]++; //Suma uno a la valoración
                    break;

                //El ImageView es el del tapón
                case "tapon":
                    playerStats[12]++; //Suma uno a los tapones
                    playerStats[16]++; //Suma uno a la valoración
                    break;

                //El ImageView es el de la pérdida
                case "perdida":
                    playerStats[13]++; //Suma uno a las pérdidas
                    playerStats[16]--; //Resta uno a la valoración
                    break;

                //El ImageView es el de la falta recibida
                case "faltaRecibida":
                    playerStats[14]++; //Suma uno a las faltas recibidas
                    playerStats[16]++; //Suma uno a la valoración
                    break;

                //El ImageView es el de la falta cometida
                case "faltaCometida":
                    playerStats[15]++; //Suma uno a las faltas cometidas
                    playerStats[16]--; //Resta uno a la valoración
                    break;

                //Opción default que no hace nada
                default:
                    return;
            }

            //Actualiza el TextView de los puntos del CardView con los puntos nuevos sumados
            TextView totalPointsTextView = cardView.findViewById(R.id.textViewPuntosJugador);
            totalPointsTextView.setText(String.valueOf(playerStats[0]));

            //Actualiza el TextView de los rebotes del CardView con los rebotes nuevos sumados
            TextView totalReboundsTextView = cardView.findViewById(R.id.textViewRebotesJugador);
            totalReboundsTextView.setText(String.valueOf(playerStats[9]));

            //Actualiza el TextView de las asistencias del CardView con las asistencias nuevas sumadas
            TextView assistTextView = cardView.findViewById(R.id.textViewAsistenciasJugador);
            assistTextView.setText(String.valueOf(playerStats[10]));

            //Actualiza el TextView de las faltas del CardView con las faltas nuevas sumadas
            TextView foulMadeTextView = cardView.findViewById(R.id.textViewFaltasJugador);
            foulMadeTextView.setText(String.valueOf(playerStats[15]));

            //Actualiza las estadísticas del jugador en el Map con las nuevas estadísticas
            playerStatsMap.put(tag, playerStats);
        }

        //Este método determina cuando dos objetos están superpuestos. Devuelve true si hay dos objetos superpuestos y false cuando no lo están
        private boolean isViewOverlapping(View view1, View view2) {
            //Creamos dos Arrays de ints que almacenaran las coordenadas X e Y de cada objeto que queramos comparar
            int[] view1Location = new int[2];
            int[] view2Location = new int[2];

            //Asignamos las coordenadas de la esquina izquierda superior de cada objeto a las variables
            view1.getLocationOnScreen(view1Location);
            view2.getLocationOnScreen(view2Location);

            //Con estos objetos Rect, definimos la dimension de cada objeto de forma rectangular, desde la esquina izquierda superior hasta la derecha inferior
            Rect rect1 = new Rect(view1Location[0], view1Location[1],
                    view1Location[0] + view1.getWidth(), view1Location[1] + view1.getHeight());
            Rect rect2 = new Rect(view2Location[0], view2Location[1],
                    view2Location[0] + view2.getWidth(), view2Location[1] + view2.getHeight());

            //Devolvemos un booleano de si están superpuestos o no
            return rect1.intersect(rect2);
        }
    }
}