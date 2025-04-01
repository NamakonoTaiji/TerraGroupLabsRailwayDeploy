# --- ステージ1: ビルドステージ ---
FROM maven:3.9-eclipse-temurin-21 AS builder 

# ★★★ デバッグポイント 1 ★★★
WORKDIR /app 
RUN echo "Current directory after WORKDIR:" && pwd
RUN echo "Listing /app after WORKDIR:" && ls -la /app
# ★★★ ここまで ★★★

# pom.xml を先にコピーして依存関係をダウンロード (キャッシュ効率化のため)
COPY pom.xml .
RUN mvn dependency:go-offline -B
# ★★★ デバッグポイント 2 ★★★
RUN echo "Listing /app after mvn dependency:go-offline:" && ls -la /app
# ★★★ ここまで ★★★

# アプリケーションのソースコードをコピー
COPY src ./src

# Mavenを使ってアプリケーションをビルド（.warファイルを作成）
COPY mvnw .
COPY .mvn/ .mvn/
RUN chmod +x ./mvnw
RUN MAVEN_OPTS="-Xmx512m" ./mvnw package -DskipTests

# ★★★ 前回失敗したデバッグコマンド (一旦コメントアウト or 削除してもOK) ★★★
# RUN ls -la /app/ 

# --- ステージ2: ランタイムステージ ---
FROM eclipse-temurin:21-jre-jammy 
WORKDIR /app 
# ここはまだ失敗する可能性が高い
COPY --from=builder /app/target/*.war app.war 

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]