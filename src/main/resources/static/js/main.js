// 即時実行関数式（IIFE）でスコープを分離
console.log('main.js loaded');

// フォームエラー判定関数
function hasFormError() {
  const urlParams = new URLSearchParams(window.location.search);
  return urlParams.has('formError');
}

// ページの初期化処理を一元化
window.addEventListener('DOMContentLoaded', function() {
  console.log('DOM fully loaded');
  
  // 画像の遅延読み込みフォールバックを初期化
  initLazyLoading();
  
  // 最初にページ上部に移動
  window.scrollTo(0, 0);
  
  // フォームエラーがある場合のみお問い合わせセクションへスクロール
  if (hasFormError() && window.location.hash === '#contact') {
    setTimeout(function() {
      const contactElement = document.getElementById('contact');
      if (contactElement) {
        const headerHeight = document.querySelector('.navbar').offsetHeight || 70;
        const elementTop = contactElement.getBoundingClientRect().top + window.pageYOffset - headerHeight - 20;
        
        window.scrollTo({
          top: elementTop,
          behavior: 'smooth'
        });
        
        // フォーム要素にフォーカス
        const firstInput = contactElement.querySelector('input, textarea');
        if (firstInput) {
          firstInput.focus();
        }
      }
    }, 300); // 遅延を設定
  }
  
  // 各種初期化処理を実行
  initNavigation();
  initScrollEffects();
  initFormValidation();
  initCharts();
});

function initNavigation() {
  console.log('initNavigation called');
  const nav = document.querySelector('.navbar.terra-nav');
  
  if (nav) {
    window.addEventListener('scroll', function() {
      if (window.scrollY > 50) {
        nav.classList.add('scrolled');
      } else {
        nav.classList.remove('scrolled');
      }
    });
  }

  // スムーズスクロール
  document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function(e) {
      e.preventDefault();
      
      const targetId = this.getAttribute('href');
      if (targetId === '#') return;
      
      const targetElement = document.querySelector(targetId);
      if (targetElement) {
        // ヘッダーの高さを考慮したスクロール
        const headerHeight = document.querySelector('.navbar').offsetHeight;
        const elementTop = targetElement.getBoundingClientRect().top + window.pageYOffset - headerHeight - 20;
        
        window.scrollTo({
          top: elementTop,
          behavior: 'smooth'
        });
      }
    });
  });
}

// スクロールアニメーション効果
function initScrollEffects() {
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('fade-in-up');
        observer.unobserve(entry.target);
      }
    });
  }, { threshold: 0.1 });

  const animateElements = document.querySelectorAll('.animate-on-scroll');
  animateElements.forEach(element => {
    observer.observe(element);
  });
}

// フォームバリデーション
function initFormValidation() {
  const contactForm = document.getElementById('contactForm');
  if (contactForm) {
    contactForm.addEventListener('submit', validateContactForm);
  }
}

// フォームバリデーション関数
function validateContactForm(e) {
  let isValid = true;
  let firstInvalidField = null;

  // 名前のバリデーション
  const nameInput = document.getElementById('name');
  if (nameInput && !nameInput.value.trim()) {
    showError(nameInput, '名前は必須です');
    isValid = false;
    firstInvalidField = firstInvalidField || nameInput;
  } else if (nameInput) {
    clearError(nameInput);
  }

  // メールアドレスのバリデーション
  const emailInput = document.getElementById('email');
  if (emailInput) {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(emailInput.value)) {
      showError(emailInput, '有効なメールアドレスを入力してください');
      isValid = false;
      firstInvalidField = firstInvalidField || emailInput;
    } else {
      clearError(emailInput);
    }
  }

  // メッセージのバリデーション
  const messageInput = document.getElementById('message');
  if (messageInput && !messageInput.value.trim()) {
    showError(messageInput, 'メッセージは必須です');
    isValid = false;
    firstInvalidField = firstInvalidField || messageInput;
  } else if (messageInput) {
    clearError(messageInput);
  }

  // reCAPTCHAの検証
  if (typeof grecaptcha !== 'undefined') {
    const recaptchaContainer = document.querySelector('.g-recaptcha');
    if (recaptchaContainer) {
      const recaptchaResponse = grecaptcha.getResponse();

      if (!recaptchaResponse) {
        let errorElement = document.createElement('div');
        errorElement.className = 'text-danger mt-2';
        errorElement.textContent = 'ロボットではないことを確認してください';

        const existingError = recaptchaContainer.nextElementSibling;
        if (existingError && existingError.classList.contains('text-danger')) {
          existingError.remove();
        }

        recaptchaContainer.after(errorElement);
        recaptchaContainer.style.animation = 'shake 0.5s ease-in-out';

        if (!firstInvalidField) {
          firstInvalidField = recaptchaContainer;
        }

        isValid = false;
      }
    }
  }

  // フォームが無効な場合は送信をキャンセル
  if (!isValid) {
    e.preventDefault();
    if (firstInvalidField) {
      firstInvalidField.focus();
      scrollToElement(firstInvalidField);
    }
  }
}

