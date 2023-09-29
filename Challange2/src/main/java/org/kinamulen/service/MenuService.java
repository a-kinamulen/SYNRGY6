package org.kinamulen.service;

import org.kinamulen.model.Data;
import org.kinamulen.model.Menu;
import org.kinamulen.model.Variant;

import java.util.Map;

import static org.kinamulen.model.Data.varLauk;
import static org.kinamulen.model.Data.varSpicy;

public class MenuService {
    public void initiateData(){
        varLauk.put(1,new Variant("Ayam+Sosis",0));
        varLauk.put(2,new Variant("Kambing",5000));
        varLauk.put(3,new Variant("Seafood",1000));
        varLauk.put(4,new Variant("Rahasia",7500));

        Data.varSpicy.put(1,new Variant("Lv.0 (npc)",0));
        Data.varSpicy.put(2,new Variant("Lv.2 (noob)",0));
        Data.varSpicy.put(3,new Variant("Lv.10 (sepuh)",1000));

        Data.menus.put(1,new Menu("Nasi Goreng",11_000,varLauk,varSpicy));
        Data.menus.put(2,new Menu("Mie Goreng",10_500,varLauk,varSpicy));
        Data.menus.put(3,new Menu("Nasi+Ayam",16_000,null,varSpicy));
        Data.menus.put(4,new Menu("Teh Manis",3_000,null,null));
        Data.menus.put(5,new Menu("Es Jeruk",5_000,null,null));
    }
    public Map<Integer,Menu> getMenus() {
        return Data.menus;
    }
    public Menu getMenus(Integer key) {
        return Data.menus.get(key);
    }
}
