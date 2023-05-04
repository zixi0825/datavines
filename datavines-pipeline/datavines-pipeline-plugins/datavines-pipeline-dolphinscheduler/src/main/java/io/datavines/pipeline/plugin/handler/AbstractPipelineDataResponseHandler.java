package io.datavines.pipeline.plugin.handler;

import io.datavines.common.entity.ListWithQueryColumn;
import io.datavines.common.entity.QueryColumn;
import io.datavines.common.utils.JSONUtils;
import io.datavines.pipeline.api.PipelineDataResponse;
import io.datavines.pipeline.api.PipelineDataResponseHandler;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractPipelineDataResponseHandler implements PipelineDataResponseHandler {

    // 用于构造INSERT语句
    protected List<QueryColumn> targetTableHeaderList = new ArrayList<>();

    //用于替换列名
    protected Map<String,String> oldColumn2NewColumn = new HashMap<>();

    //用于将一些特有的属性放到properties列中
    protected List<String> propertiesContainColumns = new ArrayList<>();

    protected abstract void operateHeader();

    @Override
    public void handle(PipelineDataResponse response) {
        operateHeader();

        if (response != null
                && response.getResponseDataList() != null
                && CollectionUtils.isNotEmpty(response.getResponseDataList().getResultList())) {
            ListWithQueryColumn list = response.getResponseDataList();
            list.setColumns(targetTableHeaderList);
            List<Map<String, Object>> resultList = list.getResultList();
            // 遍历resultList, 对oldColumn2NewColumn进行替换
            for (Map<String, Object> record : resultList) {
                if (MapUtils.isNotEmpty(oldColumn2NewColumn)) {
                    oldColumn2NewColumn.forEach((k,v) -> {
                        Object value = record.get(k);
                        record.remove(k);
                        record.put(v,value);
                    });
                }

                if (CollectionUtils.isNotEmpty(propertiesContainColumns)) {
                    Map<String,Object> properties = new HashMap<>();
                    propertiesContainColumns.forEach(name -> {
                        properties.put(name, record.get(name));
                        record.remove(name);
                    });

                    record.put("properties", JSONUtils.toJsonString(properties));
                }
            }
        }
    }
}
