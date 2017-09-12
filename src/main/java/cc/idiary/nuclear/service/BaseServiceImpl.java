package cc.idiary.nuclear.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import cc.idiary.nuclear.dao.BaseDao;
import cc.idiary.nuclear.entity.BaseEntity;
import cc.idiary.nuclear.model.BaseModel;
import cc.idiary.nuclear.query.BaseQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 基础服务类
 *
 * @author Dzb
 */
public abstract class BaseServiceImpl<E extends BaseEntity> implements BaseService<E> {

    private Logger logger = LogManager.getLogger(BaseServiceImpl.class);

    @Autowired
    private BaseDao<E> baseDao;

    /**
     * 根据Id删除实体
     *
     * @param id
     * @throws ServiceException
     */
    protected void delete(String id) throws ServiceException {
        E e = baseDao.getById(id);
        if (e == null)
            throw new ServiceException("该记录不存在！");
        try {
            baseDao.delete(e);
        } catch (Exception ex) {
            logger.catching(ex);
            throw new ServiceException();
        }
    }

    /**
     * 根据Id数组删除实体
     *
     * @param ids
     * @return
     * @throws ServiceException
     */
    protected Integer delete(String[] ids) throws ServiceException {
        Integer result = null;
        try {
            result = baseDao.delByIds(ids);
        } catch (Exception e) {
            logger.catching(e);
            throw new ServiceException();
        }
        return result;
    }

    protected Set<String> getIds(Iterable<?> source) {
        Set<String> ids = new HashSet<String>();
        if (source != null) {
            for (Object o : source) {
                if (o instanceof BaseEntity) {
                    BaseEntity e = (BaseEntity) o;
                    ids.add(e.getId());
                }
                if (o instanceof BaseModel) {
                    BaseModel m = (BaseModel) o;
                    ids.add(m.getId());
                }
                if (o instanceof BaseQuery) {
                    BaseQuery q = (BaseQuery) o;
                    ids.add(q.getId());
                }
            }
        }
        return ids;
    }

    /**
     * 时间区域判定
     */
    protected void queryTimeAssert(Date start, Date end) throws ServiceException {

        if (start != null && end == null && start.after(new Date())) {
            throw new ServiceException("开始时间不能大于当前时间");
        }
        if (end != null && start == null && end.after(new Date())) {
            throw new ServiceException("结束时间不能大于当前时间");
        }
        if (start != null && end != null && start.after(end)){
            throw new ServiceException("开始时间不能大于结束时间");
        }
    }

    protected void queryBasicAssert (BaseQuery query) throws ServiceException {
        if (query == null) {
            throw new ServiceException("查询对象不能为空");
        }
    }
}
