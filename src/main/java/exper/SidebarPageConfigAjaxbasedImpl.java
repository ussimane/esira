/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Ussimane
 */
public class SidebarPageConfigAjaxbasedImpl implements SidebarPageConfig {

    HashMap<String, SidebarPage> pageMap = new LinkedHashMap<String, SidebarPage>();

    public SidebarPageConfigAjaxbasedImpl() {
        pageMap.put("fn3", new SidebarPage("fn3", "Todo List (MVC)", "/imgs/logo.png", "/exper/todolist-mvc.zul"));
        pageMap.put("zk", new SidebarPage("zk", "www.zkoss.org", "/imgs/site.png", "http://www.zkoss.org/"));
        pageMap.put("demo", new SidebarPage("demo", "ZK Demo", "/imgs/demo.png", "http://www.zkoss.org/zkdemo"));
        pageMap.put("devref", new SidebarPage("devref", "ZK Developer Reference", "/imgs/doc.png", "http://books.zkoss.org/wiki/ZK_Developer's_Reference"));

        pageMap.put("fn1", new SidebarPage("fn1", "Profile (MVC)", "/imgs/fn.png", "/chapter3/profile-mvc.zul"));
        pageMap.put("fn2", new SidebarPage("fn2", "Profile (MVVM)", "/imgs/fn.png", "/chapter3/profile-mvvm.zul"));
        pageMap.put("fn3", new SidebarPage("fn3", "Todo List (MVC)", "/imgs/fn.png", "/exper/todolist-mvc.zul"));
        pageMap.put("fn4", new SidebarPage("fn4", "Todo List (MVVM)", "/imgs/fn.png", "/chapter4/todolist-mvvm.zul"));
    }

    @Override
    public List<SidebarPage> getPages() {
        return new ArrayList<SidebarPage>(pageMap.values());
    }

    @Override
    public SidebarPage getPage(String name) {
        return pageMap.get(name);
    }

}
