package org.red.minecraft.dellarte.library.data;

import org.bukkit.Keyed;
import org.red.minecraft.dellarte.library.util.A_DataMap;

public interface IDataStorage extends Keyed {

    SaveConfig config();

    /**
     * 데이터 맵 불러오기
     */
    A_DataMap getDataMap(String key);

    /**
     * 현재 이 데이터가 로드가 완료되어 데이터가 저장상태로 존재하는게 아닌 메모리상에 존재하는지 확인할때 사용
     */
    boolean loadedData(String key);

    /**
     * 저장된 데이터가 존재하는지 확인할때 사용
     */
    boolean containData(String key);

    /**
     * 데이터 저장
     */
    void saveData(String key);

    /**
     * 데이터 로드
     */
    void loadData(String key);

    /**
     * 데이터 삭제
     */
    void deleteData(String key);

    /**
     * 모든 데이터 저장
     */
    void saveAll();

    /**
     * 모든 데이터 로드
     */
    void loadAll();
}
