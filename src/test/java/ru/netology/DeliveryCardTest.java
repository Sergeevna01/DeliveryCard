package ru.netology;

import com.codeborne.selenide.SelenideElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.Long.parseLong;
import static java.time.Duration.ofSeconds;
import static java.util.Calendar.DAY_OF_YEAR;

public class DeliveryCardTest {
    SelenideElement form = $x("//form[contains(@class, form)]");
    SelenideElement notification = $x("//div[@data-test-id='notification']");
    SelenideElement calendarMenu = $$x("//body/div").get(1);

    // Дата из формы
    public String getExpectedDate() {
        return form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").getValue();
    }

    // Создание даты встречи с заданным кол-вом дней от настоящего времени
    public Calendar meetingDate(int addDays) {
        Calendar date = new GregorianCalendar();
        date.add(DAY_OF_YEAR, addDays);
        return date;
    }

    // Перевод формата даты в дд.мм.гггг
    public String inputMeetingDate(Calendar meetingDate) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(meetingDate.getTime());
    }

    // Получение времени из календаря
    public long getMillisecondFromCalendar(int i) {
        return parseLong(calendarMenu.$$x(".//td[@data-day]").get(i).getDomAttribute("data-day"));
    }

    // Получение города из формы
    public String getActualCity() {
        return form.$x(".//span[@data-test-id='city']//child::input").getValue();
    }

    @BeforeMethod
    public void setup() {
        open("http://localhost:9999");
    }

    @Test
    public void shouldSuccessDefaultDateTest() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Санкт-Петербург");
        form.$x(".//span[@data-test-id='name']//child::input").val("Шляк Лолита");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79199552643");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").should(text("Успешно"));
        notification.$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на "
                + getExpectedDate()));
        notification.$x(".//button").click();
        notification.should(hidden);
    }

    @Test
    public void shouldSuccessCustomDateTest() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Санкт-Петербург");
        String inputMeetingDate = inputMeetingDate(meetingDate(5));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Шляк Лолита");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79199552643");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").should(text("Успешно"));
        notification.$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на "
                + getExpectedDate()));
        notification.$x(".//button").click();
        notification.should(hidden);
    }

    @Test
    public void shouldValidCityTest1() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Москва");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Шляк Лолита");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79199552643");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").should(text("Успешно"));
        notification.$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на "
                + getExpectedDate()));
        notification.$x(".//button").click();
        notification.should(hidden);
    }

    @Test
    public void shouldValidCityTest2() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Орёл");
        String inputMeetingDate = inputMeetingDate(meetingDate(6));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Шляк Лолита");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79199552643");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").should(text("Успешно"));
        notification.$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на "
                + getExpectedDate()));
        notification.$x(".//button").click();
        notification.should(hidden);
    }

    @Test
    public void shouldInvalidCityTest1() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Moscow");
        String inputMeetingDate = inputMeetingDate(meetingDate(9));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Шляк Лолита");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79199552643");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='city']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='city']//child::span[@class='input__sub']").
                should(visible, text("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldInvalidCityTest2() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Нижняя Тавда");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Шляк Лолита");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79199552643");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='city']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='city']//child::span[@class='input__sub']").
                should(visible, text("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldInvalidDateTest1() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Санкт-Петербург");
        String inputMeetingDate = inputMeetingDate(meetingDate(2));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Шляк Лолита");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79199552643");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='date']/span/span").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='date']//child::span[@class='input__sub']").
                should(visible, text("Заказ на выбранную дату невозможен"));
    }

    @Test
    public void shouldInvalidDateTest2() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Санкт-Петербург");
        String inputMeetingDate = inputMeetingDate(meetingDate(0));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Шляк Лолита");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79199552643");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='date']/span/span").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='date']//child::span[@class='input__sub']").
                should(visible, text("Заказ на выбранную дату невозможен"));
    }


    @Test
    public void shouldValidNameTest1() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Тюмень");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Котов Артём");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79210000000");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").should(text("Успешно"));
        notification.$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на "
                + getExpectedDate()));
        notification.$x(".//button").click();
        notification.should(hidden);
    }

    @Test
    public void shouldValidNameTest2() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Тюмень");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Анна-Мария Лагнесс");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79199552643");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").should(text("Успешно"));
        notification.$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на "
                + getExpectedDate()));
        notification.$x(".//button").click();
        notification.should(hidden);
    }

    @Test
    public void shouldValidNameTest3() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Махачкала");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Хусаинов Ахмед Оглы");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").should(text("Успешно"));
        notification.$x(".//div[@class='notification__content']").should(text("Встреча успешно забронирована на "
                + getExpectedDate()));
        notification.$x(".//button").click();
        notification.should(hidden);
    }

    @Test
    public void shouldInvalidNameTest1() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Тюмень");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Lolita Lolita");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79199552643");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='name']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='name']//child::span[@class='input__sub']").
                should(visible, text("Имя и Фамилия указаные неверно"));
    }

    @Test
    public void shouldInvalidNameTest2() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Москва");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Ив@нов Иван");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='name']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='name']//child::span[@class='input__sub']").
                should(visible, text("Имя и Фамилия указаные неверно"));
    }


    @Test
    public void shouldInvalidPhoneTest1() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Тюмень");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Шляк Лолита");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+791995552643");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='phone']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='phone']//child::span[@class='input__sub']").
                should(visible, text("Телефон указан неверно"));
    }

    @Test
    public void shouldInvalidPhoneTest2() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Тюмень");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Шляк Лолита");
        form.$x(".//span[@data-test-id='phone']//child::input").val("89211234567");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='phone']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='phone']//child::span[@class='input__sub']").
                should(visible, text("Телефон указан неверно"));
    }

    @Test
    public void shouldInvalidPhoneTest3() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Тюмень");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Шляк Лолита");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+7921@002459");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='phone']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='phone']//child::span[@class='input__sub']").
                should(visible, text("Телефон указан неверно"));
    }

    @Test
    public void shouldEmptyCityTest() {
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Шляк Лолита");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79199552643");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='city']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='city']//child::span[@class='input__sub']").
                should(visible, text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldEmptyNameTest() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Тюмень");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79199552643");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='name']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='name']//child::span[@class='input__sub']").
                should(visible, text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldEmptyPhoneTest() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Тюмень");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Шляк Лолита");
        form.$x(".//label[@data-test-id='agreement']").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//span[@data-test-id='phone']").should(cssClass("input_invalid"));
        form.$x(".//span[@data-test-id='phone']//child::span[@class='input__sub']").
                should(visible, text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldEmptyCheckboxTest() {
        form.$x(".//span[@data-test-id='city']//child::input").val("Тюмень");
        String inputMeetingDate = inputMeetingDate(meetingDate(3));
        form.$x(".//span[@data-test-id='date']//child::input[@placeholder]").val(inputMeetingDate);
        form.$x(".//span[@data-test-id='name']//child::input").val("Шляк Лолита");
        form.$x(".//span[@data-test-id='phone']//child::input").val("+79199552643");
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        form.$x(".//label[@data-test-id='agreement']").
                should(cssClass("input_invalid"));
    }

}
