package com.terragrouplabs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify; // JUnitのアサーション
import static org.mockito.Mockito.when; // Mockitoのメソッド (when, verifyなど)
import org.mockito.junit.jupiter.MockitoExtension;

import com.terragrouplabs.entity.ContactMessage;
import com.terragrouplabs.repository.ContactMessageRepository;

@ExtendWith(MockitoExtension.class) // Mockitoを有効化
class ContactMessageServiceTest {

    @Mock // ContactMessageRepositoryのモックを作成
    private ContactMessageRepository mockContactRepository;

    @InjectMocks // mockContactRepositoryを注入してContactMessageServiceのインスタンスを作成
    private ContactMessageService contactMessageService;

    @Test // テストメソッドであることを示す
    void saveMessage_shouldSaveAndReturnMessage() { // テストメソッド名は分かりやすく (例: メソッド名_状況_期待結果)

        // Arrange (準備) --------------------
        // 1. テスト用のContactMessageオブジェクトを作成
        ContactMessage messageToSave = new ContactMessage();
        messageToSave.setName("テストユーザー");
        messageToSave.setEmail("test@example.com");
        messageToSave.setMessage("テストメッセージ");

        // 2. モックリポジトリの動作を設定
        //    「mockContactRepositoryのsaveメソッドが 'messageToSave' を引数に呼ばれたら、
        //      'messageToSave' (またはIDがセットされたもの) を返す」ように設定する。
        //    any() を使うと任意の ContactMessage でマッチさせられるが、
        //    具体的なオブジェクトで設定する方が厳密なテストになる。
        when(mockContactRepository.save(messageToSave)).thenReturn(messageToSave);
        // または、IDがセットされることを模倣する場合:
        // ContactMessage savedMessage = new ContactMessage();
        // savedMessage.setId(1L); // 仮のID
        // savedMessage.setName("テストユーザー");
        // savedMessage.setEmail("test@example.com");
        // savedMessage.setMessage("テストメッセージ");
        // when(mockContactRepository.save(messageToSave)).thenReturn(savedMessage);

        // Act (実行) ----------------------
        // 3. テスト対象のメソッドを呼び出す
        ContactMessage actualSavedMessage = contactMessageService.saveMessage(messageToSave);

        // Assert (検証) -------------------
        // 4. 結果を検証する
        assertNotNull(actualSavedMessage, "保存されたメッセージはnullであってはならない");
        assertEquals("テストユーザー", actualSavedMessage.getName(), "名前が一致しない");
        assertEquals("test@example.com", actualSavedMessage.getEmail(), "メールアドレスが一致しない");
        assertEquals("テストメッセージ", actualSavedMessage.getMessage(), "メッセージが一致しない");
        // もしIDがセットされることを模倣したなら:
        // assertEquals(1L, actualSavedMessage.getId(), "IDがセットされていないか、値が違う");

        // 5. モックリポジトリのメソッドが正しく呼ばれたか検証する (オプションだが重要)
        //    「mockContactRepositoryのsaveメソッドが、'messageToSave' を引数として、
        //      ちょうど1回だけ呼ばれたこと」を確認する。
        verify(mockContactRepository, times(1)).save(messageToSave);

        // 6. (任意) 他のメソッドが呼ばれていないことを確認
        // verifyNoMoreInteractions(mockContactRepository);
    }

    // 他のテストケース (例: null を渡した場合など) も必要に応じて追加 ...
    // @Test
    // void saveMessage_withNull_shouldThrowException() { ... }
}
