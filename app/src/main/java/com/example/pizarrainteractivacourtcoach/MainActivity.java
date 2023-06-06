package com.example.pizarrainteractivacourtcoach;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Button button_banquillo;
    private ImageButton imageButton_Play, imageButton_Stop; //Botontes de Play y Stop del marcador
    private TextView textView_tiempo, textView_puntosLocal, textView_puntosVisitante, textView_periodo, textView_faltasLocal, textView_faltasVisitante; //Atributos del marcador
    private TextView textView_puntosJugadorCardView, textView_rebotesJugadorCardView, textView_asistenciasJugadorCardView, textView_faltasJugadorCardView; //Estadisticas en el CardView
    private CountDownTimer timer; //Timer para el timepo de cuarto
    private boolean tiempoCorriendo = false; //Define si el tiempo esta en corriendo o parado
    private long tiempoCuarto = 600000; //10 minutos en milisegundos
    private ImageView[] imageViews; //Array de ImageViews para almacenar todos los ImageViews de las estadísticas
    private CardView[] cardViewsEnPista, cardViewsBanquillo; //Array de CardViews para almacenar todos los CardViews de los jugadores
    private CardView marcador;
    private HashMap<String, int[]> statsJugadoresMap = new HashMap<>(); //HashMap con las estadísticas de cada jugador
    private int[] marcadorStats;
    private ConstraintLayout enPista;
    private int numJugadoresConvocados = 12;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enPista = findViewById(R.id.constraintLayoutQuinteto);

        button_banquillo = findViewById(R.id.buttonBanquillo);

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

        //Array de CardViews para asignar cada CardView a cada jugador y marcador
        cardViewsEnPista = new CardView[5];
        cardViewsEnPista[0] = findViewById(R.id.cardViewBase);
        cardViewsEnPista[1] = findViewById(R.id.cardViewEscolta);
        cardViewsEnPista[2] = findViewById(R.id.cardViewAlero);
        cardViewsEnPista[3] = findViewById(R.id.cardViewAlaPivot);
        cardViewsEnPista[4] = findViewById(R.id.cardViewPivot);

        int numJugadoresBanquillo = numJugadoresConvocados - 5;

        cardViewsBanquillo = new CardView[numJugadoresBanquillo];

        marcador = findViewById(R.id.cardViewMarcador);

        //Método para asignar nombres a modo de Tags a cada ImageView y CardView
        darTags();

        //Método para inicializar todas las estadísticas de cada jugador a 0 al inicio de cada partido
        inicializarStatsJugadores();

        //Método para inicializar todas las estadísticas del marcador a 0 al inicio de cada partido
        inicializarStatsMarcador();

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

        //Asignamos el movimiento de los ImageViews con un bucle for
        for (ImageView imageView : imageViews) {
            imageView.setOnTouchListener(new MyOnTouchListener());
        }

        //Asignamos el movimiento de los CardViews que están en pista con un bucle for
        for (CardView cardView : cardViewsEnPista) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mostrarDialogo((String) cardView.getTag());
                }
            });
        }

        for (CardView cardView : cardViewsEnPista) {
            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipData clipData = ClipData.newPlainText("", "");
                    View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(view);
                    view.startDragAndDrop(clipData, dragShadowBuilder, view, 0);
                    return true;
                }
            });
        }

        //
        button_banquillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarBanquillo();
            }
        });
    }

    public void mostrarBanquillo() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);

        ImageView cerrarDialog = dialog.findViewById(R.id.imageViewCerrarDesplegable);
        LinearLayout jugadores = dialog.findViewById(R.id.linearLayoutJugadores);

        int numJugadoresBanquillo = numJugadoresConvocados - 5;

        for (int i = 0; i < numJugadoresBanquillo; i++) {
            int numJugador = 1;
            String tagJugador = "suplente" + numJugador;
            numJugador++;
            View cardView = getLayoutInflater().inflate(R.layout.jugador, null);
            cardViewsBanquillo[i] = (CardView) cardView;
            cardViewsBanquillo[i].setTag(tagJugador);
            jugadores.addView(cardView);
        }

        /*//Asignamos el movimiento de los CardViews con un bucle for
        for (CardView cardView : cardViewsBanquillo) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mostrarDialogo((String) cardView.getTag());
                }
            });
        }*/

        for (CardView cardView : cardViewsBanquillo) {
            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipData clipData = ClipData.newPlainText("", "");
                    View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(view);
                    view.startDragAndDrop(clipData, dragShadowBuilder, view, 0);
                    dialog.dismiss();

                    cardView.setOnDragListener(new View.OnDragListener() {
                        @Override
                        public boolean onDrag(View v, DragEvent event) {
                            int action = event.getAction();
                            switch (action) {
                                case DragEvent.ACTION_DROP:
                                    // Obtener la vista arrastrada
                                    CardView draggedCardView = (CardView) event.getLocalState();
                                    // Obtener el índice de la vista arrastrada en los jugadores del banquillo
                                    int indexInBanquillo = Arrays.asList(cardViewsBanquillo).indexOf(draggedCardView);
                                    // Obtener el índice de la vista sobre la cual se soltó
                                    int indexInPista = Arrays.asList(cardViewsEnPista).indexOf(v);
                                    // Comprobar si se soltó encima de otro CardView
                                    if (indexInPista != -1) {
                                        // Obtener el CardView en pista que será reemplazado
                                        CardView cardViewEnPista = cardViewsEnPista[indexInPista];
                                        // Realizar la sustitución en los arrays
                                        cardViewsBanquillo[indexInBanquillo] = cardViewEnPista;
                                        cardViewsEnPista[indexInPista] = draggedCardView;
                                        // Actualizar la vista en la pantalla
                                        enPista.removeView(cardViewEnPista);
                                        enPista.addView(draggedCardView, indexInPista);
                                    }
                                    break;
                                default:
                                    break;
                            }
                            return true;
                        }
                    });

                    return true;
                }
            });
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

    //Método para dar Tags a cada CardView
    private void darTags() {
        cardViewsEnPista[0].setTag("base");
        cardViewsEnPista[1].setTag("escolta");
        cardViewsEnPista[2].setTag("alero");
        cardViewsEnPista[3].setTag("ala_pivot");
        cardViewsEnPista[4].setTag("pivot");

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
    private void inicializarStatsJugadores() {
        //Define a 0 las stats del base
        int[] statsBase = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al base al Map de jugadores
        statsJugadoresMap.put("base", statsBase);

        //Define a 0 las stats del escolta
        int[] statsEscolta = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al escolta al Map de jugadores
        statsJugadoresMap.put("escolta", statsEscolta);

        //Define a 0 las stats del alero
        int[] statsAlero = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al alero al Map de jugadores
        statsJugadoresMap.put("alero", statsAlero);

        //Define a 0 las stats del ala_pivot
        int[] statsAlaPivot = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al ala_pivot al Map de jugadores
        statsJugadoresMap.put("ala_pivot", statsAlaPivot);

        //Define a 0 las stats del pivot
        int[] statsPivot = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al pivot al Map de jugadores
        statsJugadoresMap.put("pivot", statsPivot);

        //Define a 0 las stats del suplente 1
        int[] statsSuplente1 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al suplente 1 al Map de jugadores
        statsJugadoresMap.put("suplente1", statsSuplente1);

        //Define a 0 las stats del suplente 2
        int[] statsSuplente2 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al suplente 2 al Map de jugadores
        statsJugadoresMap.put("suplente2", statsSuplente2);

        //Define a 0 las stats del suplente 3
        int[] statsSuplente3 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al suplente 3 al Map de jugadores
        statsJugadoresMap.put("suplente3", statsSuplente3);

        //Define a 0 las stats del suplente 4
        int[] statsSuplente4 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al suplente 4 al Map de jugadores
        statsJugadoresMap.put("suplente4", statsSuplente4);

        //Define a 0 las stats del suplente 5
        int[] statsSuplente5 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al suplente 5 al Map de jugadores
        statsJugadoresMap.put("suplente5", statsSuplente5);

        //Define a 0 las stats del suplente 6
        int[] statsSuplente6 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al suplente 6 al Map de jugadores
        statsJugadoresMap.put("suplente6", statsSuplente6);

        //Define a 0 las stats del suplente 7
        int[] statsSuplente7 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Añade al suplente 7 al Map de jugadores
        statsJugadoresMap.put("suplente7", statsSuplente7);
    }

    //Método que inicializa a cero las stats del marcador
    private void inicializarStatsMarcador() {
        marcadorStats = new int[]{0, 0, 0, 0};
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

            if (periodo > 4) {
                textView_periodo.setText("Fin"); //Asignamos el String al TextView con el periodo actualizado
            } else {
                textView_periodo.setText(periodoStr); //Asignamos el String al TextView con el periodo actualizado
            }

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
                imageButton_Play.setEnabled(false); //Desactivamos el botón de Play
                imageButton_Stop.setEnabled(false); //Desactivamos el botón de Stop
                textView_periodo.setText("Fin"); //Asignamos el String al TextView con el periodo actualizado
                Toast.makeText(this, "El partido ha terminado", Toast.LENGTH_SHORT).show(); //Mostramos un mensaje emergente de que el partido ha terminado
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
                for (int i = 0; i < 13; i++) {
                    CardView cardView = cardViewsEnPista[i]; //Comprobamos el CardView que toca según la i del for
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
            String tag = (String) cardView.getTag(); //Obtenemos y guardamos el Tag del CardView que queremos actualizar las estadísticas
            int[] playerStats = statsJugadoresMap.get(tag); //Obtenemos y guardamos en un Array de ints las estadísticas del jugador

            if (textView_periodo.getText().toString().equals("Fin")) {
                Toast.makeText(getApplicationContext(), "El partido ha terminado", Toast.LENGTH_SHORT).show(); //Mostramos un mensaje emergente de que el partido ha terminado
            } else {
                //Actualizamos las estadísticas correspondientes según que ImageView sea dropeado
                switch (statTag) {
                    //El ImageView es el de triple anotado
                    case "tripleAnotado":
                        if (Objects.equals(tag, "marcador")) {
                            marcadorStats[1] += 3; //Si el tag es marcador, se suma tres a los puntos al equipo visitante
                        } else {
                            playerStats[1]++; //Se suma uno a los triples metidos
                            playerStats[2]++; //Se suma uno a los triples tirados
                            playerStats[0] += 3; //se suman tres a los puntos totales
                            playerStats[16] += 3; //Se suma tres a la valoración
                            marcadorStats[0] += 3; //Se suma tres a los puntos del equipo local
                        }

                        break;

                    //El ImageView es el de triple fallado
                    case "tripleFallado":
                        if (tag.equals("marcador")) {
                            //No se hace nada
                        } else {
                            playerStats[2]++; //Se suma uno a los tiros tirados
                            playerStats[16]--; //Se resta uno a la valoración
                        }

                        break;

                    //El ImageView es el de tiro de dos anotado
                    case "dosAnotado":
                        if (tag.equals("marcador")) {
                            marcadorStats[1] += 2; //Si el tag es marcador, se suma dos a los puntos del equipo visitante
                        } else {
                            playerStats[3]++; //Se suma uno a los tiros de dos anotados
                            playerStats[4]++; //Se suma uno a los tiros de dos tirados
                            playerStats[0] += 2; //Se suma dos a los puntos totales
                            playerStats[16] += 2; //Se suma dos a la valoración
                            marcadorStats[0] += 2; //Se suma dos a los puntos del equipo local
                        }

                        break;

                    //El ImageView es el de tiro de dos fallado
                    case "dosFallado":
                        if (tag.equals("marcador")) {
                            //No se hace nada
                        } else {
                            playerStats[4]++; //Se suma uno a los tiros de dos tirados
                            playerStats[16]--; //Se resta uno a la valoración
                        }

                        break;

                    //El ImageView es el de tiro libre anotado
                    case "libreAnotado":
                        if (tag.equals("marcador")) {
                            marcadorStats[1] += 1; //Si el tag es marcador, se suma uno a los puntos del equipo visitante
                        } else {
                            playerStats[5]++; //Se suma uno a los tiros libres anotados
                            playerStats[6]++; //Se suma uno a los tiros libres tirados
                            playerStats[0] += 1; //Se suma uno a los puntos totales
                            playerStats[16] += 1; //Se suma uno a la valoración
                            marcadorStats[0] += 1; //Se suma uno a los puntos del equipo local
                        }

                        break;

                    //El ImageView es el de tiro libre fallado
                    case "libreFallado":
                        if (tag.equals("marcador")) {
                            //No se hace nada
                        } else {
                            playerStats[6]++; //Se suma uno a los tiros libres tirados
                            playerStats[16]--; //Se resta uno a la valoración
                        }

                        break;

                    //El ImageView es el de rebote defensivo
                    case "reboteDefensivo":
                        if (tag.equals("marcador")) {
                            //No se hace nada
                        } else {
                            playerStats[7]++; //Se suma uno a rebote defensivo
                            playerStats[9]++; //Se suma uno a los rebotes totales
                            playerStats[16]++; //Se suma uno a la valoración
                        }

                        break;

                    //El ImageView es el de rebote ofensivo
                    case "reboteOfensivo":
                        if (tag.equals("marcador")) {
                            //No se hace nada
                        } else {
                            playerStats[8]++; //Se suma uno a los rebotes ofensivos
                            playerStats[9]++; //Se suma uno a los rebotes totales
                            playerStats[16]++; //Se suma uno a la valoración
                        }

                        break;

                    //El ImageView es el de asistencia
                    case "asistencia":
                        if (tag.equals("marcador")) {
                            //No se hace nada
                        } else {
                            playerStats[10]++; //Se suma uno a las asistencias
                            playerStats[16]++; //Se suma uno a la valoración
                        }

                        break;

                    //El ImageView es el del robo
                    case "robo":
                        if (tag.equals("marcador")) {
                            //No se hace nada
                        } else {
                            playerStats[11]++; //Suma uno a los robos
                            playerStats[16]++; //Suma uno a la valoración
                        }

                        break;

                    //El ImageView es el del tapón
                    case "tapon":
                        if (tag.equals("marcador")) {
                            //No se hace nada
                        } else {
                            playerStats[12]++; //Suma uno a los tapones
                            playerStats[16]++; //Suma uno a la valoración
                        }

                        break;

                    //El ImageView es el de la pérdida
                    case "perdida":
                        if (tag.equals("marcador")) {
                            //No se hace nada
                        } else {
                            playerStats[13]++; //Suma uno a las pérdidas
                            playerStats[16]--; //Resta uno a la valoración
                        }

                        break;

                    //El ImageView es el de la falta recibida
                    case "faltaRecibida":
                        if (tag.equals("marcador")) {
                            marcadorStats[3]++; //Si el tag es marcador, se suma uno a las faltas del equipo visitante
                        } else {
                            playerStats[14]++; //Suma uno a las faltas recibidas
                            playerStats[16]++; //Suma uno a la valoración
                            marcadorStats[3]++; //Se suma uno a las faltas del equipo visitante
                        }

                        break;

                    //El ImageView es el de la falta cometida
                    case "faltaCometida":
                        if (tag.equals("marcador")) {
                            marcadorStats[2]++; //Se el tag es uno, se suma uno a las faltas del equipo local
                        } else {
                            playerStats[15]++; //Suma uno a las faltas cometidas
                            playerStats[16]--; //Resta uno a la valoración
                            marcadorStats[2]++; //Se suma uno a las faltas del equipo local
                        }

                        break;

                    //Opción default que no hace nada
                    default:
                        return;
                }

                if (tag.equals("marcador")) {
                    //Actualiza el TextView de los puntos del equipo local en el marcador
                    TextView puntosLocalTextView = marcador.findViewById(R.id.textViewPuntosLocal);
                    puntosLocalTextView.setText(String.valueOf(marcadorStats[0]));

                    //Actualiza el TextView de los puntos del equipo visitante en el marcador
                    TextView puntosVisitanteTextView = marcador.findViewById(R.id.textViewPuntosVisitante);
                    puntosVisitanteTextView.setText(String.valueOf(marcadorStats[1]));

                    //Actualiza el TextView de las faltas del equipo local en el marcador
                    TextView faltasLocalTextView = marcador.findViewById(R.id.textViewNumeroFaltasLocal);
                    faltasLocalTextView.setText(String.valueOf(marcadorStats[2]));

                    //Actualiza el TextView de las faltas del equipo visitante en el marcador
                    TextView faltasVisitanteTextView = marcador.findViewById(R.id.textViewNumeroFaltasVisitante);
                    faltasVisitanteTextView.setText(String.valueOf(marcadorStats[3]));
                } else {
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

                    //Actualiza el TextView de los puntos del equipo local en el marcador
                    TextView puntosLocalTextView = marcador.findViewById(R.id.textViewPuntosLocal);
                    puntosLocalTextView.setText(String.valueOf(marcadorStats[0]));

                    //Actualiza el TextView de los puntos del equipo visitante en el marcador
                    TextView puntosVisitanteTextView = marcador.findViewById(R.id.textViewPuntosVisitante);
                    puntosVisitanteTextView.setText(String.valueOf(marcadorStats[1]));

                    //Actualiza el TextView de las faltas del equipo local en el marcador
                    TextView faltasLocalTextView = marcador.findViewById(R.id.textViewNumeroFaltasLocal);
                    faltasLocalTextView.setText(String.valueOf(marcadorStats[2]));

                    //Actualiza el TextView de las faltas del equipo visitante en el marcador
                    TextView faltasVisitanteTextView = marcador.findViewById(R.id.textViewNumeroFaltasVisitante);
                    faltasVisitanteTextView.setText(String.valueOf(marcadorStats[3]));
                }

                //Actualiza las estadísticas del jugador en el Map con las nuevas estadísticas
                statsJugadoresMap.put(tag, playerStats);
            }
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

        public void sustituirJugadores(CardView jugadorEntra, CardView jugadorSale) {
            Rect rect1 = new Rect();
            jugadorEntra.getGlobalVisibleRect(rect1);
            Rect rect2 = new Rect();
            jugadorSale.getGlobalVisibleRect(rect2);
            if (Rect.intersects(rect1, rect2)) {
                // Get the parent ViewGroup
                ViewGroup parent = (ViewGroup) jugadorEntra.getParent();

                // Remove both CardViews from the parent ViewGroup
                parent.removeView(jugadorEntra);
                parent.removeView(jugadorSale);

                // Add the CardViews back to the parent ViewGroup in the reverse order
                parent.addView(jugadorSale);
                parent.addView(jugadorEntra);

                // Request a layout update
                parent.requestLayout();
            }
        }
    }
}