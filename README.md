# 智能 AI 旅游推荐平台开源文档

>作者：[程序员小白条](https://luoye6.github.io/)
>
>[Gitee 主页](https://gitee.com/falle22222n-leaves)
>
>[GitHub 主页](https://github.com/luoye6)

## ☀️新手必读

+ 本项目支持 Knife4j 在线接口调用（需在全局参数中添加有效 Token）
+ [项目演示视频](https://www.bilibili.com/video/BV1dv97YnETj/?spm_id_from=333.999.0.0&vd_source=e40a9464b71684502f289cff3f51514f)
+ 如果项目对您有所帮助，可以Star⭐一下，受到鼓励的我会继续加油。
+ [项目在线演示地址](https://www.xiaobaitiao.icu)
+ [项目前端地址](https://gitee.com/falle22222n-leaves/vue3_tourism_frontend)
+ [项目后端地址](https://gitee.com/falle22222n-leaves/vue3_tourism_backend)

[![star](https://gitee.com/falle22222n-leaves/vue3_tourism_backend/badge/star.svg?theme=dark)](https://gitee.com/falle22222n-leaves/vue_-book-manage-system)  [![gitee](https://badgen.net/badge/gitee/falle22222n-leaves/red)](https://gitee.com/falle22222n-leaves)  [![github](https://badgen.net/badge/github/github?icon)](https://github.com/luoye6)

## ☀️个人介绍

<div style="display: flex; justify-content: center; align-items: center; gap: 20px;">
    <img src="https://pic.yupi.icu/5563/202403021406388.png" alt="图片1" style="width: 200px; height: auto;">
    <img src="https://pic.yupi.icu/5563/202403021406360.png" alt="图片2" style="width: 200px; height: auto;">
</div>

## ☀️项目介绍

**智能 AI 旅游推荐平台**（Intelligent AI Travel Recommendation Platform）是一个利用 AI 模型和数据分析为用户提供个性化旅游路线推荐、景点评分、旅游攻略分享等功能的综合性系统。该系统融合了 AI 旅游顾问、图表数据分析、游客留言、旅游日历、景点浏览、攻略收藏、弹幕互动等多种功能，项目功能总计 20+，并支持拓展会员功能以实现商业化处理。

> Ps：如果你想要一个既简单又新颖的旅游规划工具，那么这个项目将会是不错的选择~

![](https://pic.yupi.icu/5563/202503051329649.png)

![](https://pic.yupi.icu/5563/202503051332555.png)

## ☀️功能和特性

### 用户功能

1）**欢迎页**：介绍项目的功能、亮点以及如何使用。

2）**主页**：使用 Swiper 轮播图展示风景图片，吸引用户眼球。

3）**公告浏览**：查看近期管理员发布的公告，获取最新平台动态。

4）**游客留言**：支持弹幕留言功能，用户可以进行添加，并使用弹幕玩法（继续、暂停、显示、隐藏、加速、减速）。

5）**旅游攻略分享**：用户可以分享旅游攻略，**使用 Markdown 编辑器**。可以浏览他人分享的旅游攻略（分页，支持模糊搜索帖子标题）。

6）**AI 对话**：用户可以与 **AI 旅游顾问**进行聊天，AI 会根据用户的偏好推荐旅游路线，并采用协同过滤推荐算法提升推荐精准度。

7）**景点列表**：展示所有景点，支持评分、浏览量、收藏量等功能，帮助用户快速了解热门景点。

8）**景点路线**：展示景点的推荐旅游路线，帮助用户规划行程。

9）**景点门票**：展示景点门票信息，支持在线预订功能。

10）**个人主页**：展示个人旅游日历（直接用 0 和 1 展示是否绿色出行），个人订单详情查看，个人评论查看，收藏的旅游攻略查看，个人详情信息查看。

11）**注册功能**：用户输入账号、密码，重复确认密码后完成注册。

### 系统管理员功能

1）**用户管理**：编辑用户、查看用户、删除用户，支持分页和模糊查询（用户名和用户简介）。

2）**公告管理**：发布新公告、修改公告、删除公告，支持分页。

3）**旅游攻略管理**：添加新攻略、修改攻略、删除攻略，支持分页和模糊查询（帖子标题、内容、标签、用户 ID）。

4）**AI 对话管理**：删除用户与 AI 的对话记录，支持分页和模糊查询（用户 ID、用户输入、AI 生成内容）。

5）**景点管理**：添加新景点、修改景点、删除景点，支持分页和模糊查询（景点名、景点描述）。

6）**景点路线管理**：添加新路线、修改路线、删除路线，支持分页和模糊查询（路线 ID、景点 ID、路线标题、难度）。

7）**景点评分管理**：查看、修改景点评分，支持分页和模糊查询（景点 ID、用户 ID、评分）。

8）**景点门票管理**：添加新门票、修改门票、删除门票，支持分页和模糊查询（门票 ID、景点 ID、门票价格）。

9）**用户订单管理**：查看、修改、删除用户订单，支持分页和模糊查询（订单 ID、用户 ID、订单状态）。

**欢迎页 和 个人主页 同用户一致。**

### 特性（亮点）

1）**前后端分离架构**：本项目采用前后端分离的模式，前端构建页面，后端提供数据接口，前端调用后端接口获取数据并重新渲染页面。

2）**Token 认证机制**：前端在 Authorization 字段提供 Token 令牌，API 认证使用 Token 认证，使用 HTTP Status Code 表示状态，数据返回格式使用 JSON。

3）**跨域支持与权限校验**：后端已开启 CORS 跨域支持，采用权限拦截器进行权限校验，并检查登录情况。

4）**全局异常处理**：添加全局异常处理机制，捕获异常，增强系统健壮性。

5）**数据可视化**：前端使用 Echarts 可视化库实现景点热度分析图表（折线图、饼图），并通过 Loading 配置提升加载体验。

6）**接口文档自动化**：引入 knife4j 依赖，使用 Swagger + Knife4j 自动生成 OpenAPI 规范的接口文档，前端可以直接通过 Package.json 文件中的 openapi 命令生成前端接口请求代码和类型代码（高效）。

7）**组件库与权限管理**：使用 ElementUI PLUS 组件库进行前端界面搭建，快速实现页面生成，并实现前后端统一权限管理、多环境切换等功能。

8）**灵活查询与代码生成**：基于 MyBatis Plus 框架的 QueryWrapper 实现对 MySQL 数据库的灵活查询，并配合 MyBatisX 插件自动生成后端 CRUD 基础代码，减少重复工作。

9）**代码生成器**：后端 FreeMarker 模版自带代码生成器，一键生成 Controller、Service、DTO、VO 等功能，稍加修改即可实现传统增删改查。

10）**性能优化**：前端路由懒加载、CDN 静态资源缓存优化、图片懒加载效果，提升用户体验。

11）**AI 旅游顾问**：AI 充当旅游顾问，AI 模型可以随时切换版本，支持接入其他 AI 模型接口，直接下载官方 Java 工具类即可集成到本项目。

12）**旅游日历与互动功能**：引入旅游日历记录功能，支持绿色出行标记（0 和 1 表示），并集成攻略评论、收藏、浏览量等功能，与传统增删改查项目有显著区别。

13）**样式美观与适配**：本项目注重样式美观，部分功能在手机端也有良好的适配效果，并支持全局样式一键切换。

## ☀️运行方式

### 2 分钟快速上手使用项目

1）找到 SpringBoot 启动类，点击运行

![](https://pic.yupi.icu/5563/202503051336654.png)

2）打开 Knife4J 在线接口调用地址，先需要登录获取 Token，然后在全局参数上加上即可。

![](https://pic.yupi.icu/5563/202503051338244.png)

3）前端输入表单内容后点击登录即可成功，开始愉快使用功能~

![](https://pic.yupi.icu/5563/202503051339933.png)

![](https://pic.yupi.icu/5563/202503051339634.png)

## ☀️部署方式

### 前置条件

**前端**

软件：Vscode 或者 Webstorm（推荐）

环境：Node 版本 16 或者 18（推荐） **注：千万别选 18 以上的版本！**（版本不同，大概率导致项目报错）

**后端**

软件：Eclipse 或者 IDEA（推荐）

环境：MySQL 5.7 或者 8.0（推荐）Redis 3.2或以上（必须，选择 Windows 版本，看个人电脑）

### 前端部署

1）点击克隆/下载项目，会使用 Git 进行版本控制的，推荐 Git Clone，不会的小伙伴可以选择下载一个 Zip 压缩包，然后解压到自己电脑的 D 盘，推荐直接 Star，后续直接向我拿数据库模拟文件和 API 接口文档。

2）利用 Vscode 或者 Webstorm 打开前端页面，配置 Configuration。配置 Node 环境和包管理工具即可，我这边选择的包管理工具是 Npm，其他包管理工具如：Yarn、Cnpm、Pnpm 皆可。 **注：注意更改 Npm 的镜像地址为淘宝的新镜像地址，否则会出现 Npm Install 一直卡进度条的情况。**

3）直接点击 dev 的运行，或者打开控制台，输入 npm run serve 即可成功启动前端项目。

```shell
npm config set registry https://registry.npmmirror.com/
```

![](https://pic.yupi.icu/5563/202503051340691.png)

![](https://pic.yupi.icu/5563/202403041926931.png)

![](https://pic.yupi.icu/5563/202403041926639.png)

4）将图片链接进行自定义切换，可以切换为你自己的图床的图片链接，比如七牛云、GitHub 等，也可以寻找在线图片，复制百度文库图片链接（多试几次，有些图片有防盗链）。

![](https://pic.yupi.icu/5563/202503051342549.png)

### 后端部署

1）点击克隆/下载项目，会使用 Git 进行版本控制的，推荐 Git Clone，不会的小伙伴可以选择下载一个 Zip 压缩包，然后解压到自己电脑的 D 盘，建议收藏项目，后续方便获取相关版本更新信息，也可以 Fork 到自己的本地仓库，拷贝副本。

![](https://pic.yupi.icu/5563/202503051342345.png)

2）领取数据库模拟文件后，利用 Navicat 或者 SQLYog 等软件导入数据库文件，记得先建立一个名为 tourism 的数据库，然后右键点击运行 SQL 文件即可，运行成功，无报错后，重新打开数据库，检查是否有数据，如果有数据，表明导入成功。

![](https://pic.yupi.icu/5563/202503051343989.png)

3）用 IDEA 打开后端项目，找到 application-dev.yml 文件，修改其中的 MySQL 配置，保证用户名和密码正确，**注：密码不能以数字 0 开头，本机密码简单为主，不是远程服务器密码！**

![](https://pic.yupi.icu/5563/202503051344518.png)

4）导入 Maven 依赖，注意看自己的 Maven 版本是否正确，建议选择跟我一样的，3.8以上的版本，发现依赖导入很慢，是因为没有配置国内镜像，默认连接的是国外服务器，因此阿里云镜像配置可以看这篇博客。[CSDN Maven 配置教程](https://blog.csdn.net/lianghecai52171314/article/details/102625184?ops_request_misc=&request_id=&biz_id=102&utm_term=Maven)

![](https://pic.yupi.icu/5563/202503051345589.png)

5）找到 SpringBoot 启动类，我建议用 Debug 模式启动项目，更好排查错误。

![](https://pic.yupi.icu/5563/202503051346329.png)

6）如果遇到错误，大概率可能是 JDK 版本问题，我项目用的是 JDK 8，建议选择与我相同版本。

![](https://pic.yupi.icu/5563/202403041926752.png)

![](https://pic.yupi.icu/5563/202403041926887.png)

7）成功启动项目效果展示如下，注意端口号占用等情况，可以直接用 TaskKill /PID ，杀死原有进程即可，具体命令可以百度哦。

![](https://pic.yupi.icu/5563/202503051346167.png)

### 前后端联调

1）如果需要修改端口和前缀（比如/api），需要同时修改前端和后端。

![](https://pic.yupi.icu/5563/202503051348019.png)

![](https://pic.yupi.icu/5563/202503051348178.png)

## ☀️技术选型

### 前端

| 技术           | 作用              | 版本   |
| :------------- | :---------------- | :----- |
| Vue            | 提供前端交互      | 3.2.13 |
| Vue Router     | 提供前端路由功能  | 4.0.3  |
| Pinia          | 状态管理          | 2.1.4  |
| Axios          | HTTP 请求库       | 1.4.0  |
| ECharts        | 数据可视化图表库  | 5.4.2  |
| MD Editor V3   | Markdown 编辑器   | 4.8.1  |
| NProgress      | 页面加载进度条    | 0.2.0  |
| QRCode Vue3    | 生成二维码        | 1.7.1  |
| Vue Clipboard3 | 剪贴板功能        | 2.0.0  |
| Vue3 Danmaku   | 弹幕功能          | 1.6.1  |
| Element Plus   | UI 组件库         | 2.9.0  |
| TypeScript     | JavaScript 的超集 | 4.5.5  |
| ESLint         | 代码检查工具      | 7.32.0 |
| Prettier       | 代码格式化工具    | 2.4.1  |
| Sass           | CSS 预处理器      | 1.63.6 |
| mitt           | 全局事件总线      | 3.0.1  |

### 后端

| 技术          | 作用后端框架     | 版本     |
| ------------- | ---------------- | -------- |
| SpringBoot    | 后端框架         | 2.7.2    |
| MyBatis Plus  | ORM 框架         | 3.5.2    |
| Redis         | 缓存数据库       | -        |
| Elasticsearch | 搜索引擎         | -        |
| Wx Java Mp    | 微信公众平台开发 | 4.4.0    |
| Knife4j       | Swagger 增强工具 | 4.4.0    |
| QCloud COS    | 腾讯云对象存储   | 5.6.89   |
| EasyExcel     | Excel 操作库     | 3.1.1    |
| Hutool        | Java 工具库      | 5.8.8    |
| Lombok        | 简化 Java 代码   | -        |
| JWT           | JSON Web Token   | 0.9.0    |
| OkHttp        | HTTP 客户端      | 4.10.0   |
| Guava         | Google 工具库    | 30.1-jre |
| Jsoup         | HTML 解析库      | 1.15.3   |
| Gson          | JSON 解析库      | 2.9.0    |
| Fastjson      | JSON 解析库      | 2.0.53   |

## ☀️架构

![](https://pic.yupi.icu/5563/202503051449243.png)

## ☀️核心设计

### AI 旅游推荐官对话功能

```java
/**
     * 创建用户对话表
     *
     * @param userAiMessageAddRequest
     * @param request
     * @return UserAiMessage
     */
    @PostMapping("/add")
    public BaseResponse<UserAiMessage> addUserAiMessage(@RequestBody UserAiMessageAddRequest userAiMessageAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userAiMessageAddRequest == null, ErrorCode.PARAMS_ERROR);
        String userInputText = userAiMessageAddRequest.getUserInputText();
        if (WordUtils.containsForbiddenWords(userInputText)){
            ThrowUtils.throwIf(WordUtils.containsForbiddenWords(userInputText), ErrorCode.WORD_FORBIDDEN_ERROR, "包含违禁词");
        }
        UserAiMessage userAiMessage = new UserAiMessage();
        // 填充默认值
        User loginUser = userService.getLoginUser(request);
        Integer aiRemainNumber = loginUser.getAiRemainNumber();
        // 检查用户余额是否足够，如果不足，直接返回错误信息
        ThrowUtils.throwIf(aiRemainNumber <= 0, ErrorCode.USER_BALANCE_NOT_ENOUGH);
        userAiMessage.setUserId(loginUser.getId());
        userAiMessage.setUserInputText(userInputText);
        StringBuilder presetInformation = new StringBuilder("你是一个旅游景点的推荐官，请根据用户的喜好信息，从数据库中选择某几个景点，给用户概述，并给他做一个路线的推荐。\n" +
                "以下是数据库里面包含的景点：");
        List<String> spotNameList = spotService.list().stream().map(Spot::getSpotName).collect(Collectors.toList());
        for (String s : spotNameList) {
            presetInformation.append(s).append(" ");
        }
        presetInformation.append("\n用户喜好信息如下：");
        List<SparkMessage> messages = new ArrayList<>();
        messages.add(SparkMessage.userContent(presetInformation+userInputText));
        String response = "";
        int timeout = 35; // 超时时间，单位为秒
        // 构造请求
        SparkRequest sparkRequest = SparkRequest.builder()
                // 模型回答的tokens的最大长度,非必传，默认为2048。
                // V1.5取值为[1,4096]
                // V2.0取值为[1,8192]
                // V3.0取值为[1,8192]
                .maxTokens(2048)
                .messages(messages)
                // 核采样阈值。用于决定结果随机性,取值越高随机性越强即相同的问题得到的不同答案的可能性越高 非必传,取值为[0,1],默认为0.5
                .temperature(0.2)
                .build();
        Future<String> future = threadPoolExecutor.submit(() -> {
            try {
                // 同步调用
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                SparkSyncChatResponse chatResponse = sparkClient.chatSync(sparkRequest);
                SparkTextUsage textUsage = chatResponse.getTextUsage();
                stopWatch.stop();
                long total = stopWatch.getTotal(TimeUnit.SECONDS);
                System.out.println("本次接口调用耗时:" + total + "秒");
                System.out.println("\n回答：" + chatResponse.getContent());
                System.out.println("\n提问tokens：" + textUsage.getPromptTokens()
                        + "，回答tokens：" + textUsage.getCompletionTokens()
                        + "，总消耗tokens：" + textUsage.getTotalTokens());
                return chatResponse.getContent();
//                return AlibabaAIModel.doChatWithHistory(stringBuilder.toString(),recentHistory);
            } catch (Exception exception) {
                throw new RuntimeException("遇到异常");
            }
        });
        try {
            response = future.get(timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.info("服务器接口调用超时");
        }
        System.out.println(response);
        userAiMessageAddRequest.setAiGenerateText(response);

        // 复制属性
        BeanUtils.copyProperties(userAiMessageAddRequest, userAiMessage);

        // 校验数据
        userAiMessageService.validUserAiMessage(userAiMessage, true);


        // 插入数据库
        boolean result = userAiMessageService.save(userAiMessage);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 更新用户 AI 调用余额
        loginUser.setAiRemainNumber(aiRemainNumber-1);
        boolean update = userService.updateById(loginUser);
        ThrowUtils.throwIf(!update, ErrorCode.OPERATION_ERROR);
        long newUserAiMessageId = userAiMessage.getId();
        UserAiMessage generateAnswer = userAiMessageService.getById(newUserAiMessageId);
        return ResultUtils.success(generateAnswer);
    }

```



### **代码解释**

1）**方法定义**：

- 方法名为 `addUserAiMessage`，用于处理用户提交的旅游偏好信息，并生成 AI 推荐的旅游路线。
- 使用 `@PostMapping("/add")` 注解，表示这是一个 POST 请求接口。
- 接收两个参数：
  - `UserAiMessageAddRequest userAiMessageAddRequest`：用户提交的请求体，包含用户输入的文本。
  - `HttpServletRequest request`：HTTP 请求对象，用于获取当前登录用户信息。

2）**参数校验**：

- 使用 `ThrowUtils.throwIf` 方法对请求参数进行校验：
  - 如果 `userAiMessageAddRequest` 为空，抛出 `PARAMS_ERROR` 异常。
  - 如果用户输入的文本包含违禁词，抛出 `WORD_FORBIDDEN_ERROR` 异常。

3）**用户余额检查**：

- 获取当前登录用户的 AI 调用余额 `aiRemainNumber`。
- 如果余额不足（`aiRemainNumber <= 0`），抛出 `USER_BALANCE_NOT_ENOUGH` 异常。

4）**AI 推荐生成**：

- 构造 AI 模型的输入信息：
  - 预设提示信息：告诉 AI 模型它是一个旅游推荐官，并列出数据库中所有景点的名称。
  - 将用户输入的偏好信息附加到预设提示信息中。
- 使用 `SparkRequest` 构造 AI 模型的请求，设置最大 tokens、消息内容和温度参数。
- 使用线程池 `threadPoolExecutor` 异步调用 AI 模型，设置超时时间为 35 秒。
- 如果调用超时，记录日志并返回空响应。

5）**保存结果到数据库**：

- 将 AI 生成的推荐结果保存到 `userAiMessage` 对象中。
- 使用 `BeanUtils.copyProperties` 复制请求参数到 `userAiMessage` 对象。
- 调用 `userAiMessageService.validUserAiMessage` 对数据进行校验。
- 将 `userAiMessage` 插入数据库，并更新用户的 AI 调用余额。

6）**返回结果**：

- 返回生成的 AI 推荐结果，封装在 `BaseResponse` 中。

### **注意事项**

1）**参数校验**：

- 确保 `userAiMessageAddRequest` 和 `userInputText` 不为空，避免空指针异常。
- 使用违禁词过滤功能，防止用户输入不当内容。

2）**用户余额管理**：

- 在调用 AI 模型之前，检查用户的 AI 调用余额，避免无效调用。
- 调用成功后，及时更新用户的余额信息。

3）**AI 模型调用**：

- 使用异步调用（`Future`）和超时机制，避免因 AI 模型响应过慢导致接口阻塞。
- 设置合理的超时时间（如 35 秒），并根据实际需求调整。

4）**线程池管理**：

- 确保线程池 `threadPoolExecutor` 已正确配置，避免资源耗尽或线程泄漏。
- 在异步任务中捕获异常，避免因异常导致线程池崩溃。

5）**数据校验与保存**：

- 在保存数据之前，调用 `validUserAiMessage` 方法进行校验，确保数据合法性。
- 使用事务管理（如果需要），确保数据插入和用户余额更新的原子性。

6）**日志记录**：

- 在关键步骤（如 AI 调用、数据库操作）中添加日志记录，便于排查问题。
- 记录 AI 调用的耗时和 tokens 使用情况，便于性能分析和优化。

7）**性能优化**：

- 如果 AI 调用频率较高，考虑使用缓存机制（如 Redis）存储常用推荐结果，减少重复调用。
- 对数据库查询（如 `spotService.list()`）进行优化，避免全表扫描。

8）**异常处理**：

- 在异步任务中捕获所有异常，避免因未捕获异常导致接口崩溃。
- 返回明确的错误码和错误信息，便于前端处理。

9）**安全性**：

- 确保用户输入的文本经过安全过滤，防止 SQL 注入或 XSS 攻击。
- 对敏感操作（如余额更新）进行权限校验，确保只有合法用户才能调用。

10）**扩展性**：
\- 如果需要支持多种 AI 模型，可以将 AI 调用逻辑抽象为独立的服务，便于切换和扩展。
\- 如果需要支持多语言推荐，可以在预设提示信息中增加语言参数。

## ☀️学完这个项目你能得到什么

1）简单地调用 AI 模型（讯飞星火）获取自定义文本内容，支持 Websocket 形式，也可以获取全部数据后返回。

2）简单的 JWT 权限校验 ，利用后端拦截器进行登录校验。

3）简单的增删改查系统，前后端是如何联调协作的。

4）前端路由懒加载、CDN 静态资源缓存优化、图片懒加载是如何实现的

5）利用自定义线程池和 FutureTask 进行超时请求处理。

6）利用 Google 的 GuavaRateLimiter 进行单体限流控制。

7）定时任务结合 Redis 做一个缓存预热，加快查询效率，提高用户体验。

8）在 Vue3 中使用 Markdown 编辑器和仅浏览模式、实现剪切板功能，类似与复制个人电话号码、使用弹幕插件，实现类似 B 站弹幕的功能。

9）实现牛客等论坛的评论区功能，并有良好的样式体验。

10）实现力扣等平台的旅游记录日历展示，给用户持续的旅游动力。

## ☀️项目简介

+ 主要使用 Vue3 和 SpringBoot2.7 实现
+ 项目权限控制分别为：用户（游客），系统管理员
+ 开发工具：IDEA 2023.3.3 (真不推荐用 eclipse 开发，IDEA 项目可以导出为 eclipse 项目，二者不影响，但需要自己学教程，注意 IDEA 版本最好不要低于 2021，不建议用中文哦！) 
+ [IDEA->Eclipse](https://blog.csdn.net/HD202202/article/details/128076400)
+ [Eclipse->IDEA](https://blog.csdn.net/q20010619/article/details/125096051)
+ 学校老师硬性要求软件的话，还是按要求来。可以先问一下是否可以选择其他软件开发（一般不会做限制，尤其毕设）。
+ 用户账号密码：  xiaobaitiao 12345678
+ 系统管理员账号密码:   xiaobai 12345678
+ 遇到交互功能错误，或者页面无法打开，请用开发者工具F12查看请求和响应状态码情况，小白可以有偿咨询，扫 QQ 或者 WX:xiaobaitiao_z。白天工作，晚上有空才能回答。⭐⭐⭐

## ☀️项目详细介绍（亮点)

- **前后端分离架构**：本项目采用前后端分离的模式，前端负责页面构建和用户交互，后端提供数据接口，前端通过调用后端接口获取数据并动态渲染页面，提升开发效率和系统可维护性。
- **跨域支持**：后端已开启 CORS 跨域支持，确保前端能够无缝调用后端接口，解决跨域问题。
- **Token 认证机制**：API 认证采用 Token 认证，前端在请求头 `Authorization` 字段中携带 Token 令牌，确保接口调用的安全性。
- **标准化数据交互**：使用 HTTP Status Code 表示接口状态，数据返回格式统一为 JSON，便于前后端协作和数据解析。
- **权限校验与登录检查**：后端采用权限拦截器进行权限校验，确保只有合法用户才能访问特定接口，并检查用户的登录状态，增强系统安全性。
- **全局异常处理**：添加全局异常处理机制，捕获系统异常并返回友好的错误信息，增强系统的健壮性和用户体验。
- **数据可视化**：前端使用 Echarts 可视化库实现景点热度分析图表（如折线图、饼图），并通过 Loading 配置优化加载体验，提升用户交互感受。
- **弹幕留言功能**：留言组件采用弹幕形式，贴合用户喜好，增加平台的趣味性和互动性。
- **接口文档自动化**：引入 knife4j 依赖，使用 Swagger + Knife4j 自动生成 OpenAPI 规范的接口文档，前端可以基于此文档使用插件自动生成接口请求代码，降低前后端协作成本。
- **高效前端开发**：使用 ElementUI 组件库进行前端界面搭建，快速实现页面生成，并支持前后端统一权限管理和多环境切换，提升开发效率。
- **灵活数据库查询**：基于 MyBatis Plus 框架的 QueryWrapper 实现对 MySQL 数据库的灵活查询，配合 MyBatisX 插件自动生成后端 CRUD 基础代码，减少重复开发工作。
- **性能优化**：前端采用路由懒加载、CDN 静态资源缓存优化、图片懒加载等技术，提升页面加载速度和用户体验。
- **AI 旅游推荐**：集成 AI 模型，根据用户偏好推荐个性化旅游路线，支持协同过滤算法，提升推荐精准度。
- **旅游日历功能**：为用户提供个人旅游日历，记录旅行足迹，支持绿色出行标记（0 和 1 表示），帮助用户规划和管理旅行。
- **攻略分享社区**：用户可以通过 Markdown 编辑器分享旅游攻略，浏览他人经验，并通过评论区互动交流，形成活跃的旅游社区。
- **景点门票预订**：支持在线查看景点门票信息并完成预订，提供一站式旅游服务。
- **多维度景点评分**：结合用户评分和 AI 分析，动态生成景点评分，帮助用户快速了解景点热度。
- **管理员便捷管理**：为管理员提供用户、景点、攻略、门票等内容的管理功能，支持分页和模糊查询，提升管理效率。
- **高扩展性**：支持接入更多 AI 模型（如 OpenAI GPT、百度文心一言等,自己下载其他 AI 模型官网工具类即可），并可通过拓展 OJ 和会员功能实现商业化处理。

### ⭐用户（游客）模块功能介绍

![](https://pic.yupi.icu/5563/202503051356875.png)

![](https://pic.yupi.icu/5563/202503051356744.png)

1）**欢迎页**：介绍项目亮点，展示用户功能和管理者功能，帮助用户快速了解平台的核心价值和使用方法。

2）**主页**：使用 Swiper 轮播图展示风景图片，吸引用户眼球，提升视觉体验。

3）**公告浏览**：用户可以查看管理员发布的公告，获取平台最新动态和重要通知。

4）**游客留言**：支持弹幕形式的留言功能，用户可以进行实时互动，增加平台的趣味性和活跃度。

5）**旅游攻略分享**：用户可以分享自己的旅游攻略，使用 Markdown 编辑器撰写内容，并浏览他人分享的攻略，支持模糊搜索和分页展示。

6）**AI 对话**：用户可以与 AI 旅游顾问进行对话，AI 根据用户偏好推荐个性化旅游路线，采用协同过滤算法提升推荐精准度。

7）**景点列表**：展示所有景点信息，支持评分、浏览量和收藏量等功能，帮助用户快速了解热门景点。

8）**景点路线**：提供景点的推荐旅游路线，帮助用户规划行程，提升旅行效率。

9）**景点门票**：展示景点门票信息，支持在线预订功能，为用户提供一站式服务。

10）**个人主页**：展示个人旅游日历（用 0 和 1 标记是否绿色出行），查看个人订单详情、评论记录、收藏的旅游攻略以及个人详情信息。

### ⭐系统管理员模块功能介绍

![](https://pic.yupi.icu/5563/202503051358251.png)

1）**欢迎页**：同用户，介绍项目亮点，展示管理员功能，帮助管理员快速了解平台的核心价值和使用方法。

2）**主页**：同用户，使用 Swiper 轮播图展示风景图片，提升视觉体验。

3）**用户管理**：管理员可以查看、编辑、删除用户信息，支持分页和模糊查询（如用户名和用户简介），确保用户数据的管理和维护。

4）**公告管理**：管理员可以发布、修改、删除公告，支持分页展示，确保平台信息的及时更新和传达。

5）**旅游攻略管理**：管理员可以查看、添加、修改、删除旅游攻略，支持分页和模糊查询（如帖子标题、内容、标签、用户 ID），确保攻略内容的质量和合规性。

6）**AI 对话管理**：管理员可以查看和删除用户与 AI 的对话记录，支持分页和模糊查询（如用户 ID、用户输入、AI 生成内容），确保对话数据的管理和隐私保护。

7）**景点管理**：管理员可以添加、修改、删除景点信息，支持分页和模糊查询（如景点名、景点描述），确保景点数据的准确性和完整性。

8）**景点路线管理**：管理员可以添加、修改、删除景点推荐路线，支持分页和模糊查询（如路线 ID、景点 ID、路线标题、难度），帮助用户更好地规划旅行。

9）**景点评分管理**：管理员可以查看和修改景点评分，支持分页和模糊查询（如景点 ID、用户 ID、评分），确保评分数据的公平性和真实性。

10）**景点门票管理**：管理员可以添加、修改、删除景点门票信息，支持分页和模糊查询（如门票 ID、景点 ID、门票价格），确保门票数据的准确性和实时性。

11）**用户订单管理**：管理员可以查看、修改、删除用户订单，支持分页和模糊查询（如订单 ID、用户 ID、订单状态），确保订单数据的管理和售后服务。

12）**个人主页**：同用户，展示管理员的个人信息、操作记录等内容，方便管理员查看和管理个人数据。

## ☀️数据库表设计

### **users 表**

| 列名           | 数据类型以及长度 | 备注                            |
| :------------- | :--------------- | :------------------------------ |
| id             | bigint(20)       | 主键 非空 自增 用户表的唯一标识 |
| userAccount    | varchar(256)     | 账号 非空                       |
| userPassword   | varchar(512)     | 密码（加密） 非空               |
| unionId        | varchar(256)     | 微信开放平台 ID                 |
| mpOpenId       | varchar(256)     | 公众号 openId                   |
| userName       | varchar(256)     | 用户昵称                        |
| userAvatar     | varchar(1024)    | 用户头像                        |
| userProfile    | varchar(512)     | 用户简介                        |
| userRole       | varchar(256)     | 用户角色：user/admin/ban        |
| userPhone      | varchar(255)     | 联系电话                        |
| aiRemainNumber | int(11)          | 用户 AI 剩余可使用次数          |
| balance        | decimal(10, 2)   | 用户余额（仅 AI 接口调用）      |
| editTime       | datetime         | 编辑时间                        |
| createTime     | datetime         | 创建时间                        |
| updateTime     | datetime         | 更新时间                        |
| isDelete       | tinyint(4)       | 是否删除（1 删除，0 未删除）    |

------

### **admins 表**

| 列名          | 数据类型以及长度 | 备注                              |
| :------------ | :--------------- | :-------------------------------- |
| id            | bigint(20)       | 主键 非空 自增 管理员表的唯一标识 |
| adminAccount  | varchar(256)     | 管理员账号 非空                   |
| adminPassword | varchar(512)     | 管理员密码（加密） 非空           |
| adminName     | varchar(256)     | 管理员真实姓名 非空               |
| adminRole     | varchar(256)     | 管理员角色：admin/superAdmin      |
| adminPhone    | varchar(255)     | 联系电话                          |
| createTime    | datetime         | 创建时间                          |
| updateTime    | datetime         | 更新时间                          |
| isDelete      | tinyint(4)       | 是否删除（1 删除，0 未删除）      |

------

### **spot 表**

| 列名            | 数据类型以及长度 | 备注                                 |
| :-------------- | :--------------- | :----------------------------------- |
| id              | bigint(20)       | 主键 非空 自增 景点表的唯一标识      |
| adminId         | bigint(20)       | 管理员 ID（创建该景点的管理员）      |
| spotName        | varchar(255)     | 景点名称 非空                        |
| spotAvatar      | varchar(1024)    | 景点封面图                           |
| spotLocation    | varchar(255)     | 景点所在地                           |
| spotDescription | varchar(4096)    | 景点介绍                             |
| spotTags        | varchar(2048)    | 景点标签（JSON 字符串数组）          |
| favourNum       | int(11)          | 收藏量                               |
| viewNum         | int(11)          | 浏览量                               |
| spotStatus      | tinyint(4)       | 景点状态（1 开放，0 关闭，默认关闭） |
| createTime      | datetime         | 创建时间                             |
| updateTime      | datetime         | 更新时间                             |
| isDelete        | tinyint(4)       | 是否删除（1 删除，0 未删除）         |

------

### **spot_fee 表**

| 列名               | 数据类型以及长度 | 备注                                     |
| :----------------- | :--------------- | :--------------------------------------- |
| id                 | bigint(20)       | 主键 非空 自增 门票表的唯一标识          |
| spotId             | bigint(20)       | 景点 ID 非空                             |
| adminId            | bigint(20)       | 管理员 ID                                |
| spotFeeDescription | varchar(1024)    | 门票描述                                 |
| spotFeePrice       | decimal(10, 2)   | 门票价格 非空                            |
| spotFeeNumber      | int(11)          | 景点门票数量                             |
| spotFeeStatus      | tinyint(4)       | 门票可用状态（1 可用，0 不可用，默认 0） |
| createTime         | datetime         | 创建时间                                 |
| updateTime         | datetime         | 更新时间                                 |
| isDelete           | tinyint(4)       | 是否删除（1 删除，0 未删除）             |

------

### **spot_order 表**

| 列名          | 数据类型以及长度 | 备注                                   |
| :------------ | :--------------- | :------------------------------------- |
| id            | bigint(20)       | 主键 非空 自增 订单表的唯一标识        |
| userId        | bigint(20)       | 用户 ID 非空                           |
| spotFeeId     | bigint(20)       | 门票 ID 非空                           |
| userName      | varchar(255)     | 用户名                                 |
| userPhone     | varchar(255)     | 联系电话                               |
| paymentAmount | decimal(10, 2)   | 支付金额                               |
| payStatus     | tinyint(4)       | 支付状态（1 已支付，0 未支付，默认 0） |
| createTime    | datetime         | 创建时间                               |
| updateTime    | datetime         | 更新时间                               |
| isDelete      | tinyint(4)       | 是否删除（1 删除，0 未删除）           |

------

### **spot_route 表**

| 列名                 | 数据类型以及长度 | 备注                                 |
| :------------------- | :--------------- | :----------------------------------- |
| id                   | bigint(20)       | 主键 非空 自增 路线表的唯一标识      |
| spotIds              | varchar(2048)    | 景点 ID（JSON 字符串，顺序从前往后） |
| adminId              | bigint(20)       | 管理员 ID                            |
| spotRouteAvatar      | varchar(2048)    | 路线封面图                           |
| spotRouteDescription | varchar(1024)    | 路线描述                             |
| createTime           | datetime         | 创建时间                             |
| updateTime           | datetime         | 更新时间                             |
| isDelete             | tinyint(4)       | 是否删除（1 删除，0 未删除）         |

------

### **spot_score 表**

| 列名       | 数据类型以及长度 | 备注                            |
| :--------- | :--------------- | :------------------------------ |
| id         | bigint(20)       | 主键 非空 自增 评分表的唯一标识 |
| spotId     | bigint(20)       | 景点 ID 非空                    |
| userId     | bigint(20)       | 用户 ID 非空                    |
| score      | int(10)          | 评分（满分 5）                  |
| createTime | datetime         | 创建时间                        |
| updateTime | datetime         | 更新时间                        |
| isDelete   | tinyint(4)       | 是否删除（1 删除，0 未删除）    |

------

### **user_ai_message 表**

| 列名           | 数据类型以及长度 | 备注                            |
| :------------- | :--------------- | :------------------------------ |
| id             | bigint(20)       | 主键 非空 自增 消息表的唯一标识 |
| userInputText  | varchar(4096)    | 用户输入文本                    |
| aiGenerateText | varchar(4096)    | AI 生成结果                     |
| userId         | bigint(20)       | 用户 ID 非空                    |
| createTime     | datetime         | 创建时间                        |
| updateTime     | datetime         | 更新时间                        |
| isDelete       | tinyint(4)       | 是否删除（1 删除，0 未删除）    |

------

### **user_spot_favorites 表**

| 列名       | 数据类型以及长度 | 备注                               |
| :--------- | :--------------- | :--------------------------------- |
| id         | bigint(20)       | 主键 非空 自增 收藏表的唯一标识    |
| userId     | bigint(20)       | 用户 ID 非空                       |
| spotId     | bigint(20)       | 景点 ID 非空                       |
| status     | tinyint(4)       | 收藏状态（1 正常收藏，0 取消收藏） |
| remark     | varchar(255)     | 用户备注                           |
| isDelete   | tinyint(4)       | 是否删除（1 删除，0 未删除）       |
| createTime | datetime         | 创建时间                           |
| updateTime | datetime         | 更新时间                           |

------



## 🐼功能演示图

### 用户(游客）模块功能图

**欢迎页**

![](https://pic.yupi.icu/5563/202503051404464.png)

**主页（风景轮播图）**

![](https://pic.yupi.icu/5563/202503051405313.png)

**景点**

![](https://pic.yupi.icu/5563/202503051405320.png)

**景点路线**

![](https://pic.yupi.icu/5563/202503051405417.png)

**旅游公告**

![](https://pic.yupi.icu/5563/202503051406443.png)

**旅客留言**

![](https://pic.yupi.icu/5563/202503051406169.png)

**旅游攻略**

![](https://pic.yupi.icu/5563/202503051407006.png)

**AI 旅游推荐官**

![](https://pic.yupi.icu/5563/202503051407232.png)

**热门景点（排行榜）**

![](https://pic.yupi.icu/5563/202503051408061.png)

**个人主页（包含个人信息，收藏攻略，收藏景点，个人订单，我的评论，旅游日历）**

![](https://pic.yupi.icu/5563/202503051408235.png)

### 系统管理员功能图

欢迎页和个人主页同用户端，不再展示图片！

**用户管理**

![](https://pic.yupi.icu/5563/202503051413053.png)

**公告管理**

![](https://pic.yupi.icu/5563/202503051414171.png)

**攻略管理**

![](https://pic.yupi.icu/5563/202503051414237.png)

**AI 对话管理**

![](https://pic.yupi.icu/5563/202503051416998.png)

**景点管理**

![](https://pic.yupi.icu/5563/202503051416767.png)

**景点路线管理**

![](https://pic.yupi.icu/5563/202503051417830.png)

**景点门票管理**

![](https://pic.yupi.icu/5563/202503051417892.png)

**景点订单管理**

![](https://pic.yupi.icu/5563/202503051417746.png)

## 🐼部署项目

![](https://pic.yupi.icu/5563/202503051418986.png)

+ 可以下载ZIP压缩包或者使用克隆(Git clone)
+ 复制http或者ssh的链接（github建议ssh,gittee都可以)
+ 在D盘新建一个文件夹，点击进入该文件夹，右键Git Bash Here

![](https://pic.yupi.icu/5563/202403021406715.png)

+ 还没有下载 Git或者不会 Git 的建议先看基础教程（ 30 分钟左右)

+ 输入 git init 初始化 git 项目 然后出现一个 .git 文件夹
+ 输入 git remote add origin xxxxxx (xxx 为刚刚复制的 http 或者 ssh 链接)

+ 输入 git pull origin master 从远程代码托管仓库拉取代码
+ 成功拉取项目（前端后端都是如此)
+ 前端项目注意依赖下载使用 npm install 或者 yarn install （ Vscode 或者 Webstorm )
+ 后端项目注意 maven 依赖下载（ IDEA (推荐)或者 Ecplise )
+ 前端 npm 镜像源建议淘宝镜像源，后端 maven 镜像源推荐阿里云镜像源（非必选，但更换后下载快速) 

## 🐼部署项目问题

⭐

+ 乱码问题 项目采用的 UFT-8 
+ 一般出现乱码就是 UTF-8 和 GBK 二者相反
+ 请百度 IDEA 乱码和 Eclipse 乱码问题(描述清楚即可)

⭐

+ 点击交互按钮，没有发生反应。
+ 很明显，请求失败，浏览器打开开发者工具，Edge 浏览器直接 ctrl+shift+i ,其他浏览器按 F12
+ 查看红色的请求和响应状态码问题

⭐

+ 先阅读文档再进行问题的查询或者提问
+ 提问有技巧，模糊的发言，让高级架构师找BUG也无从下手

⭐

+ **QQ：909088445**
+ 一般晚上在线，建议先自己寻找问题！！！

## 🐼需求分析和设计

毕设文档（精简版和毕设版），有（**付费**）需求的可以加 QQ：909088445，适合走毕设和课设的小伙伴，图省事的可以找我。

![](https://pic.yupi.icu/5563/202503051419373.png)

<img src="https://pic.yupi.icu/5563/202503051420010.png" style="zoom: 50%;" />

## 🐼项目模拟数据库

+ 要 API 后端接口文档详细内容和数据库结构（**由于服务器、域名、 AI 模型的成本问题，以及个人模拟数据库是要花费大量时间的，因此现数据库文件已收费，懂技术和业务的小伙伴可以自己花时间即可模拟出来，不想浪费时间即可找我**），前后端 Star （收藏截图）两张+**一杯瑞幸咖啡的价格**即可获取完整的数据库文件（后续价格会提升，先到先得，而且该项目属于首发，查重率 0 懂的都懂懂，抓住时间优势！！！），并且进行数据库导入的指导。QQ：909088445。VX：xiaobaitiao_z。

#### **数据库领取截图示例(Gitee&GitHub)：**

![](https://pic.yupi.icu/5563/202503051421889.png)

![](https://pic.yupi.icu/5563/202503051421934.png)

## 🐼远程部署和项目讲解服务

远程部署服务需自己先下载向日葵远程控制软件，然后加 WX 或者 QQ 即可（**付费服务**），远程部署用于给完全不懂的小白，项目讲解服务用于**课设、实训、毕设答辩（可语音共享屏幕）服务**，想减省时间，提高通过率，直接加我即可，可以**定制背景图片和整体的样式功能**，**降重服务**也可私我！

## 🐷其他

+ 个人博客地址: https://luoye6.github.io/
+ 个人博客采用Hexo+Github托管
+ 采用butterfly主题可以实现定制化
+ 推荐有空闲时间的，可以花1-2天搭建个人博客用于记录笔记。

## ☕请我喝咖啡

如果本项目对您有所帮助，不妨请作者我喝杯咖啡 ：）

<div><img src="https://pic.yupi.icu/5563/202312191854931.png" style="height:300px;width:300px"></img> <img src="https://pic.yupi.icu/5563/202312191859536.png" style="height:300px;width:300px"></img></div>

## **版本迭代**

### 2025-3-5

智能 AI 旅游推荐平台第一版发布！（感谢各位用户对之前 GPT 智能图书馆项目的支持与帮助）

## 贡献指南

项目多有不足，如果想帮助 **智能 AI 旅游推荐平台**变得更好，请遵循以下步骤：

1）Fork 本仓库。

2）创建你的特性分支 (`git checkout -b feature/AmazingFeature`)。

3）提交你的更改 (`git commit -m 'Add some AmazingFeature'`)。

4）将你的更改推送到分支 (`git push origin feature/AmazingFeature`)。

5）打开一个Pull Request。