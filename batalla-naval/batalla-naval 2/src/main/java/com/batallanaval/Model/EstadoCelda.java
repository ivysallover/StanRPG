package com.batallanaval.Model;

public enum EstadoCelda {
    AGUA,           // Celda con agua, no atacada
    BARCO,          // Celda con barco, no atacada
    AGUA_ATACADA,   // Celda con agua que ha sido atacada
    IMPACTO,        // Celda con barco que ha sido impactada
    HUNDIDO         // Celda de un barco que ha sido hundido completamente
}

// ===== Celda.java =====
// Ubicación: src/main/java/com/batallanaval/model/Celda.java


