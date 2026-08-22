package org.red.minecraft.dellarte.library.data;

import org.red.minecraft.dellarte.library.util.A_DataMap;

import java.util.Set;

/**
 * 각 플러그인:타입 별로 데이터를 담고 있는 클래스
 * 코메디안 델라테에서 지원하는 world,player,entity는
 * CommediaDellarte:world, CommediaDellare:player, CommediaDellarte:entity
 * 형식으로 저장이 되고 있다
 * 
 * CommediaDellarte config에서 사전 설정을 통해 제작된다
 * 해당 IDataAdapter를 등록하여 사용할땐 파라미터가 없는 생성자를 하나 구현해야한다
 * 클래스 설정은 init함수를 통하여 작동한다
 */
public interface IDataAdapter {

    A_DataMap loadDataMap(String var1);

    void saveDataMap(String var1, A_DataMap var2);

    boolean containDataMap(String var1);

    void deleteDataMap(String var1);

    Set<String> loadAllKey();
}
