package com.proyecto.biblioapp

import android.os.Bundle
import android.os.StrictMode
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.proyecto.biblioapp.apirest.ApiRestAdapter
import com.proyecto.biblioapp.auth.CheckJWT
import com.proyecto.biblioapp.clases.Evento
import com.proyecto.biblioapp.clases.Libros
import com.proyecto.biblioapp.viewmodel.ItemsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Observer

class MainActivity : AppCompatActivity() {
    companion object {
        var libros = arrayListOf<Libros>()//inicializo arraylist

        // var eventos = arrayListOf<Evento>()//inicializo arraylist
        var eventosFiltrados = arrayListOf<Evento>()//inicializo arraylist
    }
    private val model: ItemsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        //supportActionBar!!.hide()
        //lineas añadidas para las imágenes url
        val policy = StrictMode.ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)
        //????????????????

        //cargo los datos de los eventos y libros
        CoroutineScope(Dispatchers.Main).launch {
            model.setEventos(ApiRestAdapter.cargaEventos().await())
            model.setLibros(ApiRestAdapter.cargaCatalogo().await())
        }

    }

}


