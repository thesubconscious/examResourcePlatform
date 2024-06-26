package com.e_r_platform.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName resource_completions
 */
@TableName(value ="resource_completions")
@Data
public class ResourceCompletions implements Serializable {
    /**
     * 
     */
    private Integer resource_id;

    /**
     * 
     */
    private Integer student_id;

    /**
     * 
     */
    private Date completion_time;

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
        ResourceCompletions other = (ResourceCompletions) that;
        return (this.getResource_id() == null ? other.getResource_id() == null : this.getResource_id().equals(other.getResource_id()))
            && (this.getStudent_id() == null ? other.getStudent_id() == null : this.getStudent_id().equals(other.getStudent_id()))
            && (this.getCompletion_time() == null ? other.getCompletion_time() == null : this.getCompletion_time().equals(other.getCompletion_time()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getResource_id() == null) ? 0 : getResource_id().hashCode());
        result = prime * result + ((getStudent_id() == null) ? 0 : getStudent_id().hashCode());
        result = prime * result + ((getCompletion_time() == null) ? 0 : getCompletion_time().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", resourceId=").append(resource_id);
        sb.append(", studentId=").append(student_id);
        sb.append(", completionTime=").append(completion_time);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}