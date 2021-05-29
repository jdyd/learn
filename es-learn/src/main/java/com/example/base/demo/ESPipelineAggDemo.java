package com.example.base.demo;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.PipelineAggregatorBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.pipeline.BucketSelectorPipelineAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.BucketSortPipelineAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.CumulativeSumPipelineAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管道Agg查询，支持脚本，排序
 */
@Slf4j
public class ESPipelineAggDemo extends ESAggDemo{
    /**
     * 测试在聚合中使用脚本
     * sql: select skuId,sum(salevolume) as sum from sku group by skuId having sum > 100
     */
    @Test
    public void testPipelineBucketSelector() {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        AggregationBuilder skuIdAgg = AggregationBuilders.terms("skuIdAgg").field("skuId");
        AggregationBuilder saleVolumeSum = AggregationBuilders.sum("saleVolumeSum").field("calcSalesVolume");
        //<!---PipelineAggregator---配置>
        //having中要使用的聚合字段，名字可以一样
        Map<String, String> bucketsPathsMap = new HashMap<>(8);
        bucketsPathsMap.put("saleVolumeSum", "saleVolumeSum");
        //设置having语句
        StringBuilder havingParams = new StringBuilder();
        havingParams.append("params.saleVolumeSum > 100");
        Script havingScript = new Script(havingParams.toString());
        //构建having bucket选择器
        BucketSelectorPipelineAggregationBuilder havingBs = PipelineAggregatorBuilders.bucketSelector("having", bucketsPathsMap, havingScript);
        //having条件添加至skuId外层聚
        skuIdAgg.subAggregation(havingBs);
        //<!---End of Having--->
        skuIdAgg.subAggregation(saleVolumeSum);
        sourceBuilder.aggregation(skuIdAgg);
        sourceBuilder.query(cityIdTermQuery);
        searchRequest.source(sourceBuilder);

        //结果解析
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("call es 查询信息异常", e);
        }
        //解析结果
        if (response != null && response.status().getStatus() == SUCCESS) {
            Aggregations aggregations = response.getAggregations();
            ParsedStringTerms parsedStringTerms = aggregations.get("skuIdAgg");
            for (Terms.Bucket bucket : parsedStringTerms.getBuckets()) {
                ParsedSum volumeSum = bucket.getAggregations().get("saleVolumeSum"); // Key as String
                double sumValue = volumeSum.getValue();         // Doc count
                log.info("key [{}], date [{}], doc_count [{}]", bucket.getKey(), sumValue);
            }
            log.info("Success ~~");
        }
    }

    /**
     * 桶聚合之后进行排序
     * sql: select skuId,sum(salevolume) as sum from sku group by skuId order by sum desc limit 0,5
     */
    @Test
    public void testPipelineBucketSort() {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        AggregationBuilder cityIdAgg = AggregationBuilders.terms("cityIdAgg").field("cityId");
        AggregationBuilder skuIdAgg = AggregationBuilders.terms("skuIdAgg").field("skuId");
        String saleVolumeSumName = "saleVolumeSum";
        AggregationBuilder saleVolumeSum = AggregationBuilders.sum(saleVolumeSumName).field("calcSalesVolume");

        //<!--- Bucket Sort --->
        List<FieldSortBuilder> fieldSortBuilders = Lists.newArrayList();
        fieldSortBuilders.add(new FieldSortBuilder(saleVolumeSumName).order(SortOrder.DESC));
        BucketSortPipelineAggregationBuilder bucketSortPipelineAggregationBuilder = new BucketSortPipelineAggregationBuilder("volumeSumSort", fieldSortBuilders).from(0).size(5);

        skuIdAgg.subAggregation(saleVolumeSum);
        skuIdAgg.subAggregation(bucketSortPipelineAggregationBuilder);

        cityIdAgg.subAggregation(skuIdAgg);
        sourceBuilder.aggregation(cityIdAgg);
        sourceBuilder.query(cityIdTermsQuery);
        searchRequest.source(sourceBuilder);

        //结果解析
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("call es 查询信息异常", e);
        }
        //解析结果
        if (response != null && response.status().getStatus() == SUCCESS) {
            Aggregations aggregations = response.getAggregations();
            ParsedStringTerms parsedStringTerms = aggregations.get("skuIdAgg");
            for (Terms.Bucket bucket : parsedStringTerms.getBuckets()) {
                ParsedSum volumeSum = bucket.getAggregations().get("saleVolumeSum"); // Key as String
                double sumValue = volumeSum.getValue();         // Doc count
                log.info("key [{}], doc_count [{}]", bucket.getKey(), sumValue);
            }
            log.info("Success ~~");
        }
    }
    /**
     * dateHistogram：直方图查询，也就是销量查询,比如1月份300,2月份600,3月份900,4月份1500
     */
    @Test
    public void testCumulativeSumAgg() {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();

        AggregationBuilder dateHistogramAgg = AggregationBuilders.dateHistogram("zqTimeHistogram").field("zqTime").calendarInterval(DateHistogramInterval.MONTH).format("yyyy-MM-dd");

        AggregationBuilder volumeSumAgg = AggregationBuilders.sum("volumeSum").field("calcSalesVolume");

        //关键pipelineAggregationBuilder
        CumulativeSumPipelineAggregationBuilder sumPipelineAggregationBuilder = PipelineAggregatorBuilders.cumulativeSum("cumalative_sum", "volumeSum");

        dateHistogramAgg.subAggregation(sumPipelineAggregationBuilder);
        dateHistogramAgg.subAggregation(volumeSumAgg);

        sourceBuilder.query(matchAllQueryBuilder);
        sourceBuilder.aggregation(dateHistogramAgg);

        searchRequest.source(sourceBuilder);
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
            ParsedDateHistogram parsedDateHistogram = aggregations.get("zqTimeHistogram");
            for (Histogram.Bucket bucket : parsedDateHistogram.getBuckets()) {
                String keyAsString = bucket.getKeyAsString(); // Key as String
                long docCount = bucket.getDocCount();         // Doc count
                log.info("key [{}], date [{}], doc_count [{}]", keyAsString, bucket.getKey(), docCount);
            }
            log.info("Success ~~");
        }
    }
}
