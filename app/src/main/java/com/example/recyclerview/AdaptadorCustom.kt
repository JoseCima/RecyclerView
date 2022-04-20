package com.example.recyclerview

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.Array

class AdaptadorCustom(items:ArrayList<Platillo>, var listener: ClickListener, var longCLickListener:LongClickListener) : RecyclerView.Adapter<AdaptadorCustom.ViewHolder>() {

    var items:ArrayList<Platillo>? = null
    var multiSeleccion = false
    var itemsSeleccionados:ArrayList<Int>? = null

    var viewHolder:ViewHolder? = null

    init {
        this.items = items
        itemsSeleccionados = ArrayList()
    }

    //Crea el ViewHoldern y mete el archivo XML en la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorCustom.ViewHolder {

        val vista = LayoutInflater.from(parent.context).inflate(R.layout.template_platillo, parent, false)

        viewHolder = ViewHolder(vista, listener, longCLickListener)

        return viewHolder!!
    }

    override fun onBindViewHolder(holder: AdaptadorCustom.ViewHolder, position: Int) {

        //Para obtener el objeto actual

        val item = items?.get(position)
        holder.imagen?.setImageResource(item?.imagen!!)
        holder.nombre?.text = item?.nombre
        holder.precio?.text = "$" + item?.precio.toString()
        holder.rating?.rating = item?.rating!!

        if(itemsSeleccionados?.contains(position)!!){
            holder.vista.setBackgroundColor(Color.LTGRAY)
        }else{
            holder.vista.setBackgroundColor(Color.WHITE)
        }


    }

    fun iniciarActionMode(){

        multiSeleccion = true
    }

    fun destruirAction(){

        multiSeleccion = false
        itemsSeleccionados?.clear()
        notifyDataSetChanged()
    }

    fun terminarActionMode(){
        //Eliminar elementos seleccionados
        for(item in itemsSeleccionados!!){
            itemsSeleccionados?.remove(item)
        }
        multiSeleccion = false
        notifyDataSetChanged()

    }

    fun seleccionarItem(index:Int){
        if(multiSeleccion){
            if(itemsSeleccionados?.contains(index)!!){
                itemsSeleccionados?.remove(index)
            }else{
                itemsSeleccionados?.add(index)
            }

            notifyDataSetChanged()
        }
    }

    fun obtenerNumElementosSelec():Int{


        return itemsSeleccionados?.count()!!
    }

    fun eliminarSeleccionados(){
        if(itemsSeleccionados?.count()!! > 0){
            //Contiene los items seleccionados que se van a eliminar
            var itemsEliminados = ArrayList<Platillo>()

            for(index in itemsSeleccionados!!){
                itemsEliminados.add(items?.get(index)!!)
            }
            items?.removeAll(itemsEliminados)

            itemsSeleccionados?.clear()

        }
    }

    override fun getItemCount(): Int {

        return items?.count()!!
    }


    class ViewHolder(vista: View, listener: ClickListener, longCLickListener: LongClickListener): RecyclerView.ViewHolder(vista), View.OnClickListener, View.OnLongClickListener{
        var vista = vista
        var imagen: ImageView? = null
        var nombre: TextView? =null
        var precio: TextView? = null
        var rating: RatingBar? = null
        var listener: ClickListener? = null
        var longListener:LongClickListener? = null

        init {
            imagen = vista.findViewById(R.id.imagenPlatillo)
            nombre = vista.findViewById(R.id.nombrePlatillo)
            precio = vista.findViewById(R.id.precioPlatillo)
            rating = vista.findViewById(R.id.ratingPlatillo)
            //El objeto que recibe como tal
            this.listener = listener
            this.longListener = longCLickListener
            vista.setOnClickListener(this)
            vista.setOnLongClickListener(this)

        }


        override fun onClick(v: View?) {

            this.listener?.onClick(v!!, adapterPosition)

        }

        override fun onLongClick(v: View?): Boolean {

            this.longListener?.longClick(v!!, adapterPosition)
            return true
        }


    }


}