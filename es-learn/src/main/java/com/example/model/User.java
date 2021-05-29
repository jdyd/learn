package com.example.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author: cuijian03
 * @description:
 * @create: 2021-04-17 14:19
 */
@Data
@Builder
public class User {
    private String name;
    private Integer age;
    private String sex;

}
