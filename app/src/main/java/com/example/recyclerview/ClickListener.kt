package com.example.recyclerview

import android.view.View

interface ClickListener {
    //Solo declaramos la estructura de la funcion

    fun onClick(vista: View, index:Int)
}