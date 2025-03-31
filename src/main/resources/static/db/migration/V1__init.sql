-- 連絡先メッセージテーブル
CREATE TABLE IF NOT EXISTS contact_messages (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  message TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- サービスカテゴリテーブル
CREATE TABLE IF NOT EXISTS service_categories (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(200)
);

-- サービステーブル
CREATE TABLE IF NOT EXISTS services (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  category_id BIGINT,
  title VARCHAR(100) NOT NULL,
  description TEXT,
  icon_name VARCHAR(50),
  FOREIGN KEY (category_id) REFERENCES service_categories(id)
);

-- 初期データ投入
INSERT INTO service_categories (name, description) VALUES 
('研究開発', '最先端の技術研究と開発'),
('環境ソリューション', '持続可能な環境ソリューション'),
('コンサルティング', '専門知識に基づくアドバイス');

INSERT INTO services (category_id, title, description, icon_name) VALUES
(1, 'バイオテクノロジー', '持続可能な未来のための生体技術研究と開発を行っています。環境に優しい素材開発や医療応用など、幅広い分野での革新を目指しています。', 'bi-fingerprint'),
(1, 'AI研究', '最先端の人工知能と機械学習技術の研究開発を通じて、複雑な環境問題の解決や効率的なリソース管理システムの構築に取り組んでいます。', 'bi-cpu'),
(2, '環境テクノロジー', '地球環境保全のための革新的技術開発に取り組んでいます。カーボンニュートラルな社会の実現に向けた様々なソリューションを提供します。', 'bi-tree');