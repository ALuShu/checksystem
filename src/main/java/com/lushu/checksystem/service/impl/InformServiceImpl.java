package com.lushu.checksystem.service.impl;

import com.lushu.checksystem.dao.InformDao;
import com.lushu.checksystem.service.InformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author ALuShu
 * @Description
 * @date 2020/1/10
 * @throws
 * @since
 */
@Service
@Slf4j
public class InformServiceImpl implements InformService {

    private InformDao informDao;
    public InformServiceImpl(InformDao informDao) {
        this.informDao = informDao;
    }

}
