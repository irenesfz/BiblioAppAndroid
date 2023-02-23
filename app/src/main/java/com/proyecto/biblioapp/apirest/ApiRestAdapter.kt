package com.proyecto.biblioapp.apirest

import android.util.Log
import com.proyecto.biblioapp.auth.CheckJWT
import com.proyecto.biblioapp.auth.Sesion
import com.proyecto.biblioapp.auth.Token
import com.proyecto.biblioapp.auth.Usuario
import com.proyecto.biblioapp.clases.*
import com.proyecto.biblioapp.utils.FiltroBuscarLibro
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiRestAdapter {

    private var cookie: String = ""

    /**
     * Función encargada de conectarse con ProveedorServicio y establecer conexión con la API
     * a través de la url proporcionada.
     * @return ProveedorServicio
     */
    private fun crearRetrofit(): ProveedorServicio {

        // val url = "http://127.0.0.1:8081/biblioapp/v1/" //para el AVD de android
        //LOCAL CASA
        // val url = "http://192.168.0.21:8081/biblioapp/v1/" //para el AVD de android
        //AZURE
        val url =
            "http://biblioapp.eastus.cloudapp.azure.com:8081/biblioapp/v1/" //para el AVD de android

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ProveedorServicio::class.java)
    }


    //*************************
    //*********SOCIO***********
    //*************************

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para obtener los datos del socio que ha iniciado sesión.
     * @param user String usuario que inicia sesión.
     * @param token String token para que la API nos reconozca como socios en su seguridad.
     * @return Deferred<Socio?> datos del socio que inicia sesión.
     */
    fun cargaSocio2(user: String, token: String): Deferred<Socio?> {
        // MainActivity.libros.clear()
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev: Socio? = null
            val response: Response<Socio> = proveedorServicios.getSocio(user, cookie, token)
            if (response.isSuccessful) {
                //  MainActivity.libros = response.body()!!
                datosDev = response.body()!!
            }
            datosDev
        }
    }

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para cambiar la contraseña del socio que previamente ha iniciado sesión.
     * @param user String usuario socio que solicita cambiar contraseña.
     * @param newPass String nueva contraseña.
     * @param oldPass String contraseña a cambiar.
     * @param token String token para que la API nos reconozca como socios en su seguridad.
     * @return Deferred<Mensaje?> mensaje para comprobar que se ha cambiado correctamente la contraseña.
     */
    fun cambiaContrasenya(
        user: String,
        newPass: String,
        oldPass: String,
        token: String
    ): Deferred<Mensaje?> {
        // MainActivity.libros.clear()
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev: Mensaje? = null
            val response: Response<Mensaje> =
                proveedorServicios.cambiaPassword(user, newPass, oldPass, cookie, token)
            if (response.isSuccessful) {
                datosDev = response.body()!!
                //  Log.d("Error", datosDev.mensaje)
            }

            datosDev
        }
    }

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para cambiar el correo electónico del socio.
     * @param correo String correo electrónico a cambiar.
     * @param idSocio Int id del socio a cambiar el correo.
     * @param token String token para que la API nos reconozca como socios en su seguridad.
     * @return Deferred<Mensaje?> mensaje para comprobar que se ha cambiado correctamente.
     */
    fun cambiaCorreoElectronico(correo: String, idSocio: Int, token: String): Deferred<Mensaje?> {
        // MainActivity.libros.clear()
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev: Mensaje? = null
            val response: Response<Mensaje> =
                proveedorServicios.editaSocio(idSocio, correo, cookie, token)
            if (response.isSuccessful) {
                datosDev = response.body()!!
            }
            datosDev
        }
    }

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para cambiar las categorías de interés del socio.
     * @param categorias String categorias a cambiar.
     * @param idSocio Int id del socio a cambiar las categorías.
     * @param token String token para que la API nos reconozca como socios en su seguridad.
     * @return Deferred<Mensaje?> mensaje para comprobar que se ha cambiado correctamente.
     */
    fun cambiaCategorias(categorias: String, idSocio: Int, token: String): Deferred<Mensaje?> {
        // MainActivity.libros.clear()
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev: Mensaje? = Mensaje()
            val response: Response<Mensaje> =
                proveedorServicios.editaCategoriasSocio(idSocio, categorias, cookie, token)
            if (response.code() == 200) {
                if (response.isSuccessful) {
                    datosDev = response.body()!!
                }
            }
            datosDev
        }
    }

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para cambiar la imagen de perfil del socio.
     * @param imagen String imagen a cambiar.
     * @param idSocio Int id del socio a cambiar la foto.
     * @param token String token para que la API nos reconozca como socios en su seguridad.
     * @return Deferred<Mensaje?> mensaje para comprobar que se ha cambiado correctamente.
     */
    fun cambiaImagenPerfil(imagen: String, idSocio: Int, token: String): Deferred<Mensaje?> {
        // MainActivity.libros.clear()
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev: Mensaje? = null
            val response: Response<Mensaje> =
                proveedorServicios.editaSocioImagen(idSocio, imagen, cookie, token)
            if (response.isSuccessful) {
                datosDev = response.body()!!
                Log.d("Error", datosDev.mensaje.toString())
            }
            datosDev
        }
    }

    //*************************
    //*********AUTH************
    //*************************

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para comprobar si la sesión en la que está el socio en estos momentos es válida y no ha caducado.
     * @param token String token para que la API nos reconozca como socios en su seguridad.
     * @return Deferred<Boolean?> booleano que comprueba si la sesión es válida o no.
     */
    fun checkJWT(token: String): Deferred<Boolean?> {
        // MainActivity.libros.clear()
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            val response: Response<CheckJWT> = proveedorServicios.checkJWT(cookie, token)
            val datosDev = response.code().toString() == "202"
            Log.d("ErrorValidate", response.code().toString())
            Log.d("ErrorValidate", datosDev.toString())
            datosDev
        }
    }

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para iniciar sesión como socio en la aplicación móvil.
     * @param usuario Usuario usuario socio a iniciar sesión.
     * @return Deferred<Token?> token con los datos de la sesión del socio.
     */
    fun loginJWT(usuario: Usuario): Deferred<Token?> {
        // MainActivity.libros.clear()
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev: Token? = null
            val response: Response<Token> = proveedorServicios.loginJWT(usuario)
            cookie = limpiarCookie(response.headers().get("Set-Cookie")).toString()

            if (response.isSuccessful) {
                Log.d("Error", response.body()!!.JWT.toString())
                datosDev = response.body()!!
            }

            datosDev
        }
    }

    /**
     * Función con la que obtenemos los datos de la cookie que nos interesan para poder comunicarnos
     * con la API de manera óptima.
     * @param cookie String? cookie a modificar.
     * @return String? cookie a utilizar para la comunicación con la API.
     */
    private fun limpiarCookie(cookie: String?): String? {
        var auxCookie = cookie?.split(';')?.get(0)
        auxCookie?.removePrefix("JSESSIONID=")
        return auxCookie
    }

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para hacer un logout de la sesión actual.
     * @return Deferred<Sesion?> sesión actual del socio.
     */
    fun logout(): Deferred<Sesion?> {
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev: Sesion? = null
            val response: Response<Sesion> = proveedorServicios.logout()
            if (response.isSuccessful) {
                datosDev = response.body()
            }
            datosDev
        }
    }

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para hacer un destroy session de la sesión actual.
     * @return Deferred<Sesion?> sesión actual del socio.
     */
    fun destroySession(): Deferred<Sesion?> {
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev: Sesion? = null
            cookie = ""
            val response: Response<Sesion> = proveedorServicios.destroySession()
            if (response.isSuccessful) {
                datosDev = response.body()!!
            }
            datosDev
        }
    }

    //*************************
    //*******LIBRO*************
    //*************************

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para buscar un libro disponible para reservar según su ISBN.
     * @param isbn String isbn del libro a buscar.
     * @return Deferred<Libro> libro obtenido.
     */
    fun cargaUnidadLibroReserva(isbn: String): Deferred<Libro> {
        // MainActivity.libros.clear()
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev = Libro()
            val response: Response<Libro> = proveedorServicios.buscaReservaPorISBN(isbn)

            if (response.isSuccessful) {
                //  MainActivity.libros = response.body()!!
                Log.d("Error1", response.body().toString())
                if (response.body().toString() != "null")
                    datosDev = response.body()!!
            }
            datosDev
        }
    }

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para buscar un libro disponible como préstamo según su ISBN.
     * @param isbn String isbn del libro a buscar.
     * @return Deferred<Libro> libro obtenido.
     */
    fun cargaUnidadLibroPrestamo(isbn: String): Deferred<Libro> {
        // MainActivity.libros.clear()
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev = Libro()
            val response: Response<Libro> = proveedorServicios.buscaPrestamoPorISBN(isbn)
            if (response.isSuccessful) {
                //  MainActivity.libros = response.body()!!
                if (response.body().toString() != "null")
                    datosDev = response.body()!!
                else datosDev = Libro()
            }
            datosDev
        }
    }

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para marcar como unidad disponible como reserva una unidad de libro en concreto.
     * @param idLibro Int id del libro a buscar.
     * @param apikey String token para que la API nos reconozca como socios en su seguridad.
     * @return Deferred<Mensaje> mensaje para comprobar que se han actualizado correctamente los datos.
     */
    fun unidadLibroDisponibleReserva(idLibro: Int, apikey: String): Deferred<Mensaje> {
        // MainActivity.libros.clear()
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev: Mensaje = Mensaje()
            val response: Response<Mensaje> =
                proveedorServicios.marcaUnidadComoDisponibleReserva(idLibro, cookie, apikey)
            if (response.code() == 200) {
                datosDev.mensaje = "Libro actualizado"
            } else {
                datosDev.mensaje = "ERROR"
            }
            Log.d("Error12", response.body().toString())
            Log.d("Error12", response.code().toString())
            datosDev
        }
    }


    //*************************
    //*******LIBROS************
    //*************************

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para obtener todos los libros de la biblioteca.
     * @return Deferred<ArrayList<Libros>> lista de libros..
     */
    fun cargaCatalogo(): Deferred<ArrayList<Libros>> {
        // MainActivity.libros.clear()
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev = ArrayList<Libros>()
            val response: Response<ArrayList<Libros>> = proveedorServicios.muestraLibrosBiblioteca()
            if (response.isSuccessful) {
                //  MainActivity.libros = response.body()!!
                datosDev = response.body()!!
            }
            datosDev
        }
    }

    /**
     * Función encargada de hacer una llamada a la API a través de proveedorServicios para obtener
     * un libro segúm su id.
     * @param id Int id del libro a buscar.
     * @return Deferred<Libros> libro encontrado.
     */
    fun cargaLibro(id: Int): Deferred<Libros> {
        // MainActivity.libros.clear()
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev = Libros()
            val response: Response<Libros> = proveedorServicios.muestraLibroPorId(id)
            if (response.isSuccessful) {
                //  MainActivity.libros = response.body()!!
                datosDev = response.body()!!
                Log.d("Error", datosDev.titulo.toString())
            }
            datosDev
        }
    }

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios para
     * obtener todos los libros de la biblioteca que coincidan con la balabra a buscar
     * @param palabraABuscar String palabra con la que buscaremos coincidencias.
     * @param filtroBusqueda FiltroBuscarLibro el tipo por el que buscamos libro (autor,título,isbn...)
     * @return Deferred<ArrayList<Libros>> lista de libros con las coincidencias
     */
    fun cargaLibrosFiltrados(
        palabraABuscar: String,
        filtroBusqueda: FiltroBuscarLibro
    ): Deferred<ArrayList<Libros>> {
        // MainActivity.libros.clear()
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev = ArrayList<Libros>()
            val response: Response<ArrayList<Libros>> = when (filtroBusqueda) {
                FiltroBuscarLibro.Titulo -> proveedorServicios.buscaLibrosPorTitulo(palabraABuscar)
                FiltroBuscarLibro.Editorial -> proveedorServicios.buscaLibrosPorEditorial(
                    palabraABuscar
                )
                FiltroBuscarLibro.Autores -> proveedorServicios.buscaLibrosPorAutor(palabraABuscar)
                FiltroBuscarLibro.Categoria -> proveedorServicios.buscaLibrosPorCategoria(
                    palabraABuscar
                )
                FiltroBuscarLibro.ISBN -> proveedorServicios.buscaLibrosPorISBN(palabraABuscar)
                FiltroBuscarLibro.AnyoPublicacion -> proveedorServicios.buscaLibrosPorPublicacion(
                    palabraABuscar
                )
                FiltroBuscarLibro.Todo -> proveedorServicios.buscaLibrosTodasCategorias(
                    palabraABuscar
                )
            }

            if (response.isSuccessful) {
                // MainActivity.libros = response.body()!!
                datosDev = response.body()!!
            }
            datosDev
        }
    }

    //*************************
    //********EVENTOS**********
    //*************************

    /**
     * Función encargada de hacer una llamada a la API a través de proveedorServicios para obtener
     * todos los eventos de la biblioteca
     * @return Deferred<ArrayList<Evento>> lista de eventos de la biblioteca
     */
    fun cargaEventos(): Deferred<ArrayList<Evento>> {
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev = ArrayList<Evento>()
            val response: Response<ArrayList<Evento>> =
                proveedorServicios.muestraEventosBiblioteca()
            if (response.isSuccessful) {
                // MainActivity.eventos = response.body()!!
                datosDev = response.body()!!
            }
            datosDev
        }
    }

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios para
     * obtener todos los eventos según una fecha concreta
     * @param fecha String fecha por la que buscar libro
     * @return Deferred<ArrayList<Evento>> lista de eventos que coincidan con esa fecha
     */
    fun cargaEventosPorFecha(fecha: String): Deferred<ArrayList<Evento>> {
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev = ArrayList<Evento>()
            val response: Response<ArrayList<Evento>> =
                proveedorServicios.muestraEventosPorFecha(fecha)
            if (response.isSuccessful) {
                // MainActivity.eventos = response.body()!!
                datosDev = response.body()!!
            }
            datosDev
        }
    }

    //*************************
    //*******PRÉSTAMOS*********
    //*************************

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios para
     * obtener todos los préstamos no funalizados de un socio en concreto.
     * @param idSocio Int id del socio a buscar.
     * @param token String token para que la API nos reconozca como socios en su seguridad.
     * @return Deferred<ArrayList<Prestamo>> lista de préstamos.
     */
    fun cargaPrestamosNoFinalizadosSocio(
        idSocio: Int,
        token: String
    ): Deferred<ArrayList<Prestamo>> {
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev: ArrayList<Prestamo>
            val response: Response<ArrayList<Prestamo>> =
                proveedorServicios.muestraPrestamosNoFinalizadosSocio(idSocio, cookie, token)
            Log.d("Error", response.code().toString())
            if (response.code() == 200) {
                if (response.isSuccessful) {
                    // MainActivity.eventos = response.body()!!
                    datosDev = response.body()!!
                    Log.d("Error", datosDev.size.toString())
                } else {
                    datosDev = ArrayList()
                }
            } else {
                datosDev = ArrayList()
            }
            datosDev
        }
    }

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para obtener los préstamos que terminen en una fecha específica.
     * @param fecha String fecha a buscar.
     * @param idSocio Int id del socio para identificar los préstamos de ese socio.
     * @param token String token para que la API nos reconozca como socios en su seguridad.
     * @return Deferred<ArrayList<Prestamo>> lista de préstamos filtrados por esa fecha.
     */
    fun cargaPrestamosPorFecha(
        fecha: String,
        idSocio: Int,
        token: String
    ): Deferred<ArrayList<Prestamo>> {
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev = ArrayList<Prestamo>()
            val response: Response<ArrayList<Prestamo>> =
                proveedorServicios.muestraPrestamosPorFecha(fecha, idSocio, cookie, token)
            proveedorServicios.muestraPrestamosPorFecha(fecha, idSocio, cookie, token)
            if (response.isSuccessful) {
                datosDev = response.body()!!
            }
            datosDev
        }
    }

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para obtener el historial de préstamos de un socio en concreto.
     * @param idSocio Int id del socio para buscar sus préstamos.
     * @param token String token para que la API nos reconozca como socios en su seguridad.
     * @return Deferred<ArrayList<Prestamo>> lista de préstamos según el socio.
     */
    fun cargaHistorialPrestamos(
        idSocio: Int,
        token: String
    ): Deferred<ArrayList<Prestamo>> {
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev = ArrayList<Prestamo>()
            val response: Response<ArrayList<Prestamo>> =
                proveedorServicios.muestraPrestamosSocio(idSocio, cookie, token)
            if (response.code() == 200) {
                if (response.isSuccessful) {
                    // MainActivity.eventos = response.body()!!
                    datosDev = response.body()!!
                    Log.d("Error", datosDev.size.toString())
                } else {
                    datosDev = ArrayList()
                }
            } else {
                datosDev = ArrayList()
            }
            datosDev
        }
    }

    //*************************
    //*******RESERVAS**********
    //*************************

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para reservar un libro.
     * @param idLibro  id del libro a reservar.
     * @param idSocio Int id del socio que ha solicitado la reserva.
     * @param token String token para que la API nos reconozca como socios en su seguridad.
     * @return Deferred<Mensaje> mensaje para comrpobar que se ha reservado correctamente.
     */
    fun reservaLibro(
        idLibro: Int,
        idSocio: Int,
        token: String
    ): Deferred<Mensaje> {
        // MainActivity.libros.clear()
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev = Mensaje()
            val response: Response<Mensaje> =
                proveedorServicios.addReserva(idSocio, idLibro, cookie, token)
            Log.d("Error1", idSocio.toString())
            Log.d("Error1", idLibro.toString())
            if (response.isSuccessful) {
                //  MainActivity.libros = response.body()!!
                datosDev = response.body()!!
                Log.d("Error22", datosDev.mensaje.toString())
            }
            datosDev
        }
    }

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para poner una notificación sobre un libro reservado en concreto.
     * @param idReserva Int id de la reserva a poner notificación.
     * @param token String token para que la API nos reconozca como socios en su seguridad.
     * @return Deferred<Mensaje> mensaje para comprpobar que se ha activado la notificación correctamente.
     */
    fun poneNotification(idReserva: Int, token: String): Deferred<Mensaje> {
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev = Mensaje()
            val response: Response<Mensaje> =
                proveedorServicios.poneNotification(idReserva, cookie, token)
            if (response.isSuccessful) {
                // MainActivity.eventos = response.body()!!
                datosDev = response.body()!!
            }
            datosDev
        }
    }

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para quitar una notificación sobre un libro reservado en concreto.
     * @param idReserva Int id de la reserva a quitar notificación.
     * @param token String token para que la API nos reconozca como socios en su seguridad.
     * @return Deferred<Mensaje> mensaje para comprpobar que se ha desactivado la notificación correctamente.
     */
    fun quitaNotification(idReserva: Int, token: String): Deferred<Mensaje> {
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev = Mensaje()
            val response: Response<Mensaje> =
                proveedorServicios.quitaNotification(idReserva, cookie, token)
            if (response.isSuccessful) {
                // MainActivity.eventos = response.body()!!
                datosDev = response.body()!!
            }
            datosDev
        }
    }

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para finalizar una reserva.
     * @param idReservaInt id de la reserva a finalizar.
     * @param idLibro Int id del libro a finalizar reserva.
     * @param token String token para que la API nos reconozca como socios en su seguridad.
     * @return Deferred<Mensaje> mensaje para comprpobar que ha finalizado correctamente la reserva.
     */
    fun quitaReserva(idReserva: Int, idLibro: Int, token: String): Deferred<Mensaje> {
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev = Mensaje()
            val response: Response<Mensaje> =
                proveedorServicios.quitaReserva(idReserva, idLibro, cookie, token)
            if (response.isSuccessful) {
                // MainActivity.eventos = response.body()!!
                datosDev = response.body()!!
            }
            datosDev
        }
    }

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para obtener los datos de las reservas no finalizadas de un socio en concreto.
     * @param idSocio Int id del socio a buscar sus reservas.
     * @param token String token para que la API nos reconozca como socios en su seguridad.
     * @return Deferred<ArrayList<Reservas>> lista de reservas no finalizadas del socio.
     */
    fun cargaReservasNoFinalizadosSocio(
        idSocio: Int,
        token: String
    ): Deferred<ArrayList<Reservas>> {
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev = ArrayList<Reservas>()
            val response: Response<ArrayList<Reservas>> =
                proveedorServicios.muestraReservasDisponiblesSocio(idSocio, cookie, token)
            if (response.isSuccessful) {
                // MainActivity.eventos = response.body()!!
                datosDev = response.body()!!
                Log.d("Error", datosDev.size.toString())
            }
            datosDev
        }
    }

    /**
     * Función encargada de hacer una llamada asíncrona a la API a través de proveedorServicios
     * para obtener los datos de todas las reservas de un socio en concreto.
     * @param idSocio Int id del socio a buscar sus reservas.
     * @param token String token para que la API nos reconozca como socios en su seguridad.
     * @return Deferred<ArrayList<Reservas>> lista de todas las reservas del socio.
     */
    fun cargaReservas(
        idSocio: Int,
        token: String
    ): Deferred<ArrayList<Reservas>> {
        val proveedorServicios: ProveedorServicio = crearRetrofit()
        return CoroutineScope(Dispatchers.IO).async {
            var datosDev = ArrayList<Reservas>()
            val response: Response<ArrayList<Reservas>> =
                proveedorServicios.muestraTodasReservasSocio(idSocio, cookie, token)
            if (response.isSuccessful) {
                // MainActivity.eventos = response.body()!!
                datosDev = response.body()!!
                Log.d("Error", datosDev.size.toString())
            }
            datosDev
        }
    }
}


