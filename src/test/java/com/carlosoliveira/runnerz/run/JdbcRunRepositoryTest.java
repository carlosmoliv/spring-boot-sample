package com.carlosoliveira.runnerz.run;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(JdbcRunRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JdbcRunRepositoryTest {
    @Autowired
    private JdbcRunRepository repository;

    @BeforeEach
    void setUp() {
        repository.create(new Run(1,
                "Monday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                3,
                Location.INDOOR,
                1));

        repository.create(new Run(2,
                "Wednesday Evening Run",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(60),
                6,
                Location.INDOOR,
                1));
    }

    @Test
    void find_all_runs() {
        List<Run> runs = repository.findAll();

        assertEquals(2, runs.size());
    }

    @Test
    void find_run_with_valid_id() {
        var run = repository.findById(1).get();

        assertEquals("Monday Morning Run", run.title());
        assertEquals(3, run.miles());
    }

    @Test
    void fails_when_run_has_an_invalid_id() {
        var run = repository.findById(3);

        assertTrue(run.isEmpty());
    }

    @Test
    void create_a_new_run() {
        repository.create(new Run(3,
                "Friday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                3,
                Location.INDOOR,
                1));

        List<Run> runs = repository.findAll();

        assertEquals(3, runs.size());
    }

    @Test
    void update_a_run() {
        repository.update(new Run(1,
                "Monday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                5,
                Location.OUTDOOR,
                1
        ), 1);

        var run = repository.findById(1).get();
        assertEquals("Monday Morning Run", run.title());
        assertEquals(5, run.miles());
        assertEquals(Location.OUTDOOR, run.location());
    }

    @Test
    void delete_a_run() {
        repository.delete(1);

        List<Run> runs = repository.findAll();
        assertEquals(1, runs.size());
    }
}
