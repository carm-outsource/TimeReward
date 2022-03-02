package cc.carm.plugin.timereward.hooker;

import cc.carm.lib.githubreleases4j.GithubReleases4J;
import cc.carm.plugin.timereward.Main;

public class GHUpdateChecker {

    private final String owner;
    private final String repo;

    public GHUpdateChecker(String owner, String repo) {
        this.owner = owner;
        this.repo = repo;
    }

    public void checkUpdate(String currentVersion) {
        Integer behindVersions = GithubReleases4J.getVersionBehind(owner, repo, currentVersion);
        String downloadURL = GithubReleases4J.getReleasesURL(owner, repo);
        if (behindVersions == null) {
            Main.severe("检查更新失败，请您定期查看插件是否更新，避免安全问题。");
            Main.severe("下载地址 " + downloadURL);
        } else if (behindVersions == 0) {
            Main.info("检查完成，当前已是最新版本。");
        } else if (behindVersions > 0) {
            Main.info("发现新版本! 目前已落后 " + behindVersions + " 个版本。");
            Main.info("最新版下载地址 " + downloadURL);
        } else {
            Main.severe("检查更新失败! 当前版本未知，请您使用原生版本以避免安全问题。");
            Main.severe("最新版下载地址 " + downloadURL);
        }
    }


}
