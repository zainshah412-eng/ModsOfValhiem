package com.appcake.modsforvalheim.utlis

class NoConnectivityException : java.io.IOException() {
    // You can send any message whatever you want from here.
    override val message: String
        get() = "No Internet Connection"
    // You can send any message whatever you want from here.
}