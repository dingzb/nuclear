package cc.idiary.nuclear.service;

import cc.idiary.nuclear.service.InitServiceImpl.InitProcess;

public interface InitService {

    /**
     * 初始化
     *
     * @throws ServiceException
     */
    void init(InitProcess process) throws ServiceException;

}
