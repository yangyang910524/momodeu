/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.HomeworkOffice;
import com.jeeplus.modules.sys.mapper.HomeworkOfficeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 作业-班级关联Service
 * @author yangyang
 * @version 2019-04-26
 */
@Service
@Transactional(readOnly = true)
public class HomeworkOfficeService extends CrudService<HomeworkOfficeMapper, HomeworkOffice> {
    @Resource
    private HomeworkOfficeMapper homeworkOfficeMapper;

	public HomeworkOffice get(String id) {
		return super.get(id);
	}
	
	public List<HomeworkOffice> findList(HomeworkOffice homeworkOffice) {
		return super.findList(homeworkOffice);
	}
	
	public Page<HomeworkOffice> findPage(Page<HomeworkOffice> page, HomeworkOffice homeworkOffice) {
		return super.findPage(page, homeworkOffice);
	}
	
	@Transactional(readOnly = false)
	public void save(HomeworkOffice homeworkOffice) {
		super.save(homeworkOffice);
	}
	
	@Transactional(readOnly = false)
	public void delete(HomeworkOffice homeworkOffice) {
		super.delete(homeworkOffice);
	}

    @Transactional(readOnly = false)
    public void insertStudentHomework(HomeworkOffice homeworkOffice) {
        homeworkOfficeMapper.insertStudentHomework(homeworkOffice);
    }

    @Transactional(readOnly = false)
    public void deleteStudentHomework(HomeworkOffice homeworkOffice) {
        homeworkOfficeMapper.deleteStudentHomework(homeworkOffice);
    }
}