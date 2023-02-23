package com.proyecto.biblioapp.apirest

import com.proyecto.biblioapp.auth.CheckJWT
import com.proyecto.biblioapp.auth.Sesion
import com.proyecto.biblioapp.auth.Token
import com.proyecto.biblioapp.auth.Usuario
import com.proyecto.biblioapp.clases.*
import retrofit2.Response
import retrofit2.http.*

interface ProveedorServicio {
    //*******************************
    //*LIBROS
    //*******************************


    @GET("libros/all")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun muestraLibrosBiblioteca(): Response<ArrayList<Libros>>

    @GET("libros/id/{id}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun muestraLibroPorId(@Path("id") id: Int): Response<Libros>

    @GET("libros/todo/{palabra}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun buscaLibrosTodasCategorias(@Path("palabra") palabra: String): Response<ArrayList<Libros>>

    @GET("libros/titulo/{titulo}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun buscaLibrosPorTitulo(@Path("titulo") titulo: String): Response<ArrayList<Libros>>

    @GET("libros/editorial/{editorial}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun buscaLibrosPorEditorial(@Path("editorial") editorial: String): Response<ArrayList<Libros>>


    @GET("libros/autores/{autores}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun buscaLibrosPorAutor(@Path("autores") autores: String): Response<ArrayList<Libros>>

    @GET("libros/anyopublicacion/{anyo}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun buscaLibrosPorPublicacion(@Path("anyo") anyo: String): Response<ArrayList<Libros>>

    @GET("libros/isbn/{isbn}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun buscaLibrosPorISBN(@Path("isbn") isbn: String): Response<ArrayList<Libros>>

    @GET("libros/categoria/{categoria}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun buscaLibrosPorCategoria(@Path("categoria") categoria: String): Response<ArrayList<Libros>>


    //*******************************
    //*LIBRO
    //*******************************
    @GET("libro/reserva/{isbn}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun buscaReservaPorISBN(@Path("isbn") isbn: String): Response<Libro>

    @GET("libro/prestamo/{isbn}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun buscaPrestamoPorISBN(@Path("isbn") isbn: String): Response<Libro>

    @PUT("libro/disponibleReserva/{idLibro}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun marcaUnidadComoDisponibleReserva(
        @Path("idLibro") idLibro: Int,
        @Header("Cookie") userCookie: String,
        @Query("apikey") token: String
    ): Response<Mensaje>

  /*  @PUT("libro/reserva/{isbn}/{idLibro}/{idSocio}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun reservaLibro(@Path("isbn") isbn: String,
                             @Path("idLibro") idLibro: Int,
                             @Path("idSocio") idSocio: Int): Response<Mensaje>*/


    //*******************************
    //*EVENTOS
    //*******************************
    @GET("eventos/all")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun muestraEventosBiblioteca(): Response<ArrayList<Evento>>

    @GET("eventos/fecha/{fecha}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun muestraEventosPorFecha(@Path("fecha") fecha: String): Response<ArrayList<Evento>>


    //*******************************
    //*PRÉSTAMOS
    //*******************************
    @GET("prestamos/socio/{idSocio}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun muestraPrestamosSocio(
        @Path("idSocio") idSocio: Int,
        @Header("Cookie") userCookie: String,
        @Query("apikey") token: String
    ): Response<ArrayList<Prestamo>>

    @GET("prestamos/noFinalizadoSocio/{idSocio}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun muestraPrestamosNoFinalizadosSocio(
        @Path("idSocio") idSocio: Int,
        @Header("Cookie") userCookie: String,
        @Query("apikey") token: String
    ): Response<ArrayList<Prestamo>>

    @GET("prestamos/fecha/{fecha}/{idSocio}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun muestraPrestamosPorFecha(
        @Path("fecha") fecha: String,
        @Path("idSocio") idSocio: Int,
        @Header("Cookie") userCookie: String,
        @Query("apikey") token: String
    ): Response<ArrayList<Prestamo>>


    //*******************************
    //*RESERVAS
    //*******************************
    @POST("reservas/add/{idSocio}/{idLibro}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun addReserva(
        @Path("idSocio") idSocio: Int,
        @Path("idLibro") idLibro: Int,
        @Header("Cookie") userCookie: String,
        @Query("apikey") token: String
    ): Response<Mensaje>

    @GET("reservas/noFinalizadaSocio/{idSocio}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun muestraReservasDisponiblesSocio(
        @Path("idSocio") idSocio: Int,
        @Header("Cookie") userCookie: String,
        @Query("apikey") token: String
    ): Response<ArrayList<Reservas>>

    @GET("reservas/socio/{idSocio}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun muestraTodasReservasSocio(
        @Path("idSocio") idSocio: Int,
        @Header("Cookie") userCookie: String,
        @Query("apikey") token: String
    ): Response<ArrayList<Reservas>>

    @PUT("reservas/poneNotification/{idReserva}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun poneNotification(
        @Path("idReserva") idReserva: Int,
        @Header("Cookie") userCookie: String,
        @Query("apikey") token: String
    ): Response<Mensaje>

    @PUT("reservas/quitaNotification/{idReserva}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun quitaNotification(
        @Path("idReserva") idReserva: Int,
        @Header("Cookie") userCookie: String,
        @Query("apikey") token: String
    ): Response<Mensaje>

    @PUT("reservas/{idReserva}/{idLibro}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun quitaReserva(
        @Path("idReserva") idReserva: Int,
        @Path("idLibro") idLibro: Int,
        @Header("Cookie") userCookie: String,
        @Query("apikey") token: String
    ): Response<Mensaje>

    //*******************************
    //*SOCIO
    //*******************************
    //GET SOCIO POR DNI
    /*@GET("socio/dni/{dni}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun getSocio(
        @Path("dni") dni: String
    ): Response<Socio> */

    @GET("socio/dni/{dni}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun getSocio(
        @Path("dni") dni: String,
        @Header("Cookie") userCookie: String,
        @Query("apikey") token: String
    ): Response<Socio>


    //CAMBIA CONTRASEÑA
    @PUT("socio/cambiaPassword/{dni}/{newPass}/{oldPass}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun cambiaPassword(
        @Path("dni") dni: String,
        @Path("newPass") newPass: String,
        @Path("oldPass") oldPass: String,
        @Header("Cookie") userCookie: String,
        @Query("apikey") token: String
    ): Response<Mensaje>

    //CAMBIA CORREO
    @PUT("socio/cambiaCorreo/{id}/{correo}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun editaSocio(
        @Path("id") id: Int,
        @Path("correo") correo: String,
        @Header("Cookie") userCookie: String,
        @Query("apikey") token: String
    ): Response<Mensaje>

    //CAMBIA CATEGORIAS
    @PUT("socio/cambiaCategorias/{id}/{categorias}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun editaCategoriasSocio(
        @Path("id") id: Int,
        @Path("categorias") categorias: String,
        @Header("Cookie") userCookie: String,
        @Query("apikey") token: String
    ): Response<Mensaje>

    //CAMBIA imagen
    @PUT("socio/cambiaImagenPerfil/{id}/{imagen}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun editaSocioImagen(
        @Path("id") id: Int,
        @Path("imagen") imagen: String,
        @Header("Cookie") userCookie: String,
        @Query("apikey") token: String
    ): Response<Mensaje>

    //*******************************
    //*AUTENTICAR
    //*******************************
    @GET("jwt/check")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun checkJWT(
        @Header("Cookie") userCookie: String,
        @Query("apikey") token: String
    ): Response<CheckJWT>

    @POST("jwt/auth")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun loginJWT(
        @Body user: Usuario
    ): Response<Token>

   @POST("jwt/logout")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun logout(): Response<Sesion>

    @POST("jwt/destroysession")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun destroySession(): Response<Sesion>

}
