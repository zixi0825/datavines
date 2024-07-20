/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.datavines.server.repository.mapper;

import io.datavines.server.api.dto.vo.JobExecutionAggState;
import io.datavines.server.repository.entity.JobExecutionResult;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JobExecutionResultMapper extends BaseMapper<JobExecutionResult>  {

    @Select("SELECT * from dv_job_execution_result where job_execution_id = #{jobExecutionId} limit 1 ")
    JobExecutionResult getOne(Long jobExecutionId);

    @Select("SELECT * from dv_job_execution_result where job_execution_id in (select id from dv_job_execution where job_id = #{jobId} and start_time >= #{startTime} and start_time <= #{endTime}) order by create_time")
    List<JobExecutionResult> listByJobIdAndTimeRange(@Param("jobId") Long jobId,
                                                     @Param("startTime")String startTime,
                                                     @Param("endTime")String endTime);
    @Select("SELECT t1.id,t1.metric_name,t1.database_name,t1.table_name,t1.column_name,t1.score\n" +
            "FROM\n" +
            "\t(select djer.* from dv_job_execution_result djer join dv_job_execution dje on djer.job_execution_id = dje.id where dje.datasource_id = #{datasourceId} and djer.create_time >= #{startTime} and djer.create_time <= #{endTime}\n" +
            ") t1\n" +
            "\tINNER JOIN ( SELECT t3.database_name,t3.table_name,t3.column_name,t3.metric_name, MAX(t3.create_time) as max_timestamp FROM (select djer.* from dv_job_execution_result djer join dv_job_execution dje on djer.job_execution_id = dje.id where dje.datasource_id = #{datasourceId} and djer.create_time >= #{startTime} and djer.create_time <= #{endTime}\n" +
            ") t3 GROUP BY database_name,table_name,column_name,metric_name ) t2\n" +
            "    ON t1.database_name = t2.database_name and t1.table_name = t2.table_name and t1.column_name = t2.column_name and t1.metric_name = t2.metric_name and t1.create_time = t2.max_timestamp;")
    List<JobExecutionResult> listByDatasourceIdAndTimeRange(@Param("datasourceId") Long datasourceId,
                                                            @Param("startTime")String startTime,
                                                            @Param("endTime")String endTime);

    List<JobExecutionAggState> listByJobExecutionId(@Param("jobExecutionIdList") List<Long> jobExecutionIdList);
}
