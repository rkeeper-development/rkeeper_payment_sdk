# Шаг 1. Настройка проекта
Создайте новый проект или откройте существующий, например, в Android Studio
Откройте файл build.gradle проекта. В секции repositories добавьте репозиторий 
```
maven { url "https://jitpack.io" }
```
Откройте файл build.gradle приложения (модуля). Укажите адрес репозитория и добавьте зависимость:

```
dependencies {    
    implementation "com.github.rkeeper-development:ucs_payment_sdk:1.0.22"
}
```

Синхронизируйте проект, чтобы применить изменения. Например, в Android Studio можно нажать Sync Now или выбрать в меню File → Synchronize. Дождитесь окончания синхронизации.
Если синхронизация завершилась успешно, при компиляции библиотека будет добавлена в проект автоматически. При ошибке компиляции, убедитесь что вы правильно указали репозиторий и зависимость и синхронизируйте проект снова.



# Шаг 2. Создание сервисов оплаты/печати
В SDK находятся два сервиса:
```
ru.usc.android.paymentservice.UcsPosService - общий сервис для взаимодействия с терминалами оплаты
ru.usc.android.paymentservice.UcsPrintService - общий сервис для взаимодействия с принтерами печати
```

Для того, чтобы модуль оплаты увидел сервис, он должен иметь соответствующий intent-filter:
```
ru.usc.payment.START_PAYMENT - сервис оплаты
ru.usc.payment.PRINT_CHECK - сервис печати
```

т.е. нужно предпринять следующие шаги по созданию сервисов оплаты/печати:

1. В приложении создать свой сервис, унаследованный от одного из SDK, для требуемого функционала

Пример сервиса оплаты:
```
import ru.usc.android.paymentservice.PaymentResult
import ru.usc.android.paymentservice.TransactionComplete
import ru.usc.android.paymentservice.UcsPosService

class ExternalPosTerminalService : UcsPosService() {

    override suspend fun startPayment(amount: String?, currencyCode: String?): PaymentResult {
        /**
         * ... ... Оплата POS терминалом
         */
        return TransactionComplete("ReturnTransactionId")
        //return TransactionError(404, "Not enough money")
    }

    override suspend fun startRefund(amount: String?, currencyCode: String?): PaymentResult {
        /**
         * ... ... Возврат оплаты POS терминалом
         */
        return TransactionComplete("ReturnTransactionId")
        //return TransactionError(404, "Not enough money")
    }
}
```

Пример сервиса печати:
```
import ru.usc.android.paymentservice.*

class ExternalPrintTerminalService : UcsPrintService() {

    override suspend fun startPrintFiscalCheck(
        order: String?,
        headers: List<String>?,
        footers: List<String>?
    ): PrintResult {
        /**
         * ... ... Печать фискального чека оплаты
         */
        return PrintComplete
        //return PrintError(500, "Paper not found")
    }

    override suspend fun startPrintRefundCheck(order: String?): PrintResult {
        /**
         * ... ... Печать фискального чека на возврат
         */
        return PrintComplete
        //return PrintError(500, "Paper not found")
    }

    override suspend fun startPrintXReport(): PrintResult {
        /**
         * ... ... Печать X-отчета
         */
        return PrintComplete
        //return PrintError(500, "Paper not found")
    }

    override suspend fun startPrintZReport(): PrintResult {
        /**
         * ... ... Печать Z-отчета
         */
        return PrintComplete
        //return PrintError(500, "Paper not found")
    }
}
```

2. В AndroidManifest.xml прописать созданный сервис в качестве экспортируемого и указать соответствующий (для оплаты или печати) intent-filter:
```
<service
    android:name=".ExternalPrintTerminalService"
    android:label="Ext PosTerminal 3"
    android:enabled="true"
    android:exported="true">
    <intent-filter>
        <action android:name="ru.usc.payment.START_PAYMENT" />
    </intent-filter>
</service>

<service
    android:name=".ExternalPosTerminalService"
    android:label="Ext PosTerminal 3"
    android:enabled="true"
    android:exported="true">
    <intent-filter>
        <action android:name="ru.usc.payment.PRINT_CHECK" />
    </intent-filter>
</service>
```

3. Методы сервисов оплаты/печати должны возвращать объект, унаследованный от sealed-классов PaymentResult или PrintResult соответственно



Шаг 3. Проверка работоспособности

Запустить параллельно с сервисом демо-приложение UCS, приложение должно вывести в список запущенные сервисы оплаты/печати и по нажатию на кнопку должен отработать требуемый функционал 
