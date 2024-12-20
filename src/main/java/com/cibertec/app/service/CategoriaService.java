package com.cibertec.app.service;



import com.cibertec.app.dto.CategoriaDTO;
import com.cibertec.app.model.Categoria;
import com.cibertec.app.repository.CategoriaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Lista todas las categorías y las convierte a CategoriaDTO
     *
     * @return Lista de CategoriaDTO
     */
    public List<CategoriaDTO> listarTodos() {
        return categoriaRepository.findAll().stream()
                .map(categoria -> modelMapper.map(categoria, CategoriaDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Guarda una categoría en la base de datos
     *
     * @param categoriaDTO Objeto CategoriaDTO a guardar
     */
    public void guardar(CategoriaDTO categoriaDTO) {
        Categoria categoria = modelMapper.map(categoriaDTO, Categoria.class);
        categoriaRepository.save(categoria);
    }

    /**
     * Obtiene una categoría por su ID y la convierte a CategoriaDTO
     *
     * @param id ID de la categoría
     * @return Objeto CategoriaDTO correspondiente
     */
    public CategoriaDTO obtenerPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));
        return modelMapper.map(categoria, CategoriaDTO.class);
    }

    /**
     * Elimina una categoría por su ID
     *
     * @param id ID de la categoría a eliminar
     */
    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }
}
