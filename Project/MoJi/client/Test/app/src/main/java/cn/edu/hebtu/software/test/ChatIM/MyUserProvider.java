package cn.edu.hebtu.software.test.ChatIM;

import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.HashMap;
import java.util.Map;

public class MyUserProvider implements EaseUI.EaseUserProfileProvider {
    private static MyUserProvider myUserProvider;
    private Map<String, EaseUser> userList = new HashMap<>();

    @Override
    public EaseUser getUser(String username) {
        if(userList.containsKey(username))
            //有就返归这个对象。。
            return userList.get(username);
        System.out.println("error ： 没有 数据" + username);
        return null;
    }

    public void setUser(String username,String nickname,String img){
        if(!userList.containsKey(username)) {
            EaseUser easeUser = new EaseUser(username);
            userList.put(username,easeUser);
        }
        EaseUser easeUser = getUser(username);
        easeUser.setNickname(nickname);
        easeUser.setAvatar(img);
    }

    private MyUserProvider(){
        System.out.println("init myTestProfileProvider");
    }
    // 获取单类。。
    public static MyUserProvider getInstance() {
        if (myUserProvider == null) {
            myUserProvider = new MyUserProvider();
        }
        return myUserProvider;
    }
}
