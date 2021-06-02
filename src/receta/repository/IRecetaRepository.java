package receta.repository;

import nota.model.INota;

import java.util.ArrayList;

public interface IRecetaRepository {

    boolean exists(String nombre);
    boolean create(INota nota);
    INota read(String nombre);
    void remove(String nombre);
    ArrayList<INota> readAll();
    void saveAll();
    //INota search(String nombre);





}