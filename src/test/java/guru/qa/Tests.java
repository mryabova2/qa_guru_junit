package guru.qa;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.Keys;
import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class Tests {

    @BeforeAll
    static void beforeAll() {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void openPerek() {
        open("https://www.perekrestok.ru");
    }


    @ValueSource(strings = {
            "Огурцы",
            "Яблоки"
    })
    @ParameterizedTest(name = "Проверка поиска в Перекрестке по слову {0}")
    void perekSearchTest(String testData) {

        $("[name=search]").setValue(testData);
        $("button[type=submit]").click();
        $(".catalog-content__list")
                .shouldHave(text(testData));
    }


    @CsvSource({
            "Помидоры, томаты",
            "Грибы, шампиньоны"
    })

    @ParameterizedTest(name = "Проверка поиска в Перекрестке по слову {0}, результат {1}")
    void perekSearchTest1(String testData, String expectedResult) {

        $("[name=search]").setValue(testData);
        $("button[type=submit]").click();
        $(".catalog-content__list")
                .shouldHave(text(expectedResult));
    }

    static Stream<Arguments> perekSearchTest2() {
        return Stream.of(
                Arguments.of("Огурцы", List.of("150","500")),
                Arguments.of("Помидоры", List.of("100","300"))
        );
    }

    @MethodSource("perekSearchTest2")
    @ParameterizedTest(name = "Проверка фильтра по цене в Перекрестке")
    void perekSearchTest2(String name, List<String> priceLim) {
        $("[name=search]").setValue(name);
        $("button[type=submit]").click();
        $(" .catalog-price-filter__value-container input[aria-label='Минимальная цена']").sendKeys(Keys.CONTROL + "A");
        $(" .catalog-price-filter__value-container input[aria-label='Минимальная цена']").sendKeys(Keys.BACK_SPACE);
        $(" .catalog-price-filter__value-container input[aria-label='Минимальная цена']").setValue(priceLim.get(0));
        $(" .catalog-price-filter__value-container_right input[aria-label='Максимальная цена']").sendKeys(Keys.CONTROL + "A");
        $(" .catalog-price-filter__value-container_right input[aria-label='Максимальная цена']").sendKeys(Keys.BACK_SPACE);
        $(" .catalog-price-filter__value-container_right input[aria-label='Максимальная цена']").setValue(priceLim.get(1));

    }
}
