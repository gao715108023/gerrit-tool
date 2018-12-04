# auto-codereview-tool
自动检测gerrit中每次提交的代码是否符合《阿里巴巴开发手册》规范，根据规范的不同等级，给予相应的CodeReview分数
## <font color="green">环境需求</font>
- JDK 1.8+
- Maven 3

## <font color="green">扫描规则</font>

工具检测规则基于PMD ([https://github.com/pmd/pmd](https://github.com/pmd/pmd))实现了 *阿里巴巴Java开发手册（终极版）.pdf*中的49个规则.
## <font color="green">快速开始</font>

### <font color="blue">1. 下载源代码</font>

	git clone https://github.com/gao715108023/gerrit-tool.git

### <font color="blue">2. 在/usr/local目录下创建文件夹CodeReview </font>

	sudo mkdir -p CodeReview
	#给CodeReview增加读写权限
	chmod -R 777 /usr/local/CodeReview
将源代码中`resources`下的`rulesets`文件夹拷贝至`/usr/local/CodeReview`目录下

### <font color="blue">3. 配置`filters/filter-dev.properties`</font>
	# ******************** log 相关配置 ********************
	#日志输出等级
	log.level=INFO
	#日志输出目录
	log.file=/usr/local/CodeReview
	#日志输出类型，STDOUT-代表控制台输出；InfoFile-代表文件输出
	log.appender=STDOUT
	#gerrit地址
	gerrit.url=http://127.0.0.1
	#gerrit用户名
	gerrit.user=gaochuanjun
	#gerrit密码
	gerrit.password=123456
	#rule目录
	rulesets.path=/usr/local/CodeReview/rulesets
	#review频率，单位为秒
	review.sleep.second=60
	
### <font color="blue">4. 配置`review-ignore`</font>
`review-ignore`文件是用来配置忽略哪些源代码不需要遵守《阿里巴巴开发手册》规范，刚开始可以直接使用默认配置。

	/dao/
	pom.xml
	resources
	test
	.gitignore
	webapp
	Example
	

### <font color="blue">5. 编译源代码</font>
	mvn clean install -DskipTests -P dev
### <font color="blue">6. 拷贝`target`目录下的`target/gerrit-code-review.zip`包至`/usr/local/CodeReview`目录下</font>
### <font color="blue">7. 解压缩</font>
	unzip gerrit-code-review.zip
### <font color="blue">3. 启动</font>
执行`service.sh`
	
	./service.sh start

日志中输出如下信息则代表启动成功！

	gerrit code review service server started


