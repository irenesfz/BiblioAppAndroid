package com.proyecto.biblioapp.auth;

class Usuario() {
    var username: String? = null
    var password: String? = null

    constructor(
        username: String, password: String
    ) : this() {
        this.username = username
        this.password = password
    }

}
