package com.qn.service;

import com.qn.model.DestServerConfig;
import com.qn.model.SrcServerConfig;

/**
 * @description:
 * @author: Youq
 * @create: 2019-03-07 14:52
 */
public interface ConfigService {
    SrcServerConfig getConfigInfo();
    boolean saveConfigInfo(SrcServerConfig config);
    DestServerConfig getDestConfigInfo();
    boolean saveDestConfigInfo(DestServerConfig config);
}
