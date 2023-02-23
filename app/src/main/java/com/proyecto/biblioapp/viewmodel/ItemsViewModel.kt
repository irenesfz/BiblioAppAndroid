package com.proyecto.biblioapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyecto.biblioapp.auth.CheckJWT
import com.proyecto.biblioapp.auth.Token
import com.proyecto.biblioapp.clases.*
import com.proyecto.biblioapp.utils.FiltroBuscarLibro

class ItemsViewModel : ViewModel() {
    private val libro = MutableLiveData<Libros>()
    private val unidadLibro = MutableLiveData<Libro>()
    private val socio = MutableLiveData<Socio?>()
    private val mensaje = MutableLiveData<Mensaje>()
    private var Libro = MutableLiveData<ArrayList<Libros>>()
    private var prestamos = MutableLiveData<ArrayList<Prestamo>>()
    private var prestamosNoFinalizados = MutableLiveData<ArrayList<Prestamo>>()
    private var libroFiltrado = MutableLiveData<ArrayList<Libros>>()
    private var libroMuestra = MutableLiveData<ArrayList<Libros>>()
    private var reservas = MutableLiveData<ArrayList<Reservas>>()
    private var reservasNoFinalizadas = MutableLiveData<ArrayList<Reservas>>()
    private var estadoFiltroLibro = MutableLiveData<FiltroBuscarLibro>()
    private var loginJWT = MutableLiveData<Token>()
    private var cookie = MutableLiveData<String>()
    private var visibilidadCategorias = MutableLiveData<Boolean>()
    private var prestamosFiltrados = MutableLiveData<ArrayList<Prestamo>>()
    private var checkJWTVariable = MutableLiveData<Boolean>()



    val getCheckJWTVariable: LiveData<Boolean> get() = checkJWTVariable

    fun setCheckJWTVariable(item: Boolean) {
        checkJWTVariable.value = item
    }

    val getPrestamosFiltrados: LiveData<ArrayList<Prestamo>> get() = prestamosFiltrados

    fun setPrestamosFiltrados(item: ArrayList<Prestamo>) {
        prestamosFiltrados.value = item
    }

    val getVisibilidadCategorias: LiveData<Boolean> get() = visibilidadCategorias

    fun setVisibilidadCategorias(item: Boolean) {
        visibilidadCategorias.value = item
    }

    val getUnidadLibroPrestamo: LiveData<Libro> get() = unidadLibro

    fun setUnidadLibroPrestamo(item: Libro) {
        unidadLibro.value = item
    }
    val getUnidadLibroReservas: LiveData<Libro> get() = unidadLibro

    fun setUnidadLibroReservas(item: Libro) {
        unidadLibro.value = item
    }

    //cookies
    val getCookie: LiveData<String> get() = cookie

    fun setCookie(item: String) {
        cookie.value = item
    }

    //todas las reservas
    val getReservasNoFinalizadas: LiveData<ArrayList<Reservas>> get() = reservasNoFinalizadas

    fun setReservasNoFinalizadas(item: ArrayList<Reservas>) {
        reservasNoFinalizadas.value = item
    }

    //reservas no finalizadas
    val getReservas: LiveData<ArrayList<Reservas>> get() = reservas

    fun setReservas(item: ArrayList<Reservas>) {
        reservas.value = item
    }

    //LOGIN jwt
    val getLoginJWT: LiveData<Token> get() = loginJWT

    fun setLoginJWT(item: Token) {
        loginJWT.value = item
    }

    //todos los prestamos
    val getPrestamos: LiveData<ArrayList<Prestamo>> get() = prestamos

    fun setPrestamos(item: ArrayList<Prestamo>) {
        prestamos.value = item
    }

    //prestamos no finalizados
    val getPrestamosNoFinalizados: LiveData<ArrayList<Prestamo>> get() = prestamosNoFinalizados

    fun setPrestamosNoFinalizado(item: ArrayList<Prestamo>) {
        prestamosNoFinalizados.value = item
    }

    //filtro libros
    val getEstadoFiltroLibro: LiveData<FiltroBuscarLibro> get() = estadoFiltroLibro

    fun setEstadoFiltroLibro(item: FiltroBuscarLibro) {
        estadoFiltroLibro.value = item
    }

    //mensaje api
    val getMensaje: LiveData<Mensaje> get() = mensaje

    fun setMensaje(item: Mensaje) {
        mensaje.value = item
    }

    //socio loggeado
    val getSocio: LiveData<Socio?> get() = socio

    fun setSocio(item: Socio?) {
        socio.value = item
    }

    //visibilidad socios
    private val liveData = MutableLiveData<Boolean>()
    val getVisibilidadSocios: LiveData<Boolean> get() = liveData
    fun setVisibilidadSocios(item: Boolean) {
        liveData.value = item
    }

    //visibilidad menu
    private val liveDataMenu = MutableLiveData<Boolean>()
    val getVisibilidadMenu: LiveData<Boolean> get() = liveDataMenu
    fun setVisibilidadMenu(item2: Boolean) {
        liveDataMenu.value = item2
    }

    //libros filtrados
    fun setLibrosFiltrados(values: ArrayList<Libros>) {
        libroFiltrado.value = values
    }

    fun getLibrosFiltrados(): LiveData<ArrayList<Libros>> {
        return libroFiltrado
    }

    //libros para mostrar con el adapter
    fun setLibroMuestra(values: ArrayList<Libros>) {
        libroMuestra.value = values
    }

    fun getLibroMuestra(): LiveData<ArrayList<Libros>> {
        return libroMuestra
    }

    fun setLibros(values: ArrayList<Libros>) {
        Libro.value = values
    }

    fun getLibros(): LiveData<ArrayList<Libros>> {
        return Libro
    }

    val getLibro: LiveData<Libros> get() = libro

    fun setLibro(item: Libros) {
        libro.value = item
    }

    private var Evento = MutableLiveData<ArrayList<Evento>>()
    fun setEventos(values: ArrayList<Evento>) {
        Evento.value = values
    }

    fun getEventos(): LiveData<ArrayList<Evento>> {
        return Evento
    }

}