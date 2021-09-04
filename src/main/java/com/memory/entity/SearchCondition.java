package com.memory.entity;

import com.memory.constant.DataType;
import com.memory.constant.WayOfComparison;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: silence
 * @Date: 2021/9/4 14:12
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchCondition {

    Double value;

    DataType dataType;

    WayOfComparison way;
}
