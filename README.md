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

## 数据库初始化（必须）

**必须使用 `sql/wgcloud-schema-full.sql` 初始化数据库**（约 45+ 张表，含 `DCE_INFO` 等全功能表）。

- 不要使用 `sql/wgcloud-MySQL.sql`：那是 2021 年旧版导出，**只有 17 张表**，缺 `DCE_INFO` 等表，会导致登录页品牌名、PING/SNMP 等功能异常。
- `wgcloud-schema-full.sql` 使用 `CREATE TABLE IF NOT EXISTS`，可重复执行。

```bash
mysql -uroot -p -e "CREATE DATABASE IF NOT EXISTS wgcloud DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;"
mysql -uroot -p wgcloud < sql/wgcloud-schema-full.sql
# 可选：邮件密码字段加长补丁
mysql -uroot -p wgcloud < sql/patches/01-mail-set-from-pwd-100.sql
```

## 构建

```bash
mvn -DskipTests clean package
# 产出 target/wgcloud-server-release.jar
```

## 部署

1. **先按上一节用 `sql/wgcloud-schema-full.sql` 初始化数据库**（不要用 `wgcloud-MySQL.sql`）
2. 把打出来的 `wgcloud-server-release.jar` 放到 `server/` 目录
3. 配置 `server/config/application.yml`（可参考 `src/main/resources/application.yml.example`）
4. 把 `agent-linux-amd64-v3.6.8.tar.gz` 解压放到 `agent/wgcloud-agent-pack/`
5. 修改 `agent/wgcloud-agent-pack/config/application.properties`：
   - `serverUrl=http://<server-ip>:9999`
   - `bindIp=<本机 ip>`（防被自动识别成 docker0 网卡 IP）
   - `wgToken=<与 server 一致>`
6. 启动:
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
