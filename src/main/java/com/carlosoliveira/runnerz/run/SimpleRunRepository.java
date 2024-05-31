package com.carlosoliveira.runnerz.run;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SimpleRunRepository {
    private final List<Run> runs = new ArrayList<>();

    public List<Run> findAll() {
        return runs;
    }

    public void create(Run run) {
        runs.add(run);
    }

    public void update(Run run, Integer id) {
        Optional<Run> existingRun = findById(id);
        if (existingRun.isPresent()) {
            runs.set(runs.indexOf(existingRun.get()), run);
        }
    }

    public void delete(Integer id) {
        runs.removeIf(run -> run.id().equals(id));
    }

    public Optional<Run> findById(Integer id) {
        return runs.stream()
                .filter(run -> run.id().equals(id))
                .findFirst();
    }

    @PostConstruct
    public void init() {
        runs.add(new Run(1, "Monday morning run", LocalDateTime.now(), LocalDateTime.now().plusMinutes(30), 3, Location.INDOOR));
        runs.add(new Run(2, "Wednesday evening run", LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), 6, Location.INDOOR));
    }
}
