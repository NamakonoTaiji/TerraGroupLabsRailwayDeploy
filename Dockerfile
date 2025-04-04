# --- ステージ1: ビルドステージ ---
# MavenとJDK21を含むイメージを"builder"という名前で使う
FROM maven:3.9-eclipse-temurin-21 AS builder 

# 作業ディレクトリを設定
WORKDIR /app

# pom.xml を先にコピーして依存関係をダウンロード (キャッシュ効率化のため)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# アプリケーションのソースコードをコピー
COPY src ./src

# Mavenを使ってアプリケーションをビルド（.warファイルを作成）
COPY mvnw .
COPY .mvn/ .mvn/
RUN chmod +x ./mvnw
RUN MAVEN_OPTS="-Xmx384m" ./mvnw package -DskipTests

# --- ステージ2: ランタイムステージ ---
# JRE21のみを含む軽量なイメージを使う
FROM eclipse-temurin:21-jre-alpine 

# 作業ディレクトリを設定
WORKDIR /app

# ビルドステージ(builder)から、ビルドされたWARファイルだけをコピー
COPY --from=builder /app/target/*.war app.war

# アプリケーションが使用するポートを公開
EXPOSE 8080

# コンテナ起動時にアプリケーションを実行
ENTRYPOINT ["java", "-Xmx384m", "-jar", "app.war"]