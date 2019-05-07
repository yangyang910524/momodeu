package com.jeeplus.modules.test;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author yangyang
 * @description 测试
 * @create 2019-04-28 09:01
 **/
public class Test {
    public static void main(String[] args) throws JavaLayerException, FileNotFoundException {
        // 播放一个 mp3 音频文件, 代码很简单
        File file = new File("d://a.mp3");
        Player player = new Player(new FileInputStream(file));
        player.play();
    }

}