package com.lushu.checksystem.service.impl;

import com.lushu.checksystem.dao.InformDao;
import com.lushu.checksystem.pojo.Inform;
import com.lushu.checksystem.pojo.PageBean;
import com.lushu.checksystem.service.InformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

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

    @Override
    public List<Inform> selectInforms(Integer sendId) {
        return informDao.selectInforms(sendId);
    }

    @Override
    public PageBean<Inform> selectAllInforms(int currentPage) {
        HashMap<String, Object> map = new HashMap<>();
        PageBean<Inform> pageBean = new PageBean<>();
        pageBean.setCurrentPage(currentPage);
        int pageSize = 15;
        pageBean.setPageSize(pageSize);
        int total = informDao.count();
        pageBean.setTotalRecord(total);

        if (total%pageSize == 0){
            pageBean.setTotalPage(total/pageSize);
        }else {
            pageBean.setTotalPage((total/pageSize)+1);
        }

        map.put("start", (currentPage-1)*pageSize);
        map.put("size", pageBean.getPageSize());
        List<Inform> informs = informDao.selectAllInforms(map);
        pageBean.setList(informs);
        return pageBean;
    }

    @Override
    public List<Inform> selectInformsByType(boolean type) {
        return informDao.selectInformsByType(type);
    }

    @Override
    public Integer addInforms(List<Inform> list) {
        return informDao.addInforms(list);
    }

    @Override
    public Integer updateInform(Inform inform) {
        return informDao.updateInform(inform);
    }

    @Override
    public Integer deleteInforms(List<Integer> ids) {
        return informDao.deleteInforms(ids);
    }
}
