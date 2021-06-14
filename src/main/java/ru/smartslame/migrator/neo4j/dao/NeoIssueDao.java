package ru.smartslame.migrator.neo4j.dao;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smartslame.migrator.neo4j.entity.NeoIssue;
import ru.smartslame.migrator.neo4j.query.QueryUtil;
import ru.smartslame.migrator.service.NeoConnectionService;

import java.util.List;

@Component
public class NeoIssueDao {
    private final Logger logger = Logger.getLogger(NeoIssueDao.class);
    private final NeoConnectionService neoConnectionService;

    @Autowired
    public NeoIssueDao(NeoConnectionService neoConnectionService) {
        this.neoConnectionService = neoConnectionService;
    }

    public void update(NeoIssue issue) {
        logger.debug("Update Issue: " + issue);
        try (Session session = neoConnectionService.getSession()) {
            List<String> queries = QueryUtil.getCompleteQuery(issue);
            for (int i = 0; i < queries.size(); ++i) {
                int finalI = i;
                session.writeTransaction(tx -> tx.run(queries.get(finalI)));
            }
        }
    }
}
