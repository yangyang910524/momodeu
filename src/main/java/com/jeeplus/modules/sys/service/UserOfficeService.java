/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.UserOffice;
import com.jeeplus.modules.sys.mapper.UserOfficeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户-班级关联Service
 * @author yangyang
 * @version 2019-04-25
 */
@Service
@Transactional(readOnly = true)
public class UserOfficeService extends CrudService<UserOfficeMapper, UserOffice> {

}