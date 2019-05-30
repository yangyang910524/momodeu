/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.scoreexchangerecord.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.scoreexchange.entity.ScoreExchange;
import com.jeeplus.modules.sys.entity.User;

/**
 * 积分兑换记录Entity
 * @author yangyang
 * @version 2019-05-30
 */
public class ScoreExchangeRecord extends DataEntity<ScoreExchangeRecord> {
	
	private static final long serialVersionUID = 1L;
	private ScoreExchange scoreExchange;		// 积分兑换物品id
	private User user;		// 用户id

    public ScoreExchange getScoreExchange() {
        return scoreExchange;
    }

    public void setScoreExchange(ScoreExchange scoreExchange) {
        this.scoreExchange = scoreExchange;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}