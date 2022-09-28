package com.superhero.dao;

import java.util.List;

import com.superhero.model.Power;

public interface PowerDao {
    Power getPowerById(int id);
    List<Power> getAllPowers();
    Power addPower(Power power);
    void updatePower(Power power);
    void deletePowerById(int id);
}
