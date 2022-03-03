package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.clearBrowserCookies;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;

public class TransferTest {

    @BeforeEach
    void setUp() {
        val loginPage = open("http://localhost:9999", LoginPage.class);
        val verificationPage = loginPage.validLogin(DataHelper.getAuthInfo());
        val verificationCode = DataHelper.getVerificationCodeFor();
        verificationPage.validVerify(verificationCode);
        clearBrowserCookies();
    }

    @Test
    void shouldTransferMoneyFromSecondToFirstCard() {
        val dashboardPage = new DashboardPage();
        val amount = 100;
        val currentBalanceOfFirstCard = dashboardPage.getCurrentBalanceOfFirstCard();
        val currentBalanceOfSecondCard = dashboardPage.getCurrentBalanceOfSecondCard();
        val transmissionPage = dashboardPage.transmissionToSecondCard();
        transmissionPage.transmission(getFirstCardNumber(), amount);

        val balanceOfFirstCardAfterTransmit = dashboardPage.getCurrentBalanceOfFirstCard();
        val balanceOfSecondCardAfterTransmit = dashboardPage.getCurrentBalanceOfSecondCard();
        val expectedBalanceOfFirstCardAfterTransmit = getExpectedBalanceIfBalanceDecrease(currentBalanceOfFirstCard, amount);
        val expectedBalanceOfSecondCardAfterTransmit = getExpectedBalanceIfBalanceRise(currentBalanceOfSecondCard, amount);
        assertEquals(expectedBalanceOfFirstCardAfterTransmit, balanceOfFirstCardAfterTransmit);
        assertEquals(expectedBalanceOfSecondCardAfterTransmit, balanceOfSecondCardAfterTransmit);
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondCard() {
        val dashboardPage = new DashboardPage();
        val amount = 100;
        val currentBalanceOfFirstCard = dashboardPage.getCurrentBalanceOfFirstCard();
        val currentBalanceOfSecondCard = dashboardPage.getCurrentBalanceOfSecondCard();
        val transmissionPage = dashboardPage.transmissionToFirstCard();
        transmissionPage.transmission(getSecondCardNumber(), amount);

        val balanceOfFirstCardAfterTransmit = dashboardPage.getCurrentBalanceOfFirstCard();
        val balanceOfSecondCardAfterTransmit = dashboardPage.getCurrentBalanceOfSecondCard();
        val expectedBalanceOfFirstCardAfterTransmit = getExpectedBalanceIfBalanceRise(currentBalanceOfFirstCard, amount);
        val expectedBalanceOfSecondCardAfterTransmit = getExpectedBalanceIfBalanceDecrease(currentBalanceOfSecondCard, amount);
        assertEquals(expectedBalanceOfFirstCardAfterTransmit, balanceOfFirstCardAfterTransmit);
        assertEquals(expectedBalanceOfSecondCardAfterTransmit, balanceOfSecondCardAfterTransmit);
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondCardMoreBalance() {
        val dashboardPage = new DashboardPage();
        val amount = 11000;
        val currentBalanceOfFirstCard = dashboardPage.getCurrentBalanceOfFirstCard();
        val currentBalanceOfSecondCard = dashboardPage.getCurrentBalanceOfSecondCard();
        val transmissionPage = dashboardPage.transmissionToSecondCard();
        transmissionPage.transmission(getFirstCardNumber(), amount);
        val updatedBalanceFirstCard = dashboardPage.getCurrentBalanceOfFirstCard();
        val updatedBalanceSecondCard = dashboardPage.getCurrentBalanceOfSecondCard();
        assertEquals(currentBalanceOfFirstCard, updatedBalanceFirstCard);
        assertEquals(currentBalanceOfSecondCard, updatedBalanceSecondCard);
        transmissionPage.notificationTransfer();
    }

    @Test
    void shouldTransferZeroAmount() {
        val dashboardPage = new DashboardPage();
        val amount = 0;
        val currentBalanceOfFirstCard = dashboardPage.getCurrentBalanceOfFirstCard();
        val currentBalanceOfSecondCard = dashboardPage.getCurrentBalanceOfSecondCard();
        val transmissionPage = dashboardPage.transmissionToSecondCard();
        transmissionPage.transmission(getFirstCardNumber(), amount);

        val balanceOfFirstCardAfterTransmit = dashboardPage.getCurrentBalanceOfFirstCard();
        val balanceOfSecondCardAfterTransmit = dashboardPage.getCurrentBalanceOfSecondCard();
        val expectedBalanceOfFirstCardAfterTransmit = getExpectedBalanceIfBalanceDecrease(currentBalanceOfFirstCard, amount);
        val expectedBalanceOfSecondCardAfterTransmit = getExpectedBalanceIfBalanceRise(currentBalanceOfSecondCard, amount);
        assertEquals(expectedBalanceOfFirstCardAfterTransmit, balanceOfFirstCardAfterTransmit);
        assertEquals(expectedBalanceOfSecondCardAfterTransmit, balanceOfSecondCardAfterTransmit);
    }

    @Test
    void shouldBeErrorIfFromInputCardIsEmpty() {
        val dashboardPage = new DashboardPage();
        val amount = 1000;
        val transmissionPage = dashboardPage.transmissionToSecondCard();
        transmissionPage.transmission(getEmptyCardNumber(), amount);
        transmissionPage.errorTransfer();
    }

    @Test
    void shouldBeErrorIfNotCorrectCardNumber() {
        val dashboardPage = new DashboardPage();
        val amount = 1000;
        val transmissionPage = dashboardPage.transmissionToSecondCard();
        transmissionPage.transmission(getNotCorrectCardNumber(), amount);
        transmissionPage.errorTransfer();
    }
}
