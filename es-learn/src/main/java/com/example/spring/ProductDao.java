package com.example.spring;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: cuijian03
 * @description:
 * @create: 2021-04-18 12:48
 */
@Repository
public interface ProductDao extends ElasticsearchRepository<Product, Long> {
}