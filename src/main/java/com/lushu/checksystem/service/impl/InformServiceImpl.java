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
    public Inform selectInform(Integer id) {
        return informDao.selectInform(id);
    }

    @Override
    public PageBean<Inform> selectInforms(String publisher, int page, int limit) {
        HashMap<String, Object> map = new HashMap<>();
        PageBean<Inform> pageBean = new PageBean<>();
        pageBean.setCurrentPage(page);
        pageBean.setPageSize(limit);
        Integer count = informDao.countByPublisher(publisher);
        pageBean.setTotalRecord(count);

        map.put("start", (page-1)*limit);
        map.put("limit", pageBean.getPageSize());
        map.put("publisher", publisher);
        List<Inform> informs = informDao.selectInformsByPublisher(map);
        pageBean.setList(informs);
        return pageBean;
    }

    @Override
    public PageBean<Inform> selectAllInforms(int currentPage) {
        HashMap<String, Object> map = new HashMap<>();
        PageBean<Inform> pageBean = new PageBean<>();
        pageBean.setCurrentPage(currentPage);
        int pageSize = 15;
        pageBean.setPageSize(pageSize);
        Integer total = informDao.count();
        pageBean.setTotalRecord(total);

        if (total%pageSize == 0){
            pageBean.setTotalPage(total/pageSize);
        }else {
            pageBean.setTotalPage((total/pageSize)+1);
        }

        map.put("start", (currentPage-1)*pageSize);
        map.put("limit", pageBean.getPageSize());
        List<Inform> informs = informDao.selectAllInforms(map);
        pageBean.setList(informs);
        return pageBean;
    }

    @Override
    public PageBean<Inform> selectInformsBySort(Integer type, int currentPage, int limit, String ... department) {
        HashMap<String, Object> map = new HashMap<>();
        PageBean<Inform> pageBean = new PageBean<>();
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(limit);
        if (department.length == 0) {
            int total = informDao.countByType(type);
            pageBean.setTotalRecord(total);
            if (total % limit == 0) {
                pageBean.setTotalPage(total / limit);
            } else {
                pageBean.setTotalPage((total / limit) + 1);
            }

            map.put("start", (currentPage - 1) * limit);
            map.put("limit", pageBean.getPageSize());
            map.put("type", type);
            List<Inform> informs = informDao.selectInformsByType(map);
            pageBean.setList(informs);

        }else {
            String tmpDepartment = department[0];
            map.put("department", tmpDepartment);
            int total = informDao.countBySort(type, tmpDepartment);
            pageBean.setTotalRecord(total);
            if (total % limit == 0) {
                pageBean.setTotalPage(total / limit);
            } else {
                pageBean.setTotalPage((total / limit) + 1);
            }

            map.put("start", (currentPage - 1) * limit);
            map.put("limit", pageBean.getPageSize());
            map.put("type", type);
            List<Inform> informs = informDao.selectInformsBySort(map);
            pageBean.setList(informs);
        }
        return pageBean;
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
