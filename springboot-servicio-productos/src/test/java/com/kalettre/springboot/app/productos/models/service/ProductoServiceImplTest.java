package com.kalettre.springboot.app.productos.models.service;

import com.kalettre.springboot.app.commons.models.entity.Producto;
import com.kalettre.springboot.app.productos.models.dao.ProductoDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductoServiceImplTest {


    Producto producto;
    @Autowired
    ProductoDao productoDao;

    @BeforeEach
    void setUp() {
        producto=new Producto();
        producto.setId(2l);
        producto.setNombre("sony");
        producto.setPrecio(700d);
        producto.setPort(null);
    }

    @Test
    void findAll() {
        List<Producto> products= (List<Producto>) productoDao.findAll();
        assertThat(products).size().isGreaterThan(0);
    }

    @Test
    @Rollback(false)
    public void testCreateProduct() {
        Producto savedProduct = productoDao.save(producto);
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    @Rollback(false)
    public void testUpdateProduct() {
        Producto product=  productoDao.findById(2L).get();
        product.setPrecio(1000d);
        productoDao.save(product);
        Producto updatedProduct = productoDao.findById(2L).get();
        assertThat(updatedProduct.getPrecio()).isEqualTo(1000d);
    }



}