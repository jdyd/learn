package com.example.base.demo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedAvg;
import org.elasticsearch.search.aggregations.metrics.ParsedCardinality;
import org.elasticsearch.search.aggregations.metrics.ParsedMax;
import org.elasticsearch.search.aggregations.metrics.ParsedMin;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * 指标聚合查询，求平均值，最大值
 */
@Slf4j
public class ESMetricsAggDemo extends ESAggDemo {

    /**
     * 求sku销量最大值，最小值，平均值以及sum的聚合
     * sql: select max(saleVolume),min(saleVolume),avg(saleVolume),sum(saleVolume) from sku group by skuId
     */
    @Test
    public void testMaxAvgMinSumAgg() {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String maxVolumeAggName = "maxVolumeAgg";
        String minVolumeAggName = "minVolumeAgg";
        String avgVolumeAggName = "avgVolumeAgg";
        String sumVolumeAggName = "sumVolumeAgg";
        //设置group by 的agg
        AggregationBuilder skuIdAgg = AggregationBuilders.terms("skuIdAgg").field("skuId").size(1000);
        //设置max,min,avg,sum的聚合sub agg
        AggregationBuilder maxVolumeAgg = AggregationBuilders.max(maxVolumeAggName).field(volumeFiledName);
        AggregationBuilder minVolumeAgg = AggregationBuilders.min(minVolumeAggName).field(volumeFiledName);
        AggregationBuilder avgVolumeAgg = AggregationBuilders.avg(avgVolumeAggName).field(volumeFiledName);
        AggregationBuilder sumVolumeAgg = AggregationBuilders.sum(sumVolumeAggName).field(volumeFiledName);
        //将metrics agg添加至group by 的agg
        skuIdAgg.subAggregation(maxVolumeAgg);
        skuIdAgg.subAggregation(minVolumeAgg);
        skuIdAgg.subAggregation(avgVolumeAgg);
        skuIdAgg.subAggregation(sumVolumeAgg);

        //设置search request
        searchSourceBuilder.aggregation(skuIdAgg);
        searchSourceBuilder.query(cityIdTermQuery);
        searchRequest.source(searchSourceBuilder);
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
            ParsedStringTerms skuIdAggTerms = aggregations.get("skuIdAgg");
            //解析每个bucket
            for (Terms.Bucket bucket : skuIdAggTerms.getBuckets()) {
                ParsedSum sum = bucket.getAggregations().get(sumVolumeAggName);
                ParsedAvg avg = bucket.getAggregations().get(avgVolumeAggName);
                ParsedMin min = bucket.getAggregations().get(minVolumeAggName);
                ParsedMax max = bucket.getAggregations().get(maxVolumeAggName);
                String skuId = bucket.getKey().toString();

                log.info("[ skuId: %s, 销量sum:%s, 平均销量:%s，sku最大销量:%s, sku最小销量:%s]",
                        skuId,sum.getValue(),avg.getValue(),max.getValue(), min.getValue()
                );
            }
        }

    }

    /**
     * 去重统计，统计去重后key的总量，cardinality关键字
     * sql: select count(distinct skuId) from sku where cityId = 44
     */
    @Test
    public void testDistinctCount() {
        Map<String, List<Long>> categoryMaps = Maps.newConcurrentMap();
        categoryMaps.put("1", Lists.newArrayList(1000L, 10001L));
        categoryMaps.put("2", Lists.newArrayList(10003L, 10004L));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String skuIdDistinctAggkey = "skuIdAgg";
        //group by Agg
        AggregationBuilder distinctSkuIdAgg = AggregationBuilders.cardinality(skuIdDistinctAggkey).field("skuId");

        //将条件添加至searchRequest
        searchSourceBuilder.aggregation(distinctSkuIdAgg);
        searchSourceBuilder.query(cityIdTermQuery);
        searchRequest.source(searchSourceBuilder);
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
            ParsedCardinality parsedCardinality = aggregations.get(skuIdDistinctAggkey);
            log.info("[去重后skuId的总量:%s]", parsedCardinality.getValue());
        }
    }
}
