package com.example.base.demo;

import com.google.common.collect.Maps;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedAvg;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

/**
 * 压测服务
 */
public class EaglePressTestServiceImpl {

    private final RestHighLevelClient restHighLevelClient;

    private final static String clusterName = "eaglenode_es-qms";
    //    private final static String clusterName = "eaglenode_sankuai-taghub";
    private final static String bizAppkey = "com.sankuai.eaglenode.qms";
    //    private final static String bizAppkey = "com.sankuai.epbi.sankuai.taghub";
    private final static String appkeyAcccess = "B3A55FFF7CF742757F24728052D7E22B";
    //    private final static String appkeyAcccess= "99EEBA3CB5C5E45ACF200FAEF26B4CC7";
    //设置查询索引
    final String index = "test_medicine";
    private final Random random = new Random();

    private final static Logger logger = LoggerFactory.getLogger(EaglePressTestServiceImpl.class);

    public EaglePressTestServiceImpl() throws Exception {

        restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
    }

    public void pressTest(String action) {
        long startTime = System.currentTimeMillis();
        try {
            if ("simpleIndex".equals(action)) {
                simpleIndex();
            } else if ("bulkIndex".equals(action)) {
                bulkIndex();
            } else if ("termQuery".equals(action)) {
                termQuery();
            } else if ("termQueryWithDocSort".equals(action)) {
                termQueryWithDocSort();
            } else if ("matchQuery".equals(action)) {
                matchSimpleQuery();
            } else if ("pageQuery".equals(action)) {
                pageQuery();
            } else if ("scrollQuery".equals(action)) {
                scrollQuery();
            } else if ("bucketAggQuery".equals(action)) {
                bucketAggQuery();
            } else if ("metricAggQuery".equals(action)) {
                metricAggQuery();
            } else if ("collapseQueryDuplicate".equals(action)) {
                collapseQueryDuplicate();
            } else if ("collapseQueryInnerLimit".equals(action)) {
                collapseQueryInnerLimit();
            } else if ("compoundQuery".equals(action)) {
                compoundBooleanQuery();
            } else if ("joinQuery".equals(action)) {
                joinQuery();
            } else if ("geoBoundingQuery".equals(action)) {
                geoBoundingQuery();
            } else if ("geoDistanceQuery".equals(action)) {
                geoDistanceQuery();
            } else if ("nestedQuery".equals(action)) {
                nestedQuery();
            } else if ("matchPhase".equals(action)) {

            }
        } catch (Exception e) {
            logger.error("Falied Test :{},Exceptions:{}", action, e.getMessage());
        }
        long endTime = System.currentTimeMillis();
    }

    /**
     * 简单写入
     */
    private void simpleIndex() throws IOException {
        IndexRequest indexRequest = buildIndexRequest();
        restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
//        restHighLevelClient.index(indexRequest);
    }

