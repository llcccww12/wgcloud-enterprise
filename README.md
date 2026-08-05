# wgcloud 企业版二开源码

## 用途

本仓库是 wgcloud 监控系统的企业版源码二开版本，对官方包做了少量调整。  
下游使用方可以基于本仓库 `mvn package` 后产出可执行的 `wgcloud-server-release.jar`。

## 相对官方源码的改动点

### 1. server 启动时自计算 jar md5（替代 `wgcloud-daemon-release`）

- **问题**: 官方版本的"防篡改校验"依赖一个独立守护进程 `wgcloud-daemon-release`（监听 9997），
  agent 会从该守护进程拉取 server.jar 的 md5 比对，本仓库部署时通常未启动 daemon，会反复
  出现 `防篡改校验失败`，累计 10 次后停止上报数据。
- **做法**:
  - `src/main/java/com/wgcloud/common/ApplicationStartListener.java` 启动钩子里
    计算 `h.getSource()`（即 server.jar 自身）md5 写入 `StaticKeys.WGCLOUD_SERVER_RELEASE_MD5STR`
  - `src/main/java/com/wgcloud/controller/LicenseController.java#getDaemon()`
    直接返回该 md5 给 agent，不再访问 `daemonUrl`

### 2. 数据库 schema 补丁

- `sql/patches/01-mail-set-from-pwd-100.sql` 将 `MAIL_SET.FROM_PWD` 从 `CHAR(30)`
  扩为 `VARCHAR(100)`，以适配部分邮箱厂商生成的较长授权码 / 应用专用密码。
- **官方 jar 里这是数据截断错误**：
  `Data too long for column 'FROM_PWD' at row 1`

## 构建

```bash
mvn -DskipTests clean package
# 产出 target/wgcloud-server-release.jar
```

## 部署

1. 把打出来的 `wgcloud-server-release.jar` 放到 `server/` 目录
2. 把 `agent-linux-amd64-v3.6.8.tar.gz` 解压放到 `agent/wgcloud-agent-pack/`
3. 应用 schema 补丁:
   ```sql
   mysql -uroot -p wgcloud < sql/patches/01-mail-set-from-pwd-100.sql
   ```
4. 修改 `agent/wgcloud-agent-pack/config/application.properties`：
   - `serverUrl=http://<server-ip>:9999`
   - `bindIp=<本机 ip>`（防被自动识别成 docker0 网卡 IP）
   - `wgToken=<与 server 一致>`
5. 启动:
   ```bash
   nohup java -Xms512m -Xmx1024m -jar server/wgcloud-server-release.jar \
     --spring.config.location=server/config/application.yml &
   nohup ./agent/wgcloud-agent-pack/wgcloud-agent-release &
   ```

## 版本基线

- 官方源码版本: `v3.6.8`（基于 wg-server-full-20260622.tar.gz）
- Spring Boot 2.7.18 / Java 11
- 数据库: MySQL / MariaDB

## 已知限制

- **UFM 监控**仍依赖外部 `https://10.33.1.21/ufmRest`，网络不通则报错刷屏；
  在 `src/main/java/com/wgcloud/util/UfmUtil.java` 里改 `UFM_HOST`。
- **Docker 全量监控** 需 agent 运行在能 mount 到宿主机 `/var/run/docker.sock` 的环境，
  在 LXC 容器里默认无效。
