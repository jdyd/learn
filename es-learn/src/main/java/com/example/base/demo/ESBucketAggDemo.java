package com.example.base.demo;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedHistogram;
import org.elasticsearch.search.aggregations.bucket.missing.ParsedMissing;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.RareTerms;
import org.elasticsearch.search.aggregations.bucket.terms.RareTermsAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.TopHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;

/**
 * bucket 桶聚合查询，group by count
 */
@Slf4j
public class ESBucketAggDemo extends ESAggDemo {
    /**
     * group by查询，如下
     * sql: select skuId, count(1) as cmt from sku where cityId = 44 group by skuId
     */
    @Test
    public void testBucketAgg() {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String skuIdAggkey = "skuIdAgg";
        //查询条件
        TermQueryBuilder termQuery = QueryBuilders.termQuery("zqDt", "20210109");
        //group by Agg
        AggregationBuilder skuIdAgg = AggregationBuilders.terms(skuIdAggkey).field("skuId").size(Integer.MAX_VALUE);

        //设置一级返回明细tophits
        AggregationBuilder tophits = AggregationBuilders.topHits("skuTop").size(1).sort("zqDt", SortOrder.DESC);
        skuIdAgg.subAggregation(tophits);

        //将条件添加至searchRequest
        searchSourceBuilder.aggregation(skuIdAgg);
        searchSourceBuilder.query(termQuery);
        searchRequest.source(searchSourceBuilder);
        searchSourceBuilder.size(0);
        //执行查询
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("call es 查询信息异常", e);
        }
        //解析结果
        if (response != null && response.status().getStatus() == SUCCESS) {
            Aggregations aggregations = response.getAggregations();
            ParsedStringTerms skuIdAggTerms = aggregations.get(skuIdAggkey);
            //bucket size数
            for (Terms.Bucket bucket : skuIdAggTerms.getBuckets()) {
                //获取topHits
                TopHits topHits = bucket.getAggregations().get("skuTop");
                SearchHits hits = topHits.getHits();
                SearchHit[] hitArray = hits.getHits();
                // 因为top_hits的siez=1所以不进行遍历直接取第一条数据
                SearchHit hit = hitArray[0];
                //获取汇总数据
                log.info("[skuId :%s，count:%s]",bucket.getKey(),bucket.getDocCount());
            }
        }
    }

    /**
     * 查询某个字段缺少的doc 汇总，比如companySkuAgg字段缺失的汇总
     */
    @Test
    public void testMissingAgg() {
        SearchSourceBuilder builder = new SearchSourceBuilder();

        TermQueryBuilder cityIdTermQuery = QueryBuilders.termQuery("cityId", 44);
        AggregationBuilder missSaleVolumeAgg = AggregationBuilders.missing("companySkuAgg").field("companySkuAgg");

        builder.aggregation(missSaleVolumeAgg);
        builder.query(cityIdTermQuery);
        searchRequest.source(builder);

        //执行查询
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("call es 查询信息异常", e);
        }
        //解析结果
        if (response != null && response.status().getStatus() == SUCCESS) {
            Aggregations aggregations = response.getAggregations();
            ParsedMissing parsedMissing = aggregations.get("companySkuAgg");
            long missingCount = parsedMissing.getDocCount();
            log.info("MissSkuCount : %s", missingCount);

        }
    }
    /**
     * 直方图查询，跨度查询，比如查询0~50,50~100,100~150之间的数据汇总,跨度是固定的
     */
    @Test
    public void testHistogramAgg() {
        SearchSourceBuilder builder = new SearchSourceBuilder();

        TermQueryBuilder cityIdTermQuery = QueryBuilders.termQuery("cityId", 44);
        AggregationBuilder histogramAggregationBuilder = AggregationBuilders.histogram("salesAmountHistogram").field("originalSalesVol").interval(10);

        builder.aggregation(histogramAggregationBuilder);
        builder.query(cityIdTermQuery);
        searchRequest.source(builder);

        //执行查询
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("call es 查询信息异常", e);
        }
        //解析结果
        if (response != null && response.status().getStatus() == SUCCESS) {
            Aggregations aggregations = response.getAggregations();
            ParsedHistogram parsedMissing = aggregations.get("salesAmountHistogram");
            // For each entry
            for (Histogram.Bucket entry : parsedMissing.getBuckets()) {
                Double key = (Double) entry.getKey();    // Key
                String keyAsString = entry.getKeyAsString(); // Key as String
                long docCount = entry.getDocCount();         // Doc count
                log.info("key [{}], date [{}], doc_count [{}]", keyAsString, key, docCount);
            }
        }
    }

    /**
     * 稀有查询，查询某些特殊行为的doc，比如max doc count为200
     */
    @Test
    public void testRareTermsAgg() {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //term限制条件
        TermQueryBuilder cityIdTermQuery = QueryBuilders.termQuery("cityId", 44);
        builder.query(cityIdTermQuery);

        RareTermsAggregationBuilder rareTermsAggregationBuilder = new RareTermsAggregationBuilder("rareSkuAgg").field("skuId");
        rareTermsAggregationBuilder.maxDocCount(20);
        builder.aggregation(rareTermsAggregationBuilder);

        searchRequest.source(builder);

        //执行查询
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("call eres 查询信息异常", e);
        }
        //解析结果
        if (response != null && response.status().getStatus() == SUCCESS) {
            Aggregations aggregations = response.getAggregations();
            RareTerms rareTerms = aggregations.get("rareSkuAgg");
            // For each entry
            for (RareTerms.Bucket entry : rareTerms.getBuckets()) {
                Double key = (Double) entry.getKey();    // Key
                String keyAsString = entry.getKeyAsString(); // Key as String
                long docCount = entry.getDocCount();         // Doc count
                log.info("key [{}], date [{}], doc_count [{}]", keyAsString, key, docCount);
            }
        }
    }

    /**
     * 范围查询，查询聚合结果在某个范围内的值,可以定义范围区间，比如10~50,50~79,80~100，跨度是自定义的
     */
    @Test
    public void testRangeTermsAgg() {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //term限制条件
        MatchAllQueryBuilder cityIdTermQuery = QueryBuilders.matchAllQuery();
        builder.query(cityIdTermQuery);

        AggregationBuilder rangeAgg = AggregationBuilders.range("saleVolumeRangeAgg").field("calcSalesVolume").addUnboundedTo(50.0).addRange(50, 100).addRange(100, 200);
        builder.aggregation(rangeAgg);

        searchRequest.source(builder);

        //执行查询
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("call eres 查询信息异常", e);
        }
        //解析结果
        if (response != null && response.status().getStatus() == SUCCESS) {
            Aggregations aggregations = response.getAggregations();
            ParsedRange rangeTerms = aggregations.get("saleVolumeRangeAgg");
            // For each entry
            for (Range.Bucket entry : rangeTerms.getBuckets()) {
                String key =  entry.getKey().toString();    // Key
                String keyAsString = entry.getKeyAsString(); // Key as String
                long docCount = entry.getDocCount();         // Doc count
                log.info("key [{}], date [{}], doc_count [{}]", keyAsString, key, docCount);
            }
        }
    }

}
