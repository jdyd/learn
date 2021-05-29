package com.example.base.demo;

import com.google.common.collect.Lists;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.junit.Before;

public abstract class ESAggDemo {
    protected final static String CLUSTER_NAME = "eaglenode_es-smartinquiry";
    protected final static String CLIENT_APPKEY = "com.sankuai.dppt.search.eagleweb";
    protected final static String ACCESS_KEY = "305FE5914D67DA3143587331F4B581AE";

    //目标测试索引SKU
    private final static String INDEX_NAME = "sku";
    protected static final int SUCCESS = 200;
    protected RestHighLevelClient restHighLevelClient;

    //子类继承使用的请求
    protected SearchRequest searchRequest = null;
    protected TermQueryBuilder cityIdTermQuery = null;
    protected TermsQueryBuilder cityIdTermsQuery = null;
    String volumeFiledName = "sellPrice";
    @Before
    public void afterPropertiesSet() {
        //初始化 client
        restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        //初始化searchRequest
        searchRequest = new SearchRequest(INDEX_NAME);
        //term 查询条件
        cityIdTermQuery = QueryBuilders.termQuery("cityId", 88);
        cityIdTermsQuery = QueryBuilders.termsQuery("cityId", Lists.newArrayList(88,214));
    }
}
