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
package io.datavines.server.repository.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.datavines.server.repository.entity.CommonTaskCommand;
import io.datavines.server.repository.mapper.CommonTaskCommandMapper;
import io.datavines.server.repository.service.CommonTaskCommandService;

import org.springframework.stereotype.Service;

@Service("ccommonTaskCommandService")
public class CommonTaskCommandServiceImpl
        extends ServiceImpl<CommonTaskCommandMapper, CommonTaskCommand>
        implements CommonTaskCommandService {

    @Override
    public long create(CommonTaskCommand commonTaskCommand) {
        baseMapper.insert(commonTaskCommand);
        return commonTaskCommand.getId();
    }

    @Override
    public int update(CommonTaskCommand commonTaskCommand) {
        return 0;
    }

    @Override
    public CommonTaskCommand getById(long id) {
        return null;
    }

    @Override
    public CommonTaskCommand getOne(int totalSlot, int currentSlot) {
        return baseMapper.getOne(totalSlot, currentSlot);
    }

    @Override
    public int deleteById(long id) {
        return baseMapper.deleteById(id);
    }
}
