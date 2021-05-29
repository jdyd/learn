package com.example;

import com.example.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

/**
 * @author: cuijian03
 * @description:
 * @create: 2021-04-17 14:29
 */
@Slf4j
public class Es_doc_study {
    public static void main(String[] args) throws IOException {

        // 开启客户端  9200 端口为 Elasticsearch 的 Web 通信端口，localhost 为启动 ES 服务的主机名
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

        selectDoc(restHighLevelClient, "1001");
        createDoc(restHighLevelClient);
        selectDoc(restHighLevelClient, "1001");

        System.out.println("=======================");
        updateDoc(restHighLevelClient);
        selectDoc(restHighLevelClient, "1001");

        System.out.println("=======================");
        deleteDoc(restHighLevelClient);
        selectDoc(restHighLevelClient, "1001");

        System.out.println("=======================");
        bulkCreateDoc(restHighLevelClient);
        selectDoc(restHighLevelClient, "1001");
        selectDoc(restHighLevelClient, "1002");
        selectDoc(restHighLevelClient, "1003");

//        System.out.println("=======================");
//        bulkDeleteDoc(restHighLevelClient);
//        selectDoc(restHighLevelClient, "1001");
//        selectDoc(restHighLevelClient, "1002");
//        selectDoc(restHighLevelClient, "1003");

        // 关闭客户端
        restHighLevelClient.close();
    }

    /**
     * 创建文档
     *
     * @param restHighLevelClient
     * @throws IOException
     */
    private static void createDoc(RestHighLevelClient restHighLevelClient) throws IOException {
        // 新增文档 - 请求对象
        IndexRequest indexRequest = new IndexRequest();
        // 设置索引及唯一性标识
        indexRequest.index("user").id("1001");

        // 创建数据对象
        User user = User.builder()
                .name("zhangsan")
                .age(30)
                .sex("男")
                .build();

        // 添加文档数据，数据格式为 JSON 格式
        ObjectMapper objectMapper = new ObjectMapper();
        String userString = objectMapper.writeValueAsString(user);
        indexRequest.source(userString, XContentType.JSON);

        // 客户端发送请求，获取响应对象
        IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

        System.out.println(index.getResult());
    }

    /**
     * 修改文档
     *
     * @param restHighLevelClient
     * @throws IOException
     */
    private static void updateDoc(RestHighLevelClient restHighLevelClient) throws IOException {
        // 修改文档 - 请求对象
        UpdateRequest request = new UpdateRequest();

        // 配置修改参数
        request.index("user").id("1001");

        // 设置请求体，对数据进行修改
        request.doc(XContentType.JSON, "sex", "女");

        // 客户端发送请求，获取响应对象
        UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);

        System.out.println("_index:" + response.getIndex());
        System.out.println("_id:" + response.getId());
        System.out.println("_result:" + response.getResult());
    }

    /**
     * 查看文档
     *
     * @param restHighLevelClient
     * @throws IOException
     */
    private static void selectDoc(RestHighLevelClient restHighLevelClient, String docId) throws IOException {
        //1.创建请求对象
        GetRequest request = new GetRequest().index("user").id(docId);

        //2.客户端发送请求，获取响应对象
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT); ////3.打印结果信息
        System.out.println("_index:" + response.getIndex());
        System.out.println("_type:" + response.getType());
        System.out.println("_id:" + response.getId());
        System.out.println("source:" + response.getSourceAsString());
    }

    /**
     * 删除文档
     *
     * @param restHighLevelClient
     * @throws IOException
     */
    private static void deleteDoc(RestHighLevelClient restHighLevelClient) throws IOException {

        //创建请求对象
        DeleteRequest request = new DeleteRequest().index("user").id("1001");

        //客户端发送请求，获取响应对象
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);

        //打印信息
        System.out.println(response.toString());
    }

    /**
     * 批量新增
     *
     * @param restHighLevelClient
     * @throws IOException
     */
    private static void bulkCreateDoc(RestHighLevelClient restHighLevelClient) throws IOException {

        //创建批量新增请求对象
        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest().index("user").id("1001").source(XContentType.JSON, "name", "zhangsan", "age", 30, "sex", "男"));
        request.add(new IndexRequest().index("user").id("1002").source(XContentType.JSON, "name", "lisi", "age", 50, "sex", "男"));
        request.add(new IndexRequest().index("user").id("1003").source(XContentType.JSON, "name", "wangwu", "age", 40, "sex", "女"));

        //客户端发送请求，获取响应对象
        BulkResponse responses = restHighLevelClient.bulk(request, RequestOptions.DEFAULT); //打印结果信息
        System.out.println("took:" + responses.getTook());
        System.out.println("items:" + responses.getItems());

    }

    /**
     * 批量删除文档
     *
     * @param restHighLevelClient
     * @throws IOException
     */
    private static void bulkDeleteDoc(RestHighLevelClient restHighLevelClient) throws IOException {

        //创建批量删除请求对象
        BulkRequest request = new BulkRequest();
        request.add(new DeleteRequest().index("user").id("1001"));
        request.add(new DeleteRequest().index("user").id("1002"));
        request.add(new DeleteRequest().index("user").id("1003"));

        //客户端发送请求，获取响应对象
        BulkResponse responses = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);

        //打印结果信息
        System.out.println("took:" + responses.getTook());
        System.out.println("items:" + responses.getItems());

    }
}