    /**
     * 批量写入
     *
     * @throws IOException
     */
    private void bulkIndex() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (int i = 0; i < 100; i++) {
            IndexRequest indexRequest = buildIndexRequest();
            bulkRequest.add(indexRequest);
        }
        restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    /**
     * terms数组查询
     *
     * @throws IOException
     */
    private void termQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        //设置查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder);
        for (int i = 0; i < 100; i++) {
            try {
                SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
                SearchHits searchHits = searchResponse.getHits();
                if (searchHits.getHits().length > 0) {
                    logger.info("Congra !! Search Successfully!!");
                }
            } catch (Exception e) {
                logger.info("Congra !! Search Failed!!");
            }

        }
    }

    /**
     * terms简单查询 指定doc排序
     *
     * @throws IOException
     */
    private void termQueryWithDocSort() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);

        //设置查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        int request_random = random.nextInt(100000);
        String request_url = "com.dianping.sankuai.shoping" + request_random;
        searchSourceBuilder.query(QueryBuilders.termQuery("url_request", request_url));
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);
        searchSourceBuilder.sort("_doc");
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        if (searchHits.getHits().length > 0) {
            logger.info("Congra !! Search Successfully!!");
        }
    }

    /**
     * match简单查询
     *
     * @throws IOException
     */
    private void matchSimpleQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);

        //设置查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("Origin", "Gimpo"));
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        if (searchHits.getHits().length > 0) {
            logger.info("Congra !! Search Successfully!!");
        }
    }

    /**
     * match简单查询
     *
     * @throws IOException
     */
    private void matchPhaseQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);

        //设置查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchPhraseQuery("Origin", "Gimpo"));
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        if (searchHits.getHits().length > 0) {
            logger.info("Congra !! Search Successfully!!");
        }
    }

    /**
     * 嵌套查询
     */
    private void joinQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("kibana_sample_data_logs");
        //设置查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("geo.dest", "PL"));
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        if (searchHits.getHits().length > 0) {
            logger.info("Congra !! Search Successfully!!");
        }
    }

    /**
     * 地理位置查询，获取在指定矩形框内的数据
     */
    private void geoBoundingQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        //设置查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);

        GeoPoint point1 = new GeoPoint(40.73, -74.1);
        GeoPoint point2 = new GeoPoint(40.01, -71.12);
        GeoBoundingBoxQueryBuilder srb = QueryBuilders.geoBoundingBoxQuery("OriginLocation").setCorners(point1, point2);
        searchSourceBuilder.query(srb);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        if (searchHits.getHits().length > 0) {
            logger.info("Congra !! Search Successfully!!");
        }
    }

    /**
     * 地理位置查询，获取在指定点方圆多少公里的数据
     */
    private void geoDistanceQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        //设置查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);

        GeoPoint point1 = new GeoPoint(40.73, -74.1);
        GeoDistanceQueryBuilder srb = QueryBuilders.
                geoDistanceQuery("OriginLocation").
                point(point1).distance(10000, DistanceUnit.KILOMETERS);
        searchSourceBuilder.query(srb);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        if (searchHits.getHits().length > 0) {
            logger.info("Congra !! Search Successfully!!");
        }
    }

    /**
     * match简单查询
     *
     * @throws IOException
     */
    private void matchMultiQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);

        //设置查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(
                QueryBuilders.matchQuery("Origin", "Gimpo,Airport")
                        .minimumShouldMatch("75%")
        );
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        if (searchHits.getHits().length > 0) {
            logger.info("Congra !! Search Successfully!!");
        }
    }

    /**
     * 分页查询,暂时不压，意义不大
     */
    @Deprecated
    private void pageQuery() throws IOException {
        //设置查询条件
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("url_host", "friendvisitfeedlist"));
        searchSourceBuilder.from(random.nextInt(1000));
        searchSourceBuilder.size(10);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        if (searchHits.getHits().length > 0) {
            logger.info("Congra !! Search Successfully!!,result size: " + searchHits.getHits().length);
        }
    }

    /**
     * scroll查询
     *
     * @throws IOException
     */
    private void scrollQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        int request_random = random.nextInt(100000);
        ;
        String request_url = "com.dianping.sankuai.shoping" + request_random;
        searchSourceBuilder.query(QueryBuilders.termQuery("url_request", request_url)).size(50);
        searchRequest.source(searchSourceBuilder);
        searchRequest.scroll("1m");

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        int count = 0;
        while (searchHits.getHits().length > 0) {
            searchResponse = restHighLevelClient.searchScroll(new SearchScrollRequest(searchResponse.getScrollId()).scroll("1m"), RequestOptions.DEFAULT);
            searchHits = searchResponse.getHits();
            count += searchHits.getHits().length;
        }
        logger.info("Congra !! Search Successfully!!,result size: " + count);
    }

    /**
     * Buckt 按照航班日聚合查询，求每周每天的航班总数
     */
    private void bucketAggQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder termQuery = QueryBuilders.termQuery("DestCityName", "New York");
        AggregationBuilder aggregationBuilder = AggregationBuilders.
                terms("weekDayCount").
                field("dayOfWeek");
        searchSourceBuilder.aggregation(aggregationBuilder);

        searchSourceBuilder.query(termQuery);
        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        if (aggregations != null) {
            ParsedLongTerms longTerms = aggregations.get("weekDayCount");
            for (Terms.Bucket bucket : longTerms.getBuckets()) {
                logger.info("Day:" + bucket.getKey() + "-->" + bucket.getDocCount());
            }
        }
    }

    /**
     * nested 嵌套查询demo
     */
    private void nestedQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder termQuery = QueryBuilders.rangeQuery("info.size").gt(0).lt(2300496263L);
        QueryBuilder existsQuery = QueryBuilders.existsQuery("info.size");
        QueryBuilder boolQueryBuilder = new BoolQueryBuilder()
                .must(termQuery);
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        int count = 0;
        while (searchHits.getHits().length > 0) {
            searchResponse = restHighLevelClient.searchScroll(new SearchScrollRequest(searchResponse.getScrollId()).scroll("1m"), RequestOptions.DEFAULT);
            searchHits = searchResponse.getHits();
            count += searchHits.getHits().length;
        }
    }

    /**
     * Metric 聚合查询，查询hostHash的平均值
     */
    private void metricAggQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        AggregationBuilder aggregationBuilder = AggregationBuilders.avg("avg_distance").
                field("DistanceKilometers");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(aggregationBuilder);
        searchSourceBuilder.query(QueryBuilders.termQuery("OriginCityName", "Chengdu"));
        searchSourceBuilder.size(1);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        if (aggregations != null) {
            ParsedAvg aggregation = aggregations.get("avg_distance");
            logger.info("Avg Distance: " + aggregation.value());
        }
    }

    /**
     * collapse 查询去重
     */
    private void collapseQueryDuplicate() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        QueryBuilder termQuery = QueryBuilders.termQuery("DestCityName", "New York");

        CollapseBuilder collapseBuilder = new CollapseBuilder("OriginAirportID");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.collapse(collapseBuilder);
        searchSourceBuilder.query(termQuery);
        searchSourceBuilder.size(10);
        searchSourceBuilder.from(1);

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        if (searchHits.getHits().length > 0) {
            logger.info("Congra !! Search Successfully!!,result size: " + searchHits.getHits().length);
        }
    }

    /**
     * collapse 查询去重
     */
    private void collapseQueryInnerLimit() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        int request_random = random.nextInt(100000);
        String request_url = "com.dianping.sankuai.shoping" + request_random;
        QueryBuilder termQuery = QueryBuilders.termQuery("url_request", request_url);

        CollapseBuilder collapseBuilder = new CollapseBuilder("url_pv");
        InnerHitBuilder innerHitBuilder = new InnerHitBuilder("collapse_inner");
        innerHitBuilder.setSize(10);
        collapseBuilder.setInnerHits(innerHitBuilder);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.collapse(collapseBuilder);
        searchSourceBuilder.query(termQuery);
        searchSourceBuilder.size(10);
        searchSourceBuilder.from(random.nextInt(100));

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        if (searchHits.getHits().length > 0) {
            logger.info("Congra !! Search Successfully!!,result size: " + searchHits.getHits().length);
        }
    }

    /**
     * 复合查询
     */
    private void compoundBooleanQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(10);
        searchSourceBuilder.from(0);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().
                must(QueryBuilders.matchQuery("OriginCityName", "Seoul")).
                must(QueryBuilders.matchQuery("DestCountry", "JP")).
                should(QueryBuilders.termsQuery("dayOfWeek", "1,2")).
                should(QueryBuilders.termQuery("dayOfWeek", "2"));

        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        if (searchHits.getHits().length > 0) {
            logger.info("Congra !! Search Successfully!!,result size: " + searchHits.getHits().length);
        }
    }

    private IndexRequest buildIndexRequest() {
        IndexRequest indexRequest = new IndexRequest(index, "_doc");
        Map<String, String> source = Maps.newConcurrentMap();
        int url_random = random.nextInt(200);
        int request_random = random.nextInt(100000);

        String url_host = "com.dianping.sankuai" + url_random;
        String host_hash = String.valueOf(url_host.hashCode());
        String request_url = "com.dianping.sankuai.shoping" + request_random;
        source.put("url_host", url_host);
        source.put("schedule_url", url_host);
        source.put("host_hash", host_hash);
        source.put("priority", "0");
        source.put("schedule_status", "1");
        source.put("url_request", request_url);
        source.put("schedule_task", "task00020");
        indexRequest.source(source);
        return indexRequest;
    }
}
