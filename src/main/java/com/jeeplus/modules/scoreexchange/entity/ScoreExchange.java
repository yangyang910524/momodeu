/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.scoreexchange.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.sys.entity.User;

/**
 * 积分兑换信息Entity
 * @author yangyang
 * @version 2019-04-27
 */
public class ScoreExchange extends DataEntity<ScoreExchange> {
	
	private static final long serialVersionUID = 1L;
	private String score;		// 所需分数
	private String photo;		// 照片
	private String name;		// 名称
    private String exchangeTimes;//兑换次数
    private User exchangeUser;//兑换人
    public String getExchangeTimes() {
        return exchangeTimes;
    }

    public User getExchangeUser() {
        return exchangeUser;
    }

    public void setExchangeUser(User exchangeUser) {
        this.exchangeUser = exchangeUser;
    }

    public void setExchangeTimes(String exchangeTimes) {
        this.exchangeTimes = exchangeTimes;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}