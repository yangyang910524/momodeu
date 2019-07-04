/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.UserHomework;
import com.jeeplus.modules.sys.entity.UserOffice;
import com.jeeplus.modules.sys.mapper.UserHomeworkMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户-作业关联Service
 * @author yangyang
 * @version 2019-04-28
 */
@Service
@Transactional(readOnly = true)
public class UserHomeworkService extends CrudService<UserHomeworkMapper, UserHomework> {
    @Resource
    private UserHomeworkMapper userHomeworkMapper;

	@Override
    public UserHomework get(String id) {
		return super.get(id);
	}

    @Override
	public List<UserHomework> findList(UserHomework userHomework) {
		return super.findList(userHomework);
	}

    @Override
	public Page<UserHomework> findPage(Page<UserHomework> page, UserHomework userHomework) {
		return super.findPage(page, userHomework);
	}

    @Override
	@Transactional(readOnly = false)
	public void save(UserHomework userHomework) {
		super.save(userHomework);
	}

    @Override
	@Transactional(readOnly = false)
	public void delete(UserHomework userHomework) {
		super.delete(userHomework);
	}

	/**
	 * @Description 解除班级与人员关系时删除学生所有未完成与为批改的作业
	 **/
    @Transactional(readOnly = false)
    public void deleteUserHomeworkByState(UserOffice userOffice) {
        userHomeworkMapper.deleteUserHomeworkByState(userOffice);
    }

    /**
     * @Description 解除班级与人员关系时清空学生作业officeid
     **/
    @Transactional(readOnly = false)
    public void cleanUserHomeworkOfficeId(UserOffice userOffice) {
        userHomeworkMapper.cleanUserHomeworkOfficeId(userOffice);
    }

    //班级中添加学生时,将班级所有作业下发给该学生一份（如果存在则不再次添加）。
    @Transactional(readOnly = false)
    public void addUserHomeworkByOffice(UserOffice userOffice) {
        userHomeworkMapper.addUserHomeworkByOffice(userOffice);
    }

    //班级中添加学生时：将该学生原有作业与新班级进行绑定
    @Transactional(readOnly = false)
    public void updateUserHomeworkOffice(UserOffice userOffice) {
        userHomeworkMapper.updateUserHomeworkOffice(userOffice);
    }
}