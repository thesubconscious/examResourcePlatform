package com.e_r_platform.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private Integer resource_id;

    /**
     * 
     */
    private Integer course_id;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String path;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Resource other = (Resource) that;
        return (this.getResource_id() == null ? other.getResource_id() == null : this.getResource_id().equals(other.getResource_id()))
            && (this.getCourse_id() == null ? other.getCourse_id() == null : this.getCourse_id().equals(other.getCourse_id()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getPath() == null ? other.getPath() == null : this.getPath().equals(other.getPath()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getResource_id() == null) ? 0 : getResource_id().hashCode());
        result = prime * result + ((getCourse_id() == null) ? 0 : getCourse_id().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getPath() == null) ? 0 : getPath().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", resourceId=").append(resource_id);
        sb.append(", courseId=").append(course_id);
        sb.append(", name=").append(name);
        sb.append(", path=").append(path);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}