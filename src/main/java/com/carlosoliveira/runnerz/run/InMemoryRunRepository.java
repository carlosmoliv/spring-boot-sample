package com.carlosoliveira.runnerz.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryRunRepository implements RunRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryRunRepository.class);
    private final JdbcClient jdbcClient;

    public InMemoryRunRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Run> findAll() {
        return jdbcClient.sql("select * from run").query(Run.class).list();
    }

    @Override
    public Optional<Run> findById(Integer id) {
        return jdbcClient.sql("SELECT id,title,completed_on,started_on,miles,location FROM Run WHERE id = :id")
                .param("id", id)
                .query(Run.class)
                .optional();
    }

    @Override
    public void create(Run run) {
        var created = jdbcClient.sql("INSERT INTO Run(id,title,started_on, completed_on,miles,location) values (?,?,?,?,?,?)")
                .params(List.of(run.id(), run.title(), run.startedOn(), run.completedOn(), run.miles(), run.location().toString()))
                .update();
        Assert.state(created == 1, "Failed to updated Run" + run.title());
    }

    @Override
    public void update(Run run, Integer id) {
        var updated = jdbcClient.sql("UPDATE run SET title = ?, started_on = ?, completed_on = ?, miles = ?, location = ? where id = ?")
                .params(List.of(run.title(), run.startedOn(), run.completedOn(), run.miles(), run.location().toString(), id))
                .update();
        Assert.state(updated == 1, "Failed to updated Run" + run.title());
    }

    @Override
    public void delete(Integer id) {
        var updated = jdbcClient.sql("delete from run where id = :id")
                .param("id", id)
                .update();
    }

    @Override
    public int count() {
        return jdbcClient.sql("select * from run").query().listOfRows().size();
    }

    @Override
    public void saveAll(List<Run> runs) {
        runs.stream().forEach(this::create);
    }
}
