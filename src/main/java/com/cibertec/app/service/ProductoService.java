package com.cibertec.app.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cibertec.app.dto.ProductoDTO;
import com.cibertec.app.model.Producto;
import com.cibertec.app.repository.ProductoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Listar todos los productos como DTOs
    public List<ProductoDTO> listarTodos() {
        return productoRepository.findAll()
                .stream()
                .map(this::convertirAProductoDTO)
                .collect(Collectors.toList());
    }

    // Guardar un producto desde un DTO
    public ProductoDTO guardar(ProductoDTO productoDTO) {
        Producto producto = convertirADesdeProductoDTO(productoDTO);
        Producto productoGuardado = productoRepository.save(producto);
        return convertirAProductoDTO(productoGuardado);
    }

    // Método que retorna todos los productos
    public List<Producto> getAllProducts() {
        return productoRepository.findAll();
    }



    // Eliminar un producto por ID
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    // Obtener un producto por ID como DTO
    public ProductoDTO obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            return null;
        }
        return convertirAProductoDTO(producto);
    }

    // Métodos auxiliares para la conversión entre Producto y ProductoDTO

    private ProductoDTO convertirAProductoDTO(Producto producto) {
        ProductoDTO productoDTO = modelMapper.map(producto, ProductoDTO.class);
        productoDTO.setCategoriaId(producto.getCategoria().getId());
        return productoDTO;
    }

    private Producto convertirADesdeProductoDTO(ProductoDTO productoDTO) {
        Producto producto = modelMapper.map(productoDTO, Producto.class);
        // Debes cargar la entidad Categoria desde la base de datos si necesitas establecerla aquí
        // Ejemplo:
        // Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId()).orElseThrow(...);
        // producto.setCategoria(categoria);
        return producto;
    }
}
