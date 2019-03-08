package com.qn.service;

import com.qn.model.Config;

/**
 * @description:
 * @author: Youq
 * @create: 2019-03-07 14:52
 */
public interface ConfigService {
    Config getConfigInfo();
    boolean saveConfigInfo(Config config);
}
