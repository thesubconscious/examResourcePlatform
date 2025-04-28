package com.e_r_platform.model;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName resource
 */
@TableName(value ="resource")
@Data
public class Resource implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer node_id; // 改用nodeId更符合树形结构特征

    private Integer course_id;

    private Integer parent_node_id; // 必须存在的父节点引用

    @EnumValue // MyBatis-Plus枚举处理
    private NodeType type; // 核心类型区分

    private String title;

    private String file_uri;

    private Integer display_order;

    public boolean isFolder() {
        return type == NodeType.CHAPTER;
    }

    public enum NodeType {
        CHAPTER,
        LEAF
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}