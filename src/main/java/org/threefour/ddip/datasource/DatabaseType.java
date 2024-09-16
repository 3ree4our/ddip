package org.threefour.ddip.datasource;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DatabaseType {
    MASTER("메인 데이터베이스", "AWS RDS"),
    SLAVE("서브 데이터베이스", "Docker Container");

    private final String description;
    private final String location;
}
