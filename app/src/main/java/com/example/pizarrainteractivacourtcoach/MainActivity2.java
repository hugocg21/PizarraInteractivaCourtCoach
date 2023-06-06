package com.example.pizarrainteractivacourtcoach;

import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity2 extends AppCompatActivity {

    private View draggedPlayer; // Jugador arrastrado
    private ViewGroup originalParent; // Contenedor original del jugador arrastrado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        /*LinearLayout banquilloLayout = findViewById(R.id.banquilloLayout);

        for (int i = 0; i < banquilloLayout.getChildCount(); i++) {
            View jugador = banquilloLayout.getChildAt(i);
            jugador.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipData clipData = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(clipData, shadowBuilder, v, 0);
                    draggedPlayer = v;
                    originalParent = (ViewGroup) v.getParent();
                    return true;
                }
            });
        }

        LinearLayout pistaLayout = findViewById(R.id.pistaLayout);

        pistaLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // Verificar si es un jugador válido que se arrastra
                        if (event.getLocalState() instanceof View && event.getLocalState() != v) {
                            v.setBackgroundColor(Color.GREEN); // Cambiar el color del fondo de la pista para indicar una posible posición de soltar
                            return true;
                        }
                        return false;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        v.setBackgroundColor(Color.BLUE); // Cambiar el color del fondo de la pista cuando el jugador se arrastra sobre ella
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        v.setBackgroundColor(Color.GREEN); // Restaurar el color del fondo de la pista cuando el jugador sale de ella
                        return true;
                    case DragEvent.ACTION_DROP:
                        View jugadorArrastrado = (View) event.getLocalState();
                        ViewGroup parent = (ViewGroup) jugadorArrastrado.getParent();
                        parent.removeView(jugadorArrastrado);
                        pistaLayout.addView(jugadorArrastrado);
                        originalParent.addView(v);
                        v.setBackgroundColor(Color.WHITE); // Restaurar el color del fondo de la pista después de soltar
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        v.setBackgroundColor(Color.WHITE); // Restaurar el color del fondo de la pista después de arrastrar
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });*/

        LinearLayout banquilloLayout = findViewById(R.id.banquilloLayout);
        ConstraintLayout pistaLayout = findViewById(R.id.pistaLayout);

// Configurar el arrastre para los CardViews en el banquillo
        for (int i = 0; i < banquilloLayout.getChildCount(); i++) {
            CardView jugador = (CardView) banquilloLayout.getChildAt(i);
            jugador.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipData clipData = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(clipData, shadowBuilder, v, 0);
                    return true;
                }
            });
        }

// Configurar el arrastre y soltar para los CardViews en la pista
        for (int i = 0; i < pistaLayout.getChildCount(); i++) {
            CardView jugador = (CardView) pistaLayout.getChildAt(i);
            jugador.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            v.setBackgroundColor(Color.GREEN); // Cambiar el color del fondo de la pista para indicar una posible posición de soltar
                            return true;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            v.setBackgroundColor(Color.BLUE); // Cambiar el color del fondo de la pista cuando el jugador se arrastra sobre ella
                            return true;
                        case DragEvent.ACTION_DRAG_EXITED:
                            v.setBackgroundColor(Color.GREEN); // Restaurar el color del fondo de la pista cuando el jugador sale de ella
                            return true;
                        case DragEvent.ACTION_DROP:
                            CardView draggedPlayer = (CardView) event.getLocalState();
                            ViewGroup draggedPlayerParent = (ViewGroup) draggedPlayer.getParent();
                            ViewGroup dropTargetParent = (ViewGroup) v.getParent();
                            if (draggedPlayerParent != null && dropTargetParent != null) {
                                int draggedPlayerIndex = draggedPlayerParent.indexOfChild(draggedPlayer);
                                int dropTargetIndex = dropTargetParent.indexOfChild(v);
                                draggedPlayerParent.removeViewAt(draggedPlayerIndex);
                                dropTargetParent.removeViewAt(dropTargetIndex);
                                draggedPlayerParent.addView(v, draggedPlayerIndex);

                                LinearLayout.LayoutParams draggedLayoutParams = (LinearLayout.LayoutParams) draggedPlayer.getLayoutParams();
                                draggedPlayer.setLayoutParams(new LinearLayout.LayoutParams(draggedLayoutParams.width, draggedLayoutParams.height));

                                LinearLayout.LayoutParams targetLayoutParams = (LinearLayout.LayoutParams) v.getLayoutParams();
                                v.setLayoutParams(new LinearLayout.LayoutParams(targetLayoutParams.width, targetLayoutParams.height));

                                dropTargetParent.addView(draggedPlayer, dropTargetIndex);
                            }
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
}