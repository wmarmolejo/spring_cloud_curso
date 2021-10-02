package com.kalettre.springboot.app.commons.models.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ProductoTest {

    private Producto item;

    @BeforeEach
    void setUp() {
        item = new Producto();
        item.setId(1l);
        item.setNombre("PC Dell");
        item.setPrecio(2400d);
        item.setPort(8090);
    }

    @Test
    void ProductoObjeto() {
        Producto prod=new Producto();
        prod.setId(1L);
        prod.setNombre("PC Dell");
        prod.setPrecio(2400D);
        prod.setPort(500);
    }

    @Test
    @DisplayName("valida nombre")
    void getName() {
        item.setNombre("PC Dell");
        assertEquals(item.getNombre(),"PC Dell");
    }

}
