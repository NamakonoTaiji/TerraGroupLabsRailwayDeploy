// このファイルが属するパッケージ名を宣言します。
// config パッケージは、アプリケーションの設定関連クラスをまとめるためによく使われます。
package com.terragrouplabs.config;

// 必要なクラスをインポートします。
// Bean: Spring IoCコンテナに管理されるオブジェクト（Bean）を定義することを示すアノテーション。
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Spring MVCに関する設定を行うクラスです。
 *
 * @Configuration アノテーションが付いているため、Spring Bootが起動時にこのクラスを読み込み、
 * アプリケーションの設定情報として利用します。
 */
@Configuration
public class MvcConfig {

    /**
     * ViewResolverのBean定義です。 ViewResolverは、Controllerが返したビュー名（文字列）を、
     * 実際に表示するビューファイル（この場合はJSPファイル）のパスに変換する役割を持ちます。
     *
     * @Bean アノテーションが付いているメソッドは、その戻り値をSpring IoCコンテナが管理するBeanとして登録します。
     *
     * @return 設定済みのInternalResourceViewResolverインスタンス
     */
    @Bean
    public InternalResourceViewResolver viewResolver() {
        // InternalResourceViewResolverのインスタンスを生成します。
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();

        // setPrefix: Controllerが返すビュー名の前につける接頭辞（プレフィックス）を設定します。
        // 例えば、Controllerが "home" という文字列を返した場合、
        // この設定によりビューファイルのパスは "/WEB-INF/views/home" となります。
        // "/WEB-INF/" ディレクトリは、通常Webブラウザから直接アクセスできないように設定されるため、
        // JSPファイルなどのビューテンプレートを安全な場所に置くために使われます。
        resolver.setPrefix("/WEB-INF/views/");

        // setSuffix: Controllerが返すビュー名の後ろにつける接尾辞（サフィックス）を設定します。
        // 例えば、Controllerが "home" という文字列を返し、上記のPrefix設定と合わせると、
        // 最終的なビューファイルのパスは "/WEB-INF/views/home.jsp" となります。
        // これにより、Controllerでは拡張子を除いたビュー名だけを指定すればよくなります。
        resolver.setSuffix(".jsp");

        // setExposeContextBeansAsAttributes(true):
        // Springコンテナで管理されている全てのBeanを、リクエスト属性としてJSPからアクセス可能にするかどうかを設定します。（デフォルトはfalse）
        // trueにすると、JSP内で `${beanName.someProperty}` のようにBeanのプロパティに直接アクセスできますが、
        // 意図しないBeanが公開される可能性もあるため、通常はfalseのままか、必要なBeanだけをModelに追加する方法が推奨されます。
        resolver.setExposeContextBeansAsAttributes(true);

        // setExposePathVariables(true):
        // Controllerのメソッドで `@PathVariable` を使って取得したパス変数を、
        // リクエスト属性としてJSPからアクセス可能にするかどうかを設定します。（デフォルトはtrue）
        // 例えば、`/users/{userId}` というURLで `userId` を取得した場合、
        // JSP内で `${userId}` のようにアクセスできるようになります。
        resolver.setExposePathVariables(true);

        // 設定が完了したViewResolverのインスタンスを返します。
        // このインスタンスがSpring IoCコンテナにBeanとして登録されます。
        return resolver;
    }
}
