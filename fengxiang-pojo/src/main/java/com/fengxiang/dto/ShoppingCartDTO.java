package com.fengxiang.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.io.Serializable;

@Data
public class ShoppingCartDTO implements Serializable {

    @JsonProperty("goodId")  // 明确指定 JSON 字段名
    private Long goodId; //商品id

}