// エラーを表示する関数
function showError(input, message) {
  input.classList.add('is-invalid');
  input.classList.remove('is-valid');

  let errorElement = input.nextElementSibling;
  if (!errorElement || !errorElement.classList.contains('invalid-feedback')) {
    errorElement = document.createElement('div');
    errorElement.className = 'invalid-feedback';
    input.parentNode.appendChild(errorElement);
  }
  errorElement.textContent = message;
}

// エラーをクリアする関数
function clearError(input) {
  input.classList.remove('is-invalid');
  input.classList.add('is-valid');
}

// 指定要素までスクロール
function scrollToElement(element) {
  const headerHeight = document.querySelector('.navbar').offsetHeight || 70;
  const elementTop = element.getBoundingClientRect().top + window.pageYOffset - headerHeight - 20;
  window.scrollTo({
    top: elementTop,
    behavior: 'smooth'
  });
}

// チャートの初期化
function initCharts() {
  if (document.getElementById('achievementChart')) {
    const ctx = document.getElementById('achievementChart').getContext('2d');

    const achievementChart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: ['2020', '2021', '2022', '2023', '2024'],
        datasets: [{
          label: '研究開発プロジェクト数',
          data: [12, 19, 25, 30, 42],
          backgroundColor: 'rgba(54, 162, 235, 0.5)',
          borderColor: 'rgba(54, 162, 235, 1)',
          borderWidth: 1
        }, {
          label: '特許取得数',
          data: [5, 10, 15, 22, 30],
          backgroundColor: 'rgba(75, 192, 192, 0.5)',
          borderColor: 'rgba(75, 192, 192, 1)',
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        plugins: {
          title: {
            display: true,
            text: 'TerraGroup Labs 成長実績'
          },
        },
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });
  }
}

// reCAPTCHA用のコールバック関数はグローバルスコープに必要
function recaptchaCallback() {
  const recaptchaContainer = document.querySelector('.g-recaptcha');
  if (recaptchaContainer) {
    const errorElement = recaptchaContainer.nextElementSibling;
    if (errorElement && errorElement.classList.contains('text-danger')) {
      errorElement.remove();
    }
  }
}

// 画像の遅延読み込みフォールバック
// native loading="lazy" 属性をサポートしていないブラウザ用
function initLazyLoading() {
  // ブラウザが loading="lazy" をサポートしているか確認
  if ('loading' in HTMLImageElement.prototype) {
    console.log('Browser supports native lazy loading');
    return; // ネイティブサポートありの場合は何もしない
  }
  
  console.log('Browser does not support native lazy loading, using fallback');
  
  // IntersectionObserver が利用可能か確認
  if (!('IntersectionObserver' in window)) {
    // IntersectionObserver もサポートしていない場合は全画像を読み込み
    loadAllImages();
    return;
  }
  
  // IntersectionObserver を使用した遅延読み込みの実装
  const observer = new IntersectionObserver((entries, observer) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        const img = entry.target;
        const src = img.getAttribute('data-src');
        
        if (src) {
          img.setAttribute('src', src);
          img.removeAttribute('data-src');
        }
        
        observer.unobserve(img);
      }
    });
  }, {
    rootMargin: '200px 0px' // ビューポートの200px手前から読み込みを開始
  });
  
  // loading="lazy" 属性を持つ画像を取得
  const lazyImages = document.querySelectorAll('img[loading="lazy"]');
  
  lazyImages.forEach(img => {
    // 元のsrcをdata-srcに移動
    const src = img.getAttribute('src');
    if (src) {
      img.setAttribute('data-src', src);
      
      // プレースホルダー画像を設定（オプション）
      // データ URI で軽量な半透明画像を設定するか、何も設定しない
      img.setAttribute('src', 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1 1"%3E%3C/svg%3E');
    }
    
    // observerで監視
    observer.observe(img);
  });
}

// IntersectionObserverもサポートしていないブラウザ用のフォールバック
function loadAllImages() {
  const lazyImages = document.querySelectorAll('img[loading="lazy"]');
  
  lazyImages.forEach(img => {
    // すべての画像を通常通り読み込む
    const src = img.getAttribute('data-src');
    if (src) {
      img.setAttribute('src', src);
    }
  });
}