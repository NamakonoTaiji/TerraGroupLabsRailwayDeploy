# TerraGroup Labs Landing Page

架空の研究開発企業「TerraGroup Labs」のコーポレートサイト（ランディングページ）およびお問い合わせ管理機能を持つ Spring Boot アプリケーションです。

**デプロイ先:** [https://web-production-a32c0.up.railway.app/](https://web-production-a32c0.up.railway.app/)

## 概要

このアプリケーションは、企業の公開情報（会社概要、サービス紹介など）を発信するランディングページとしての機能と、ユーザーからの問い合わせを受け付け、管理者が内容を確認できる機能を提供します。

## 主な機能

- **公開ページ:**
  - ホームページ (企業実績、ビジョン、ニュース、サービス概要、お問い合わせフォーム)
  - サービス紹介ページ
  - 会社概要ページ (ビジョン、ミッション、企業情報、沿革)
- **お問い合わせ機能:**
  - お問い合わせフォーム (名前、メールアドレス、メッセージ)
  - 入力内容の確認画面
  - Google reCAPTCHA v2 によるスパム対策
  - 送信完了 (Thank You) ページ
  - 管理者へのメール通知 (非同期処理)
- **管理機能:**
  - 管理者ログイン/ログアウト (Spring Security による認証)
  - お問い合わせメッセージ一覧表示
  - お問い合わせメッセージ詳細表示

## 使用技術スタック

- **バックエンド:**
  - Java 21
  - Spring Boot 3.4.4
  - Spring Security (認証、認可、CSRF 保護、セキュリティヘッダー)
  - Spring Data JPA (Hibernate)
  - Spring Web (MVC)
  - Spring Mail (JavaMailSender)
  - Spring Validation (Bean Validation)
  - Spring Async (非同期処理)
- **フロントエンド:**
  - JSP (Jakarta Server Pages)
  - JSTL (Jakarta Standard Tag Library)
  - HTML5
  - CSS3 (Bootstrap 5.3, カスタムスタイル)
  - JavaScript (基本的な DOM 操作、フォーム連携、スクロールエフェクト)
- **データベース:**
  - MySQL (開発環境、本番環境)
  - H2 Database (テスト環境)
- **ビルド・依存関係管理:**
  - Apache Maven
- **データベースマイグレーション:**
  - Flyway (`src/main/resources/db/migration`)
- **その他:**
  - Google reCAPTCHA v2 (サイト検証)
  - SLF4j (ロギング)
  - Jakarta Servlet

## 環境構築とローカルでの実行

### 前提条件

- JDK 21
- Apache Maven 3.6 以上
- MySQL サーバー (ローカル環境にインストール・起動済みであること)

### セットアップ手順

1.  **リポジトリのクローン:**

    ```bash
    git clone https://github.com/NamakonoTaiji/TerraGroupLabsRailwayDeploy.git
    cd TerraGroupLabsRailwayDeploy
    ```

2.  **ローカル MySQL データベースの準備:**

    - MySQL に接続し、アプリケーション用のデータベースを作成します。
      ```sql
      CREATE DATABASE terragroupdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
      ```
    - アプリケーション用のデータベースユーザーを作成し、パスワードを設定、権限を付与します。
      ```sql
      -- 例: ユーザー 'terragroupuser', パスワード 'StrongPass123!'
      CREATE USER 'terragroupuser'@'localhost' IDENTIFIED BY 'StrongPass123!';
      GRANT ALL PRIVILEGES ON terragroupdb.* TO 'terragroupuser'@'localhost';
      FLUSH PRIVILEGES;
      ```
      _(セキュリティのため、実際にはより強力なパスワードを使用してください)_

3.  **ローカル設定ファイルの作成 (`application-dev.properties`):**

    - `src/main/resources/` ディレクトリに `application-dev.properties` という名前でファイルを作成します。
    - 以下のテンプレートをコピーし、ご自身のローカル環境に合わせて **データベース接続情報** (`url`, `username`, `password`)、**管理者アカウント情報** (`ADMIN_USERNAME`, `ADMIN_PASSWORD`) を必ず設定してください。必要に応じてメール設定や reCAPTCHA キーも設定します。

      ```properties
      # src/main/resources/application-dev.properties (ローカル開発用)

      # --- データベース設定 (必須) ---
      spring.datasource.url=jdbc:mysql://localhost:3306/terragroupdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Tokyo
      spring.datasource.username=terragroupuser # 手順2で作成したDBユーザー名
      spring.datasource.password=StrongPass123! # 手順2で作成したDBパスワード
      spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
      spring.jpa.hibernate.ddl-auto=validate # Flywayでスキーマ管理するため validate または none を推奨

      # --- 管理者アカウント (必須) ---
      ADMIN_USERNAME=admin
      ADMIN_PASSWORD=password # 開発用に簡単なパスワードを設定

      # --- メール設定 (任意 - 管理者通知テスト用) ---
      # 例: Gmailを使う場合 (アプリパスワードが必要)
      # spring.mail.host=smtp.gmail.com
      # spring.mail.port=587
      # spring.mail.username=your-email@gmail.com
      # spring.mail.password=your-gmail-app-password
      # spring.mail.properties.mail.smtp.auth=true
      # spring.mail.properties.mail.smtp.starttls.enable=true
      # contact.admin.email=admin-notification-recipient@example.com

      # --- reCAPTCHA 設定 (任意 - フォームテスト用) ---
      # ローカル(localhost)用に発行したテストキーを設定
      # google.recaptcha.key=YOUR_LOCAL_RECAPTCHA_SITE_KEY
      # google.recaptcha.secret=YOUR_LOCAL_RECAPTCHA_SECRET_KEY

      # --- ログ設定 (開発用) ---
      logging.level.com.terragrouplabs=DEBUG
      logging.level.org.springframework.web=DEBUG
      logging.level.org.hibernate.SQL=DEBUG # 実行されるSQLを表示
      logging.level.org.hibernate.type.descriptor.sql=TRACE # SQLのパラメータを表示 (デバッグ用)
      ```

    - **重要:** この `application-dev.properties` は `.gitignore` に記載されているため、Git リポジトリにはコミットされません。各自で作成・管理してください。

4.  **Maven でビルド:**

    ```bash
    mvn clean package
    ```

    - 初回実行時、またはデータベーススキーマが存在しない場合、Flyway が `src/main/resources/db/migration` 内の SQL ファイル (`V1__init.sql`) を実行し、自動的にテーブルを作成します。

5.  **アプリケーションの実行 (開発プロファイル):**
    以下のいずれかのコマンドで実行します。

    ```bash
    # Mavenプラグインで実行
    mvn spring-boot:run -Dspring-boot.run.profiles=dev
    ```

    または

    ```bash
    # ビルドされたWARファイルを実行
    java -jar target/TerraGroupLabsLandingPage-*.war --spring.profiles.active=dev
    ```

    _(WAR ファイル名はビルドによって変わる可能性があります)_

6.  **アクセス:**
    - Web ブラウザで `http://localhost:8080` (デフォルトポート) にアクセスします。
    - 管理画面には `/login` からアクセスし、`application-dev.properties` で設定した `ADMIN_USERNAME` と `ADMIN_PASSWORD` でログインします。

## テスト

Maven を使用してユニットテストおよび結合テストを実行します。テスト時には H2 インメモリデータベースが使用されます (`src/test/resources/application-test.properties`)。

```bash
mvn test
```
