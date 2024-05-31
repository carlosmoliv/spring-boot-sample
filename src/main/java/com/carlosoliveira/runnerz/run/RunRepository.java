package com.carlosoliveira.runnerz.run;

import java.util.List;
import java.util.Optional;

public interface RunRepository {
    List<Run> findAll();

    void create(Run run);

    void update(Run run, Integer id);

    void delete(Integer id);

    Optional<Run> findById(Integer id);
}
