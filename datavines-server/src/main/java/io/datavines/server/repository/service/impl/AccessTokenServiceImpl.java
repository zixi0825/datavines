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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.datavines.core.constant.DataVinesConstants;
import io.datavines.core.exception.DataVinesServerException;
import io.datavines.core.utils.TokenManager;
import io.datavines.server.repository.entity.AccessToken;
import io.datavines.server.repository.mapper.AccessTokenMapper;
import io.datavines.server.repository.service.AccessTokenService;

import io.datavines.server.utils.ContextHolder;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service("accessTokenService")
public class AccessTokenServiceImpl extends ServiceImpl<AccessTokenMapper, AccessToken> implements AccessTokenService {

    @Resource
    private TokenManager tokenManager;

    @Override
    public Boolean checkTokenExist(String token) {
        token = token.replace("Bearer ","");
        return getOne(new LambdaQueryWrapper<AccessToken>().eq(AccessToken::getToken, token), false) != null;
    }

    @Override
    public String generateToken(Long workspaceId, String expireTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime expireDateTime = LocalDateTime.parse(expireTime, formatter);
        LocalDateTime now = LocalDateTime.now();
        if (expireDateTime.isBefore(now)) {
            throw new DataVinesServerException("expire time must after now");
        }
        long secondsBetween = ChronoUnit.SECONDS.between(now, expireDateTime);
        String token = tokenManager.generateToken(String.valueOf(ContextHolder.getParam(DataVinesConstants.TOKEN)), secondsBetween);

        AccessToken accessToken = new AccessToken();
        accessToken.setToken(token);
        accessToken.setUserId(ContextHolder.getUserId());
        accessToken.setWorkspaceId(workspaceId);
        accessToken.setExpireTime(expireDateTime);
        accessToken.setCreateBy(ContextHolder.getUserId());
        accessToken.setCreateTime(LocalDateTime.now());
        accessToken.setUpdateBy(ContextHolder.getUserId());
        accessToken.setUpdateTime(LocalDateTime.now());
        save(accessToken);

        return token;
    }

    @Override
    public boolean deleteToken(Long id) {
        return removeById(id);
    }

    @Override
    public List<AccessToken> listToken(Long workspaceId, Long userId) {
        return list(new LambdaQueryWrapper<AccessToken>().eq(AccessToken::getWorkspaceId, workspaceId).eq(AccessToken::getUserId, userId));
    }
}
