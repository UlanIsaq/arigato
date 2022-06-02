package com.binarycod.arigato.repository;

import com.binarycod.arigato.domain.Address;
import com.binarycod.arigato.domain.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.Arrays;

@Repository
public class StoreRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void createStore(Store store, Address address) {
        String sql = "INSERT INTO store (id, name, address_id) VALUES(?, ?, ?)";
        Integer addressID = createAddress(address);
        jdbcTemplate.update(sql, store.getId(), store.getName(), addressID);

    }

    public Integer createAddress(Address address) {
        String sql = "INSERT INTO address (address1, address2, city, state, country, zipCode)" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatementCreator psc = new PreparedStatementCreatorFactory(sql,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.CHAR,
                Types.CHAR,
                Types.VARCHAR).newPreparedStatementCreator(Arrays.asList(
                        address.getAddress1(),
                        address.getAddress2(),
                        address.getCity(),
                        address.getState(),
                        address.getCountry(),
                        address.getZipCode()

        ));
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(psc, keyHolder);

        return keyHolder.getKey().intValue();
    }
}
