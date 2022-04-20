package com.example.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

    var lista:RecyclerView? = null
    var adaptador:AdaptadorCustom? = null

    //Administramos el Layout(Como queremos que se muestre)
    var layoutManager:RecyclerView.LayoutManager? = null

    //validando si estamos dentro del actionMode
    var isActionMode = false

    var actionMode:ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val platillos = ArrayList<Platillo>()

        platillos.add(Platillo("Platillo 1", 250.0, 3.5F, R.drawable.platillo01))
        platillos.add(Platillo("Platillo 2", 250.0, 3.5F, R.drawable.platillo02))
        platillos.add(Platillo("Platillo 3", 250.0, 3.5F, R.drawable.platillo03))
        platillos.add(Platillo("Platillo 4", 250.0, 3.5F, R.drawable.platillo04))
        platillos.add(Platillo("Platillo 5", 250.0, 3.5F, R.drawable.platillo05))
        platillos.add(Platillo("Platillo 6", 250.0, 3.5F, R.drawable.platillo06))
        platillos.add(Platillo("Platillo 7", 250.0, 3.5F, R.drawable.platillo07))
        platillos.add(Platillo("Platillo 8", 250.0, 3.5F, R.drawable.platillo08))
        platillos.add(Platillo("Platillo 9", 250.0, 3.5F, R.drawable.platillo09))
        platillos.add(Platillo("Platillo 10", 250.0, 3.5F, R.drawable.platillo10))


        lista  = findViewById(R.id.lista)
        //Para poder optimizar el rendimiento de nuestro RV la mejor forma es decirle que esto va a ser de una altura definida. De lo contrario va a tardar mas el Rv en calcular el ancho de cada selda

        lista?.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        //Desde donde queremos que se dibuje el layout
        lista?.layoutManager = layoutManager

        val callback = object: ActionMode.Callback{
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {

                //Inicilizar mi action Mode
                adaptador?.iniciarActionMode()
                actionMode = mode
                menuInflater.inflate(R.menu.menu_contextual, menu!!)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode?.title = "0 Seleccionados"

                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {

                when(item?.itemId){
                    R.id.eliminar ->{

                        adaptador?.eliminarSeleccionados()
                    }
                    else ->{return true}
                }

                adaptador?.terminarActionMode()
                mode?.finish()
                isActionMode = false

                return true
            }

            override fun onDestroyActionMode(p0: ActionMode?) {

                adaptador?.destruirAction()
                isActionMode = false
            }

        }

        adaptador = AdaptadorCustom(platillos, object:ClickListener{
            override fun onClick(vista: View, index: Int) {
                Toast.makeText(applicationContext, platillos.get(index).nombre, Toast.LENGTH_SHORT).show()
            }

        }, object:LongClickListener{
            override fun longClick(vista: View, index: Int) {
               //super.longClick(vista, index)
               if(!isActionMode){
                   startSupportActionMode(callback)
                   isActionMode = true
                   adaptador?.seleccionarItem(index)
               }
                else{
                    //hacer las selecciones o deselecciones
                    adaptador?.seleccionarItem(index)
                }

                actionMode?.title  = adaptador?.obtenerNumElementosSelec().toString() + " Seleccionados"
            }
        })
        lista?.adapter = adaptador



        val swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swipe)
        //cada ves que hagamos un drag and drog de este layout va a mandar a activar este evento
        swipeRefresh.setOnRefreshListener {
            for(i in 1..1000000000){

            }
            swipeRefresh.isRefreshing= false
            platillos.add(Platillo("Nuggets de Pollo", 250.0, 3.5F, R.drawable.platillo10))

            adaptador?.notifyDataSetChanged()
        }
    }
}