package com.example.pizarrainteractivacourtcoach;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ImageButton imageButton_Play, imageButton_Stop; //Botontes de Play y Stop del marcador
    private TextView textView_tiempo, textView_puntosLocal, textView_puntosVisitante, textView_periodo, textView_faltasLocal, textView_faltasVisitante; //Atributos del marcador
    private TextView textView_puntosJugadorCardView, textView_rebotesJugadorCardView, textView_asistenciasJugadorCardView; //Estadisticas en el CardView
    private CountDownTimer timer; //Timer para el timepo de cuarto
    private boolean tiempoCorriendo = false; //Define si el tiempo esta en corriendo o parado
    private long tiempoCuarto = 600000; //10 minutos en milisegundos
    private CardView cardView_base, cardView_escolta, cardView_alero, cardView_alapivot, cardView_pivot; //Los diferentes CardViews de todos los jugadores en pista


    private int currentPoints;

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
        if (!periodoSTR.contains("PR ")){
            int periodo = Integer.parseInt(periodoSTR); //Como no lo contiene, transformamos el String en un valor int
            periodo++; //Incrementamos en uno el periodo

            String periodoStr = String.valueOf(periodo); //Transformamos el int en String para poder asignarselo al TextView
            textView_periodo.setText(periodoStr); //Asignamos el String al TextView con el periodo actualizado

            if (periodo <= 4){ //Comprobamos si el periodo es menor o igual a 4, los cuartos que tiene un partido de baloncesto
                periodo = Integer.parseInt(textView_periodo.getText().toString()); //Transformamos el String en un valor int
                periodoStr = String.valueOf(periodo); //Transformamos el int en un String para poder asignarlo al TextView
                textView_periodo.setText(periodoStr); //Asignamos el String al TextView con el periodo actualizado
            } else if (!hayProrroga()){ //Comprobamos si al finalizar los 4 periodos hay prórroga
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
            if(hayProrroga()){
                //Dividimos el String que obtenemos del TextView en dos partes, tomando como divisor un espacio, ya que el formato de un periodo de la prórroga es: "PR 0"
                String[] periodoPartes = textView_periodo.getText().toString().split(" ");

                //Asignamos el número del periodo de la prórroga a un int, tomando como valor la segunda parte del String
                int periodoNumero = Integer.parseInt(periodoPartes[1]);

                String periodoStr = "PR " + (periodoNumero + 1); //Creamos un String aplicando el formato de la prórroga, sumando un periodo más
                textView_periodo.setText(periodoStr); //Asignamos el String al TextView
            } else{
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

    private boolean hayProrroga(){
        int puntosLocal = Integer.parseInt(textView_puntosLocal.getText().toString());
        int puntosVisitante = Integer.parseInt(textView_puntosVisitante.getText().toString());

        if(puntosLocal == puntosVisitante){
            return true;
        }

        return false;
    }
}