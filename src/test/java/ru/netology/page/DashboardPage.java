package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final SelenideElement heading = $("[data-test-id=dashboard]");
    private final ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private final SelenideElement topUpTheFirstCard = $$("[data-test-id=action-deposit]").first();
    private final SelenideElement topUpTheSecondCard = $$("[data-test-id=action-deposit]").last();
    private final SelenideElement amount = $$("[data-test-id=amount] input").first();
    private final SelenideElement from = $("[data-test-id=from] input ");
    private final SelenideElement transfer = $("[data-test-id=action-transfer]");
    public final SelenideElement notification = $("[data-test-id=notification] .input__sub");

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public DashboardPage transfersToFirstCard() {
        topUpTheFirstCard.click();
        return new DashboardPage();
    }

    public DashboardPage transfersToSecondCard() {
        topUpTheSecondCard.click();
        return new DashboardPage();
    }

    public DashboardPage transfersMoney(String amountValue, String debitCardNumber) {
        amount.setValue(amountValue);
        from.setValue(debitCardNumber);
        transfer.click();
        return new DashboardPage();
    }

    public int getCardBalance(String number) {
        val text = cards.find(text(number.substring(16, 19))).getText();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
    public DashboardPage notificationTransfer(){
        notification.shouldHave(exactText("Перевод невозможен!. Не достаточно средств"));
        return new DashboardPage();
            }

}