#-------------------
# Build Stage
#-------------------
# MavenとJava 21 JDKを含むイメージをビルドステージのベースとして使用
FROM maven:3.9-eclipse-temurin-21 AS builder

# アプリケーションの作業ディレクトリを設定
WORKDIR /app

# Maven Wrapper関連ファイルをコピー (キャッシュ効率化のため先にコピー)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# mvnw に実行権限を付与する
RUN chmod +x ./mvnw
# Mavenの依存関係をダウンロード (レイヤーキャッシュを活用)
# ./mvnw dependency:go-offline を使うとより効率的ですが、まずはシンプルな方法で
RUN ./mvnw dependency:resolve

# アプリケーションのソースコードをコピー
COPY src ./src

# アプリケーションをビルド (WARファイルを作成)
# ここで minify プラグインをスキップし、テストもスキップします
# メモリ使用量を制限 (-Xmx256m は256MBを指定。環境に応じて調整)
RUN MAVEN_OPTS="-Xmx512m" ./mvnw package -DskipTests

#-------------------
# Runtime Stage
#-------------------
# Java 21 JREのみを含む軽量なイメージを実行ステージのベースとして使用
FROM eclipse-temurin:21-jre

# アプリケーションの作業ディレクトリを設定
WORKDIR /app

# ビルドステージから生成されたWARファイルをコピー
# target/*.war のようにワイルドカードを使うか、pom.xmlのartifactIdとversionから特定します
# pom.xmlの <artifactId>terragrouplabslandingpagefixed</artifactId>, <version>0.0.1-SNAPSHOT</version> より
COPY --from=builder /app/target/terragrouplabslandingpagefixed-0.0.1-SNAPSHOT.war app.war

# アプリケーションがリッスンするポート (Railwayが環境変数PORTで指定)
# Dockerfile内でEXPOSEする必要は通常ありませんが、記述しておくと分かりやすいです
# EXPOSE 8080

# アプリケーションの起動コマンド
# Spring Bootの実行可能WARとして起動
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.war"]