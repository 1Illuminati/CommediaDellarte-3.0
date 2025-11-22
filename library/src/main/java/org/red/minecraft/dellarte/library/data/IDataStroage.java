package org.red.minecraft.dellarte.library.data;

import java.util.UUID;

import org.bukkit.Keyed;
import org.red.minecraft.dellarte.library.util.A_DataMap;
import org.red.minecraft.dellarte.library.util.CoolTimeMap;

/**
 * 각 플러그인:타입 별로 데이터를 담고 있는 클래스
 * 코메디안 델라테에서 지원하는 world,player,entity는
 * CommediaDellarte:world, CommediaDellare:player, CommediaDellarte:entity
 * 형식으로 저장이 되고 있다
 * 
 * CommediaDellarte config에서 사전 설정을 통해 제작된다
 */
public interface IDataStroage extends Keyed {

    /**
     * 데이터 맵 불러오기
     */
    A_DataMap getDataMap(UUID key);

    /**
     * 쿨타임 맵 불러오기
     */
    CoolTimeMap getCoolTimeMap(UUID key);

    /**
     * 현재 이 데이터가 로드가 완료되어 데이터가 저장상태로 존재하는게 아닌 메모리상에 존재하는지 확인할때 사용
     */
    boolean loadedData(UUID key);

    /**
     * 저장된 데이터가 존재하는지 확인할때 사용
     */
    boolean containData(UUID key);

    /**
     * 데이터 저장
     */
    void saveData(UUID key);

    /**
     * 데이터 로드
     */
    void loadData(UUID key);

    /**
     * 데이터 삭제
     */
    void deleteData(UUID key);

    /**
     * 모든 데이터 저장
     */
    void saveAll();
}
