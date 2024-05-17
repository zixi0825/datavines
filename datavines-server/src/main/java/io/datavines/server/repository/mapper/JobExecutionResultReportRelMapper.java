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

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.datavines.server.repository.entity.JobExecutionResult;
import io.datavines.server.repository.entity.JobExecutionResultReportRel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JobExecutionResultReportRelMapper extends BaseMapper<JobExecutionResultReportRel> {

    @Select("SELECT djer.* from dv_job_execution_result djer join dv_job_execution_result_report_rel djerrr on djer.id = djerrr.job_execution_result_id where djerrr.quality_report_id = #{reportId}")
    List<JobExecutionResult> listExecutionResultByReportId(@Param("reportId") Long reportId);
}
