package com.example.pizarrainteractivacourtcoach;

public class Jugador {
    String nombre;
    int puntosTotales, tirosLibresMetidos, tirosLibresFallados, tirosDosMetidos, tirosDosFallados, triplesMetidos, triplesFallados, rebotesDefensivos, rebotesOfensivos, asistencias,
        robos, tapones, perdidas, faltasRecibidas, faltasCometidas;
    float porcentajeTirosLibres, porcentajeTirosDos, porcentajeTriples;

    public Jugador(String nombre, int puntosTotales, int tirosLibresMetidos, int tirosLibresFallados, int tirosDosMetidos, int tirosDosFallados, int triplesMetidos,
                   int triplesFallados, int rebotesDefensivos, int rebotesOfensivos, int asistencias, int robos, int tapones, int perdidas, int faltasRecibidas,
                   int faltasCometidas, float porcentajeTirosLibres, float porcentajeTirosDos, float porcentajeTriples) {
        this.nombre = nombre;
        this.puntosTotales = puntosTotales;
        this.tirosLibresMetidos = tirosLibresMetidos;
        this.tirosLibresFallados = tirosLibresFallados;
        this.tirosDosMetidos = tirosDosMetidos;
        this.tirosDosFallados = tirosDosFallados;
        this.triplesMetidos = triplesMetidos;
        this.triplesFallados = triplesFallados;
        this.rebotesDefensivos = rebotesDefensivos;
        this.rebotesOfensivos = rebotesOfensivos;
        this.asistencias = asistencias;
        this.robos = robos;
        this.tapones = tapones;
        this.perdidas = perdidas;
        this.faltasRecibidas = faltasRecibidas;
        this.faltasCometidas = faltasCometidas;
        this.porcentajeTirosLibres = porcentajeTirosLibres;
        this.porcentajeTirosDos = porcentajeTirosDos;
        this.porcentajeTriples = porcentajeTriples;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntosTotales() {
        return puntosTotales;
    }

    public void setPuntosTotales(int puntosTotales) {
        this.puntosTotales = puntosTotales;
    }

    public int getTirosLibresMetidos() {
        return tirosLibresMetidos;
    }

    public void setTirosLibresMetidos(int tirosLibresMetidos) {
        this.tirosLibresMetidos = tirosLibresMetidos;
    }

    public int getTirosLibresFallados() {
        return tirosLibresFallados;
    }

    public void setTirosLibresFallados(int tirosLibresFallados) {
        this.tirosLibresFallados = tirosLibresFallados;
    }

    public int getTirosDosMetidos() {
        return tirosDosMetidos;
    }

    public void setTirosDosMetidos(int tirosDosMetidos) {
        this.tirosDosMetidos = tirosDosMetidos;
    }

    public int getTirosDosFallados() {
        return tirosDosFallados;
    }

    public void setTirosDosFallados(int tirosDosFallados) {
        this.tirosDosFallados = tirosDosFallados;
    }

    public int getTriplesMetidos() {
        return triplesMetidos;
    }

    public void setTriplesMetidos(int triplesMetidos) {
        this.triplesMetidos = triplesMetidos;
    }

    public int getTriplesFallados() {
        return triplesFallados;
    }

    public void setTriplesFallados(int triplesFallados) {
        this.triplesFallados = triplesFallados;
    }

    public int getRebotesDefensivos() {
        return rebotesDefensivos;
    }

    public void setRebotesDefensivos(int rebotesDefensivos) {
        this.rebotesDefensivos = rebotesDefensivos;
    }

    public int getRebotesOfensivos() {
        return rebotesOfensivos;
    }

    public void setRebotesOfensivos(int rebotesOfensivos) {
        this.rebotesOfensivos = rebotesOfensivos;
    }

    public int getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(int asistencias) {
        this.asistencias = asistencias;
    }

    public int getRobos() {
        return robos;
    }

    public void setRobos(int robos) {
        this.robos = robos;
    }

    public int getTapones() {
        return tapones;
    }

    public void setTapones(int tapones) {
        this.tapones = tapones;
    }

    public int getPerdidas() {
        return perdidas;
    }

    public void setPerdidas(int perdidas) {
        this.perdidas = perdidas;
    }

    public int getFaltasRecibidas() {
        return faltasRecibidas;
    }

    public void setFaltasRecibidas(int faltasRecibidas) {
        this.faltasRecibidas = faltasRecibidas;
    }

    public int getFaltasCometidas() {
        return faltasCometidas;
    }

    public void setFaltasCometidas(int faltasCometidas) {
        this.faltasCometidas = faltasCometidas;
    }

    public float getPorcentajeTirosLibres() {
        return porcentajeTirosLibres;
    }

    public void setPorcentajeTirosLibres(float porcentajeTirosLibres) {
        this.porcentajeTirosLibres = porcentajeTirosLibres;
    }

    public float getPorcentajeTirosDos() {
        return porcentajeTirosDos;
    }

    public void setPorcentajeTirosDos(float porcentajeTirosDos) {
        this.porcentajeTirosDos = porcentajeTirosDos;
    }

    public float getPorcentajeTriples() {
        return porcentajeTriples;
    }

    public void setPorcentajeTriples(float porcentajeTriples) {
        this.porcentajeTriples = porcentajeTriples;
    }
}
