package org.threefour.ddip.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.threefour.ddip.datasource.DatabaseType.MASTER;
import static org.threefour.ddip.datasource.DatabaseType.SLAVE;

public class CustomRoutingDataSource extends AbstractRoutingDataSource {
    private static final Logger log = LoggerFactory.getLogger(CustomRoutingDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        DatabaseType databaseType = MASTER;
        if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            databaseType = SLAVE;
        }
        log.info("Routing to {} database", databaseType);

        return databaseType;
    }
}
