package com.lushu.checksystem.service.impl;

import com.lushu.checksystem.dao.InformDao;
import com.lushu.checksystem.pojo.Inform;
import com.lushu.checksystem.service.InformService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ALuShu
 * @Description
 * @date 2020/1/10
 * @throws
 * @since
 */
@Service
public class InformServiceImpl implements InformService {

    private InformDao informDao;
    public InformServiceImpl(InformDao informDao) {
        this.informDao = informDao;
    }

    @Override
    public int addInform(List<Inform> list) {
        return informDao.addInform(list);
    }

    @Override
    public List<Inform> selectInform(Integer receiveId) {
        return informDao.selectInform(receiveId);
    }
}
