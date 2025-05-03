package br.com.fjsistemas.gamerating.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class GenericController<T, ID, R extends JpaRepository<T, ID>> {
    R repository;

    public GenericController(R repository){
        this.repository = repository;
    }
    public T load(ID id){
        return repository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Entidade não encontrada"));
    }

    public T loadTwo(ID id){
        return repository.findById(id).orElse(null);
    }

    public T save(T entity){
        return repository.save(entity);
    }

    public List<T> saveBatch(List<T> entity){

        return repository.saveAll(entity);
    }

    public T update(T entity){
        return repository.save(entity);
    }

    public void delete(T entity){
        repository.delete(entity);
    }

    public void deleteById(ID id){
        repository.deleteById(id);
    }

    public List<T> list(){
        return repository.findAll();
    }

}
