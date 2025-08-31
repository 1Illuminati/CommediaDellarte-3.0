package org.red.addon.level;

public interface A_LevelInfo {
    /**
     * 각 레벨의 최종 레벨을 반환
     * -1의 경우 최대 레벨이 없음을 의미
     *
     * @return 최고 레벨의 값
     */
    int getMaxLevel();

    /**
     * 레벨업이 가능한 상태인지 확인
     *
     * @param level 레벨
     * @return 레벨업이 가능하면 true, 그렇지 않으면 false
     */
    boolean canLevelUp(A_Level level);
}
