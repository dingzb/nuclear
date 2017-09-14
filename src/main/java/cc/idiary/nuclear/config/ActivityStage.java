package cc.idiary.nuclear.config;

/**
 * 活动阶段枚举
 * 每个阶段对应一个int值，现阶段按10递增，方便以后扩充。
 */
public enum ActivityStage {

    /**
     * 评审活动启动
     */
    START(0),
    /**
     * 提交（申报、推荐）
     */
    COMMIT(10),
    /**
     * 形审
     */
    CHECK(20),
    /**
     * 网络评审
     */
    INTERNET(30),
    /**
     * 分组初评
     */
    SESSION(40),
    /**
     * 评委会终评
     */
    FINAL(50),

    /**
     * 奖项公示
     */
    PUBLICITY(60),
    /**
     * 项目终止
     */
    FINISH(70);

    private int value;

    ActivityStage(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        switch (value) {
            case 0:
                return "开始";
            case 10:
                return "项目提交";
            case 20:
                return "项目审查";
            case 30:
                return "网络评审";
            case 40:
                return "分组初评";
            case 50:
                return "评委会终评";
            case 60:
                return "结束";
            default:
                return "";
        }
    }
}
