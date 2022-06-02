package com.binarycod.arigato.repository;

import com.binarycod.arigato.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;
    public int count(){
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM product;", Integer.class);

    }

    private RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setName(rs.getString("p_name"));
        p.setPrice(rs.getDouble("price"));
        p.setSize(rs.getInt("size"));

        return p;//deserialization
    };
public List<Product> getListOfProduct(){
    List<Product> productList = jdbcTemplate.query("SELECT * FROM product;", productRowMapper);
return productList;
}

    public void insertProduct(Product product) {
    String query = "INSERT INTO product(id, p_name, price, size) VALUES (?, ?, ?, ?);";
    jdbcTemplate.update(query, product.getId(), product.getName(), product.getPrice(), product.getSize());
    }

    public Product getProductByID(Long id) {
    String sql = "SELECT * FROM product WHERE id=?";

    Product product = null;
    try{
        product = jdbcTemplate.queryForObject(sql, productRowMapper, id);
    }catch (DataAccessException dae){
        product = null;
    }
    return product;
    }


    public void deleteProduct(Long id) {
    String sql = "DELETE FROM product where id=?";
        jdbcTemplate.update(sql, id);
    }
}
