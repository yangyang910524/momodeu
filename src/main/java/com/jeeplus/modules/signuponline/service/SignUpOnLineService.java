/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.signuponline.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.signuponline.entity.SignUpOnLine;
import com.jeeplus.modules.signuponline.mapper.SignUpOnLineMapper;

/**
 * 在线报名Service
 * @author yangyang
 * @version 2019-05-16
 */
@Service
@Transactional(readOnly = true)
public class SignUpOnLineService extends CrudService<SignUpOnLineMapper, SignUpOnLine> {

	public SignUpOnLine get(String id) {
		return super.get(id);
	}
	
	public List<SignUpOnLine> findList(SignUpOnLine signUpOnLine) {
		return super.findList(signUpOnLine);
	}
	
	public Page<SignUpOnLine> findPage(Page<SignUpOnLine> page, SignUpOnLine signUpOnLine) {
		return super.findPage(page, signUpOnLine);
	}
	
	@Transactional(readOnly = false)
	public void save(SignUpOnLine signUpOnLine) {
		super.save(signUpOnLine);
	}
	
	@Transactional(readOnly = false)
	public void delete(SignUpOnLine signUpOnLine) {
		super.delete(signUpOnLine);
	}
	
}