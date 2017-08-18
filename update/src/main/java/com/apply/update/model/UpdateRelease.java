package com.apply.update.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/1.
 */

public class UpdateRelease implements Serializable {

    /**
     * autoInstallAble : 0
     * code : 2
     * content : 测试bee news 更新
     * down_url : http://180.97.80.139:8080/UpgradeCenter/file/BeeNews_v1.1_2_offical.apk
     * hotfix : {"code":2,"down_url":"http://180.97.80.139:8080/UpgradeCenter/file/patch_signed_7zip.apk","plugin_id":1,"upgrade_type":1,"version":"1.0"}
     * pkg : com.bee.news
     * plugin : {"code":1,"down_url":"http://180.97.80.139:8080/UpgradeCenter/file/patch_signed_7zip.apk","plugin_id":3,"upgrade_type":2,"version":"1.1"}
     * popAble : 0
     * project : offical
     * upgrade_id : 37
     * useAble : 0
     * version : 1.1
     */

    private int autoInstallAble;
    private int code;
    private String content;
    private String down_url;
    private HotfixBean hotfix;
    private String pkg;
    private PluginBean plugin;
    private int popAble;
    private String project;
    private int upgrade_id;
    private int useAble;
    private String version;

    public int getAutoInstallAble() {
        return autoInstallAble;
    }

    public void setAutoInstallAble(int autoInstallAble) {
        this.autoInstallAble = autoInstallAble;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDown_url() {
        return down_url;
    }

    public void setDown_url(String down_url) {
        this.down_url = down_url;
    }

    public HotfixBean getHotfix() {
        return hotfix;
    }

    public void setHotfix(HotfixBean hotfix) {
        this.hotfix = hotfix;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public PluginBean getPlugin() {
        return plugin;
    }

    public void setPlugin(PluginBean plugin) {
        this.plugin = plugin;
    }

    public int getPopAble() {
        return popAble;
    }

    public void setPopAble(int popAble) {
        this.popAble = popAble;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public int getUpgrade_id() {
        return upgrade_id;
    }

    public void setUpgrade_id(int upgrade_id) {
        this.upgrade_id = upgrade_id;
    }

    public int getUseAble() {
        return useAble;
    }

    public void setUseAble(int useAble) {
        this.useAble = useAble;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static class HotfixBean {
        /**
         * code : 2
         * down_url : http://180.97.80.139:8080/UpgradeCenter/file/patch_signed_7zip.apk
         * plugin_id : 1
         * upgrade_type : 1
         * version : 1.0
         */

        private int code;
        private String down_url;
        private int plugin_id;
        private int upgrade_type;
        private String version;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDown_url() {
            return down_url;
        }

        public void setDown_url(String down_url) {
            this.down_url = down_url;
        }

        public int getPlugin_id() {
            return plugin_id;
        }

        public void setPlugin_id(int plugin_id) {
            this.plugin_id = plugin_id;
        }

        public int getUpgrade_type() {
            return upgrade_type;
        }

        public void setUpgrade_type(int upgrade_type) {
            this.upgrade_type = upgrade_type;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

    public static class PluginBean {
        /**
         * code : 1
         * down_url : http://180.97.80.139:8080/UpgradeCenter/file/patch_signed_7zip.apk
         * plugin_id : 3
         * upgrade_type : 2
         * version : 1.1
         */

        private int code;
        private String down_url;
        private int plugin_id;
        private int upgrade_type;
        private String version;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDown_url() {
            return down_url;
        }

        public void setDown_url(String down_url) {
            this.down_url = down_url;
        }

        public int getPlugin_id() {
            return plugin_id;
        }

        public void setPlugin_id(int plugin_id) {
            this.plugin_id = plugin_id;
        }

        public int getUpgrade_type() {
            return upgrade_type;
        }

        public void setUpgrade_type(int upgrade_type) {
            this.upgrade_type = upgrade_type;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
